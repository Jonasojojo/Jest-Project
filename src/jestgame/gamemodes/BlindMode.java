package jestgame.gamemodes;
import jestgame.model.*;
import java.util.*;

public class BlindMode implements GameMode{

    @Override
    public String getName() {
        return "The Blind";
    }

    @Override
    public void onOffer(Player player) {
        List<Card> hand = player.getHand().getCardList();
        if (hand.size() < 2) return;

        Scanner sc = new Scanner(System.in);
        System.out.println("\n" + player.getPlayerName() + ", choose the order of your cards (both will be hidden).");
        System.out.println("1: " + hand.get(0));
        System.out.println("2: " + hand.get(1));

        int choice = -1;
        while (true) {
            System.out.print("Which card should be on the LEFT? (1 or 2): ");
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                if (choice == 1 || choice == 2) break;
            } else sc.next();
            System.out.println("Invalid input.");
        }

        Card left = hand.get(choice - 1);
        Card right = hand.get(1 - (choice - 1));

        // Both hidden to others, but player still sets order
        player.getPlayedHand().setCardFaceDown(left);
        player.getPlayedHand().setCardFaceUp(right); // logically second, but still hidden to others

        // Optionally hide printing them
        hand.clear();
    }

    @Override
    public boolean hideOffersFromOthers() {
        return true;
    }
}
