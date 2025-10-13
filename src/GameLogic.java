import java.util.*;

public class GameLogic {
    private Deck initialDeck = new Deck();
    private RoundStack roundStack = new RoundStack();
    private List<Trophy> trophies = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private boolean isFirstRound = true;

    public void setupPhase() {
        initialDeck.shuffle();
        setTrophies();

        for (int i = 0; i < GameConstants.numberOfPlayers; i++) {
            Player p = new Player("Player " + (i + 1));
            players.add(p);
        }
    }

    private void setTrophies() {
        if (GameConstants.numberOfPlayers == 3) {
            trophies.add(new Trophy(initialDeck.drawCard()));
            System.out.println("the first trophy is " + trophies.get(0).getRank() + trophies.get(0).getSuit() + " win condition is " + trophies.get(0).getObtainConditions());
            trophies.add(new Trophy(initialDeck.drawCard()));
            System.out.println("the second trophy is " + trophies.get(1).getRank() + trophies.get(1).getSuit() + " win condition is " + trophies.get(1).getObtainConditions());
        } else {
            trophies.add(new Trophy(initialDeck.drawCard()));
            System.out.println("the first trophy is " + trophies.get(0).getRank() + trophies.get(0).getSuit() + " win condition is " + trophies.get(0).getObtainConditions());
        }
    }

    public List<Trophy> getTrophies(){
        return trophies;
    }

    public void playGame() {

        setupPhase();
        while (initialDeck.size() >= GameConstants.numberOfPlayers * 2 ) {

            dealCards();
            makeOffers();
            takeTurns();
            isFirstRound = false;
        }

        finalizeJests();
        awardTrophies();
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

    private void takeTurns() {
        Set<Player> playedThisRound = new HashSet<>();
        Deque<Player> turnQueue = new ArrayDeque<>();

        // Determine the first player based on face-up card & suit
        Player firstPlayer = getFirstPlayer(players);
        turnQueue.add(firstPlayer);

        while (!turnQueue.isEmpty()) {
            Player current = turnQueue.poll();
            playedThisRound.add(current);

            // Get eligible targets for stealing
            List<Player> targets = getEligibleTargets(current, players);

            Player target;
            if (!targets.isEmpty()) {
                target = chooseTarget(current, targets);
            } else if (current.getPlayedHand().getCardFaceUp() != null && current.getPlayedHand().getCardFaceDown() !=null) {
                // Final player takes from own offer
                target = current;
            } else {
                continue; // no eligible card to take
            }

            // Take the card
            takeCard(current, target);

            // Determine who goes next
            if (target != current && !playedThisRound.contains(target)) {
                // Stolen-from player goes next
                turnQueue.addFirst(target);
            } else {
                Player next = getNextPlayer(playedThisRound, players);
                if (next != null) turnQueue.addFirst(next);
            }
        }
    }
    private Player getFirstPlayer(List<Player> players) {
        return players.stream()
                .filter(p -> p.getPlayedHand().getCardFaceUp() != null)
                .max(Comparator
                        .comparingInt((Player p) -> p.getPlayedHand().getCardFaceUp().getRank())
                        .thenComparingInt(p -> getSuitStrength(p.getPlayedHand().getCardFaceUp().getSuit())))
                .orElseThrow(() -> new IllegalStateException("No player has a face-up card!"));
    }



    private List<Player> getEligibleTargets(Player current, List<Player> players) {
        return players.stream()
                .filter(p -> p != current
                        && p.getPlayedHand().getCardFaceDown() != null
                        && p.getPlayedHand().getCardFaceUp() != null)
                .toList();
    }


    private Player chooseTarget(Player current, List<Player> targets) {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n" + current.getPlayerName() + ", choose a player to steal from:");
        for (int i = 0; i < targets.size(); i++) {
            System.out.println((i+1) + ": " + targets.get(i).getPlayerName());
            System.out.println(" he has " + targets.get(i).getPlayedHand().getCardFaceUp() + " hidden card");
        }

        int choice = -1;
        while (true) {
            System.out.print("Enter number of player to steal from: ");
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                if (choice >= 1 && choice <= targets.size()) break;
            } else {
                sc.next(); // clear invalid input
            }
            System.out.println("Invalid input.");
        }

        return targets.get(choice - 1);
    }


    private void takeCard(Player current, Player target) {
        boolean chooseFaceUp = chooseFaceUpOrDown(current, target);

        Card chosenCard;
        if (chooseFaceUp) {
            chosenCard = target.getPlayedHand().getCardFaceUp();
            target.getPlayedHand().setCardFaceUp(null);
        } else {
            chosenCard = target.getPlayedHand().getCardFaceDown();
            target.getPlayedHand().setCardFaceDown(null);
        }

        current.getJest().addCardToCards(chosenCard);
    }

    // Temporary simple logic: always pick face-up (can be replaced by player input)
    private boolean chooseFaceUpOrDown(Player current, Player target) {
        Scanner sc = new Scanner(System.in);
        int choice = 0;

        while (true) {
            System.out.println("\n" + current.getPlayerName() + ", choose a card from " + target.getPlayerName() + "'s offer:");
            System.out.println("1. Face-up: " + target.getPlayedHand().getCardFaceUp());
            System.out.println("2. Face-down: [hidden]");
            System.out.print("Enter 1 or 2: ");

            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                if (choice == 1 || choice == 2) break;
            } else {
                sc.next(); // clear invalid input
            }

            System.out.println("Invalid input. Please enter 1 or 2.");
        }

        return choice == 1; // true = face-up, false = face-down

    }


    private Player getNextPlayer(Set<Player> playedThisRound, List<Player> players) {
        return players.stream()
                .filter(p -> !playedThisRound.contains(p)
                        && p.getPlayedHand().getCardFaceDown() != null
                        && p.getPlayedHand().getCardFaceUp() != null)
                .max(Comparator.comparingInt((Player p) -> p.getPlayedHand().getCardFaceUp().getRank())
                        .thenComparingInt(p -> getSuitStrength(p.getPlayedHand().getCardFaceUp().getSuit())))
                .orElse(null);
    }


    private int getSuitStrength(Card.Suit s) {
        return switch (s) {
            case SPADE -> 4;
            case CLUB -> 3;
            case DIAMOND -> 2;
            case HEART -> 1;
            default -> 0; // Joker
        };
    }







    private void finalizeJests() {
        for (Player p : players) {
            if (p.getPlayedHand().getCardFaceDown() != null) {
                p.getJest().addCardToCards(p.getPlayedHand().getCardFaceDown());
            }
        }
    }

    private void awardTrophies() {
        HashMap<Player, Trophy> trophiesToBeAwarded = new HashMap<>();

        for (Trophy trophy : trophies) {
            Player winner = trophy.awardTrophy(players);

            if (winner != null) {
                // Only add valid winners to the map
                trophiesToBeAwarded.put(winner, trophy);
                System.out.println("DEBUG → Trophy " + trophy + " will be awarded to " + winner.getPlayerName());
            } else {
                System.out.println("DEBUG → Trophy " + trophy + " has no valid winner!");
            }
        }

        // Actually give out the trophies
        for (Player p : trophiesToBeAwarded.keySet()) {
            p.getJest().addCardToCards(trophiesToBeAwarded.get(p));
        }
    }


    private void declareWinner() {
        Player winner = players.stream()
                .max(Comparator.comparingLong(p -> p.getJest().calculatePoints()))
                .orElse(null);

        System.out.println("Winner: " + winner.getPlayerName() + " with " + winner.getJest().calculatePoints() + " points.");
    }
}