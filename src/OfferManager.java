import java.util.Scanner;

public class OfferManager {
    private final java.util.List<Player> players;

    public OfferManager(java.util.List<Player> players) {
        this.players = players;
    }

    public void collectOffers() {
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
}
