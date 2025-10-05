import java.util.ArrayList;

public class Card {
    private final String suit;
    private final int rank;
    private int currentValue;

    public Card(String suit, int rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public String getSuit() {
        return suit;
    }

    public int getRank() {
        return rank;
    }


    private boolean isAceWorthFive(ArrayList<Card> hand) {          //Checks if number of Aces si equal to 1 to see if it's worth extra
        if (rank != 1 ){ return false; }
        long numberOfAces = hand.stream().filter(card -> card.getRank() == this.rank).count();
        return numberOfAces > 1;
    }

    private int createCardValue(ArrayList<Card> hand) {
        int value = rank;
        if (rank ==1 && isAceWorthFive(hand)) { value = 5;}
        switch (this.suit) {
            case "Hearts" -> currentValue = 0;      //TODO conditions for if there is a joker in hand + hearts
            case "Spades" -> currentValue = -value;
            default -> currentValue = value;
        }

        return currentValue;
    }
}
