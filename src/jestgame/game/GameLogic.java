package jestgame.game;

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

    public void enableExpansion(Extension expansion) {
        enabledExtension.add(expansion);
    }

    public void setupPhase() {

        for (Extension expansion : enabledExtension) {
            expansion.addCardsToDeck(initialDeck);
        }
        trophyManager = new TrophyManager(enabledExtension);
        initialDeck.shuffle();


        for (int i = 0; i < GameConstants.numberOfPlayers; i++) {
            Player p = new Player("Player " + (i + 1), enabledExtension);
            players.add(p);
        }
        System.out.println("size of players: " + players.size());

        trophyManager.setupTrophies(initialDeck, players.size());
    }


    public void playGame() {
        setupPhase();

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