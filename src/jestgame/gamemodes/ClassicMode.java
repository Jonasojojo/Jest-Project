package jestgame.gamemodes;

import jestgame.model.Player;

import java.util.Scanner;

public class ClassicMode implements  GameMode {
    @Override
    public String getName() {
        return "Classic";
    }

    @Override
    public void onOffer(Player p) {
        int choice = -1;
        if (p.isBot()) {
            // Use bot strategy
            p.putCardsInOffer(p.getStrategy().chooseCardToOffer(p));

        }
        else{
            Scanner sc = new Scanner(System.in);


            System.out.println("\n" + p.getPlayerName() + ", choose your offer:");
            System.out.println("Your hand: ");
            for (int i = 0; i < p.getHand().getCardList().size(); i++) {
                System.out.println((i + 1) + ": " + p.getHand().getCardList().get(i));
            }


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
            p.putCardsInOffer(choice - 1);
        }

    }

}
