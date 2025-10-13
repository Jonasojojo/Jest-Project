import java.util.*;

public class HandToPlay extends Cards {
    private List<Card> cardList = new ArrayList<>();

    public List<Card> getCardList() {
        return cardList; // return the actual list, not a new one
    }

    public void addCardToCards(Card card) {
        if (card != null) cardList.add(card);
    }
}
