package jestgame.bots;

import jestgame.model.Player;
import jestgame.model.Card;
import java.util.List;
import java.util.Random;

public class RiskyBot implements BotStrategy {

    private final Random random = new Random();

    @Override
    public int chooseCardToOffer(Player self) {
        var hand = self.getHand().getCardList();

        // 70% chance to bluff (hide strong card), 30% to play strong card face-up
        boolean bluff = random.nextDouble() < 0.7;
        if (bluff) {
            // hide the stronger card (put it face down)
            return hand.get(0).getRank() < hand.get(1).getRank() ? 0 : 1;
        } else {
            // play stronger card face-up
            return hand.get(0).getRank() >= hand.get(1).getRank() ? 0 : 1;
        }
    }

    @Override
    public Player chooseTarget(Player self, List<Player> eligibleTargets) {
        // 50% chance to pick the strongest visible, 50% random
        if (random.nextBoolean()) {
            Player bestTarget = null;
            int bestRank = Integer.MIN_VALUE;
            for (Player target : eligibleTargets) {
                Card visible = target.getPlayedHand().getCardFaceUp();
                if (visible != null && visible.getRank() > bestRank) {
                    bestRank = visible.getRank();
                    bestTarget = target;
                }
            }
            return bestTarget;
        } else {
            // Random target
            return eligibleTargets.get(random.nextInt(eligibleTargets.size()));
        }
    }

    @Override
    public boolean chooseFaceUpOrDown(Player self, Player target) {
        // 30% chance to take face-down card (risky choice)
        if (target.getPlayedHand().getCardFaceDown() != null && random.nextDouble() < 0.3) {
            return false; // choose face-down
        }
        return true; // choose face-up
    }

    @Override
    public String toString() {
        return "RiskyBot";
    }
}
