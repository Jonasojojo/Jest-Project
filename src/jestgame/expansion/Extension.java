package jestgame.expansion;
import jestgame.model.Card;
import jestgame.model.Deck;
import jestgame.model.Trophy;
import jestgame.visitors.CardValueVisitor;

public interface Extension {
    String getName();

    void addCardsToDeck(Deck deck);

    default Trophy.ObtainConditions getTrophyCondition(Card card) {
        return null;
    }

    default Integer getCustomCardValue(Card card, CardValueVisitor visitor) {
        return null;
    }
}