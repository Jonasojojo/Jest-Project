package model;
import visitors.*;

public class Card {


    public enum Suit {
        HEART,
        DIAMOND,
        CLUB,
        SPADE,
        STAR,
        JOKER
    }


    private final Suit suit; //if joker, the suit is JOKER
    private final int rank; //if joker, the rank is 0


    public Card(Suit suit, int rank) {
        this.suit = suit;
        this.rank = rank;
    }


    public Suit getSuit() {
        return suit;
    }

    public int getRank() {
        return rank;
    }

    @Override
    public String toString(){
        if (suit == Suit.JOKER) {
            return "JOKER";
        }
        String suitSymbol = switch (suit) {
            case HEART -> "Heart";
            case DIAMOND -> "Diamond";
            case CLUB -> "Club";
            case SPADE -> "Spade";
            case STAR -> "Star";
            default -> "afafafaf";
        };

        return rank + suitSymbol;
    }

    public void accept(CardVisitor visitor) {
        visitor.visit(this);
    }

}
