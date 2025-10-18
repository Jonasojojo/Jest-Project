public class Deck extends AbstractCardStack {

    public Deck() {
        createBaseDeck();
    }

    private void createBaseDeck() {
        for (Card.Suit suit : Card.Suit.values()) {
            if (suit != Card.Suit.JOKER) {
                for (int rank = 1; rank <= 4; rank++) {
                    addCard(new Card(suit, rank));
                }
            }
        }
        addCard(new Card(Card.Suit.JOKER, 0));
    }
}
