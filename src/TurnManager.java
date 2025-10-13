import java.util.*;

public class TurnManager {
    private final List<Player> players;

    public TurnManager(List<Player> players) {
        this.players = players;
    }

    public void executeTurns() {
        Set<Player> playedThisRound = new HashSet<>();
        Deque<Player> turnQueue = new ArrayDeque<>();

        Player firstPlayer = getFirstPlayer();
        turnQueue.add(firstPlayer);

        while (!turnQueue.isEmpty()) {
            Player current = turnQueue.poll();
            playedThisRound.add(current);

            System.out.println("DEBUG starting target selection for " + current.getPlayerName());
            List<Player> targets = getEligibleTargets(current);

            Player target;
            if (!targets.isEmpty()) {
                target = chooseTarget(current, targets);
            } else if (current.getPlayedHand().getCardFaceUp() != null && current.getPlayedHand().getCardFaceDown() != null) {
                target = current;
            } else {
                System.out.println("DEBUG no card found for " + current.getPlayerName());
                continue;
            }

            takeCard(current, target);

            if (target != current && !playedThisRound.contains(target)) {
                turnQueue.addFirst(target);
            } else {
                Player next = getNextPlayer(playedThisRound);
                if (next != null) turnQueue.addFirst(next);
            }
        }
    }

    private Player getFirstPlayer() {
        return players.stream()
                .filter(p -> p.getPlayedHand().getCardFaceUp() != null)
                .max(Comparator
                        .comparingInt((Player p) -> p.getPlayedHand().getCardFaceUp().getRank())
                        .thenComparingInt(p -> getSuitStrength(p.getPlayedHand().getCardFaceUp().getSuit())))
                .orElseThrow(() -> new IllegalStateException("No player has a face-up card!"));
    }

    private List<Player> getEligibleTargets(Player current) {
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


    private Player getNextPlayer(Set<Player> playedThisRound) {
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
            default -> 0;
        };
    }
}