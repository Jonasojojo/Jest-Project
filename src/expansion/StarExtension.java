package expansion;
import java.util.*;
import model.*;
import visitors.*;

public class StarExtension implements Extension {

    @Override
    public String getName() {
        return "Star Expansion";
    }

    @Override
    public void addCardsToDeck(Deck deck) {
        for (int rank = 1; rank <= 4; rank++) {
            deck.addCard(new Card(Card.Suit.STAR, rank));
        }
    }

    @Override
    public Trophy.ObtainConditions getTrophyCondition(Card card) {
        if (card.getSuit() == Card.Suit.STAR) {
            return switch (card.getRank()) {
                case 1 -> Trophy.ObtainConditions.bestJest;
                case 2 -> Trophy.ObtainConditions.majorityAs;
                case 3 -> Trophy.ObtainConditions.highestSpade;
                case 4 -> Trophy.ObtainConditions.lowestHeart;
                default -> Trophy.ObtainConditions.bestJest;
            };
        }
        return null;
    }

    @Override
    public Integer getCustomCardValue(Card card, CardValueVisitor visitor) {
        if (card.getSuit() == Card.Suit.STAR) {
            return card.getRank() * 2; // Example: double rank for STAR cards
        }
        return null;
    }
}
