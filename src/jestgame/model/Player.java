package jestgame.model;

import jestgame.expansion.Extension;

import java.util.List;

public class Player {
    private Jest jest;
    private Cards hand = new Cards();
    private PlayedHand playedHand = new PlayedHand();
    private String playerName;

    public Player(String name, List<Extension> extensions) {
        this.playerName = name;
        this.jest = new Jest(extensions);

    }

    public Jest getJest() {
        return jest;
    }

    public String getPlayerName(){
        return playerName;
    }

    public Cards getHand() {
        return hand;
    }

    public PlayedHand getPlayedHand(){
        return playedHand;
    }

    // choose what card is face up
    public void putCardsInOffer(int faceUpIndex) {

        Card faceUp = hand.getCardList().get(faceUpIndex);
        Card faceDown = hand.getCardList().get(1 - faceUpIndex);

        playedHand.setCardFaceUp(faceUp);
        playedHand.setCardFaceDown(faceDown);

        hand.getCardList().clear(); // remove cards from hand
    }
}
