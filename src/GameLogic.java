import java.util.*;

public class GameLogic {
    private Deck initialDeck = new Deck();
    private RoundStack roundStack = new RoundStack();
    private final TrophyManager trophyManager = new TrophyManager();
    private List<Player> players = new ArrayList<>();
    private boolean isFirstRound = true;

    public void setupPhase() {
        initialDeck.shuffle();
        trophyManager.setupTrophies(initialDeck, players.size());

        for (int i = 0; i < GameConstants.numberOfPlayers; i++) {
            Player p = new Player("Player " + (i + 1));
            players.add(p);
        }
    }


    public void playGame() {
        setupPhase();

        while (initialDeck.size() >= GameConstants.numberOfPlayers * 2 ) {
            dealCards();

            makeOffers();

            TurnManager turnManager = new TurnManager(players);
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
            roundStack = new RoundStack();
            roundStack.prepare(players, initialDeck);

            for (Player p : players) {
                p.getHand().addCardToCards(roundStack.drawCard());
                p.getHand().addCardToCards(roundStack.drawCard());
            }
        }
    }


    private void makeOffers() {
        for (Player p : players) {
            if (p.getHand().getCardList().size() < 2) {
                continue;
            }
            int choice = promptFaceUpCard(p);
            p.putCardsInOffer(choice - 1);
        }
    }


    private int promptFaceUpCard(Player p) {
        Scanner sc = new Scanner(System.in);


        System.out.println("\n" + p.getPlayerName() + ", choose your offer:");
        System.out.println("Your hand: ");
        for (int i = 0; i < p.getHand().getCardList().size(); i++) {
            System.out.println((i + 1) + ": " + p.getHand().getCardList().get(i));
        }

        int choice = -1;
        while (true) {
            System.out.print("Which card should be face-up (enter 1 or 2)? ");
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                if (choice == 1 || choice == 2) break;
            } else {
                sc.next(); // clear invalid input
            }
            System.out.println("Invalid input. Enter 1 or 2.");
        }
        return choice;
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

        System.out.println("Winner: " + winner.getPlayerName() + " with " + winner.getJest().calculatePoints() + " points.");
    }
}