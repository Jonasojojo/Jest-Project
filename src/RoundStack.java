import java.util.*;

public class RoundStack extends AbstractCardStack {

    public void prepare(List<Player> players, Deck deck) {
        // Collect leftover cards from last round
        for (Player p : players) {
            Card leftover = p.getPlayedHand().getCardFaceUp() != null
                    ? p.getPlayedHand().getCardFaceUp()
                    : p.getPlayedHand().getCardFaceDown();

            if (leftover != null) {
                addCard(leftover);

                // Clear the offer for next round
                p.getPlayedHand().setCardFaceUp(null);
                p.getPlayedHand().setCardFaceDown(null);
            }
            // Add as many new cards as there are players
            for (int i = 0; i < players.size(); i++) {
                Card newCard = deck.drawCard();
                if (newCard != null) addCard(newCard);
            }

            shuffle();
        }
    }
}
