package jestgame.model;

public class PlayedHand{
    private Card cardFaceUp;
    private Card cardFaceDown;

    public void setCardFaceDown(Card cardFaceDown) {
        this.cardFaceDown = cardFaceDown;
    }

    public void setCardFaceUp(Card cardFaceUp) {
        this.cardFaceUp = cardFaceUp;
    }

    public Card getCardFaceDown() {
        return cardFaceDown;
    }

    public Card getCardFaceUp() {
        return cardFaceUp;
    }


}
