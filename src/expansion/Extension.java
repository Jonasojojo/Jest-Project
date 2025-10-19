package expansion;
import model.Card;
import model.Deck;
import model.Trophy;
import visitors.CardValueVisitor;

import java.util.*;

public interface Extension {
    String getName();

    /** Add extra cards to the deck */
    void addCardsToDeck(Deck deck);

    /** Optional: assign custom trophy condition for a card */
    default Trophy.ObtainConditions getTrophyCondition(Card card) {
        return null;
    }

    /** Optional: assign custom card value for scoring */
    default Integer getCustomCardValue(Card card, CardValueVisitor visitor) {
        return null;
    }
}