package jestgame.bots;

import jestgame.model.Card;
import jestgame.model.Player;

import java.util.List;

public class MaxPoints implements BotStrategy {
    @Override
    public int chooseCardToOffer(Player self) {
        var hand = self.getHand().getCardList();
        // Play the higher-ranked card face-up to show strength
        return hand.get(0).getRank() >= hand.get(1).getRank() ? 0 : 1;
    }

    @Override
    public Player chooseTarget(Player self, List<Player> eligibleTargets) {
        Player bestTarget = null;
        int bestFaceUpValue = Integer.MIN_VALUE;

        for (Player target : eligibleTargets) {
            Card faceUp = target.getPlayedHand().getCardFaceUp();
            if (faceUp != null && faceUp.getRank() > bestFaceUpValue) {
                bestFaceUpValue = faceUp.getRank();
                bestTarget = target;
            }
        }

        return bestTarget;
    }

    @Override
    public boolean chooseFaceUpOrDown(Player self, Player target) {
        // Since the bot can't see the face-down card, it will always take the face-up card if it exists
        return target.getPlayedHand().getCardFaceUp() != null;
    }
}
