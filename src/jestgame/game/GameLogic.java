package jestgame.game;

import jestgame.bots.BotStrategy;
import jestgame.expansion.Extension;
import jestgame.gamemodes.GameMode;
import jestgame.model.*;
import java.util.*;

public class GameLogic {
    private final Deck initialDeck = new Deck();
    private final List<Extension> enabledExtension = new ArrayList<>();
    private TrophyManager trophyManager;
    private final List<Player> players = new ArrayList<>();
    private boolean isFirstRound = true;
    private final GameMode gameMode;

    public GameLogic(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public List<Extension> getEnabledExtension() {
        return enabledExtension;
    }



    public void setupGame(int numHumans, Map<String, BotStrategy> botConfigs) {
        // Add human players
        for (int i = 1; i <= numHumans; i++) {
            players.add(new Player("Human " + i, enabledExtension));
        }

        // Add bots
        int botIndex = 1;
        for (Map.Entry<String, BotStrategy> entry : botConfigs.entrySet()) {
            String name = entry.getKey() + " (Bot)";
            BotStrategy strategy = entry.getValue();
            players.add(new Player(name, strategy, enabledExtension));
            botIndex++;
        }

        // Setup trophies and deck
        for (Extension ext : enabledExtension) {
            ext.addCardsToDeck(initialDeck);
        }

        trophyManager = new TrophyManager(enabledExtension);
        initialDeck.shuffle();
        trophyManager.setupTrophies(initialDeck, players.size());
    }

    public void enableExpansion(Extension extension) {
        enabledExtension.add(extension);
    }


    public void playGame() {

        while (initialDeck.size() >= GameConstants.numberOfPlayers * 2 ) {
            dealCards();

            OfferManager offerManager = new OfferManager(players, gameMode);
            offerManager.collectOffers();

            TurnManager turnManager = new TurnManager(players, gameMode);
            turnManager.executeTurns();

            isFirstRound = false;
        }
        finalizeJests();
        trophyManager.awardTrophies(players);
        declareWinner();
    }

    private void dealCards() {
        if (isFirstRound) {
            // First round = draw 2 cards per player from main deck
            for (Player p : players) {
                Card firstCard = initialDeck.drawCard();
                Card secondCard = initialDeck.drawCard();
                p.getHand().addCardToCards(firstCard);
                p.getHand().addCardToCards(secondCard);
            }
        } else {
            // Later rounds = use a RoundStack
            RoundStack roundStack = new RoundStack();
            roundStack.prepare(players, initialDeck);

            for (Player p : players) {
                p.getHand().addCardToCards(roundStack.drawCard());
                p.getHand().addCardToCards(roundStack.drawCard());
            }
        }
    }


    private void finalizeJests() {
        for (Player p : players) {
            if (p.getPlayedHand().getCardFaceDown() != null) {
                p.getJest().addCardToCards(p.getPlayedHand().getCardFaceDown());
            }
        }
    }



    private void declareWinner() {
        Player winner = players.stream()
                .max(Comparator.comparingLong(p -> p.getJest().calculatePoints()))
                .orElse(null);

        assert winner != null;
        System.out.println("Winner: " + winner.getPlayerName() + " with " + winner.getJest().calculatePoints() + " points.");
    }
}