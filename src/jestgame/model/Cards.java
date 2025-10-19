package jestgame.model;

import java.util.ArrayList;
import java.util.List;

public class Cards {
    private List<Card> cardList = new ArrayList<>();

    public List<Card> getCardList() {
        return cardList;
    }

    public void addCardToCards(Card card) {
        cardList.add(card);
    }

    public void removeCardToCards(Card card){ cardList.remove(card); }
}
