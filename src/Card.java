import java.util.ArrayList;
import java.util.Objects;

public class Card {
    private final Suit suit; //if joker, the suit is JOKER
    private final int rank; //if joker, the rank is 0

    public Card(Suit suit, int rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return this.suit;
    }

    public int getRank() {
        return this.rank;
    }
}
