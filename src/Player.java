import java.util.List;

public class Player {
    private Jest jest = new Jest();
    private HandToPlay hand = new HandToPlay();
    private PlayedHand playedHand = new PlayedHand();
    private String playerName;

    public Player(String name){
        this.playerName = name;
    }

    public Jest getJest() {
        return jest;
    }

    public String getPlayerName(){
        return playerName;
    }

    public HandToPlay getHand() {
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