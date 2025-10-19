package jestgame.model;

import java.util.List;

public class Deck extends AbstractCardStack {

    public Deck() {
        createBaseDeck();
    }

    private void createBaseDeck() {
        for (Card.Suit suit : List.of(Card.Suit.HEART, Card.Suit.DIAMOND, Card.Suit.CLUB, Card.Suit.SPADE)) {
            for (int rank = 1; rank <= 4; rank++) {
                addCard(new Card(suit, rank));
            }
        }
        addCard(new Card(Card.Suit.JOKER, 0));
    }

}
