package model;

import java.util.*;

public abstract class AbstractCardStack extends Cards {

    public void shuffle() {
        Collections.shuffle(getCardList());
    }

    public Card drawCard() {
        if (!getCardList().isEmpty()) {
            return getCardList().remove(0);
        }
        return null;
    }

    public void addCard(Card card) {
        getCardList().add(card);
    }

    public void addAll(Collection<Card> cards) {
        getCardList().addAll(cards);
    }

    public int size() {
        return getCardList().size();
    }

    public boolean isEmpty() {
        return getCardList().isEmpty();
    }
}
