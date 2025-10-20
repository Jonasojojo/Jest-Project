package jestgame.bots;

import jestgame.model.Player;
import jestgame.model.Card;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

public class CollectorBot implements BotStrategy {

    private final Random random = new Random();

    @Override
    public int chooseCardToOffer(Player self) {
        var hand = self.getHand().getCardList();
        Map<String, Long> suitCounts = countSuits(self);

        // Find the most common suit the bot already has
        String favoriteSuit = suitCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // Try to hide the card that belongs to the favorite suit (protect it)
        if (favoriteSuit != null) {
            if (hand.get(0).getSuit().equals(favoriteSuit)) return 1;
            if (hand.get(1).getSuit().equals(favoriteSuit)) return 0;
        }

        // Otherwise, reveal the lower ranked card
        return hand.get(0).getRank() <= hand.get(1).getRank() ? 0 : 1;
    }

    @Override
    public Player chooseTarget(Player self, List<Player> eligibleTargets) {
        Map<String, Long> suitCounts = countSuits(self);
        String favoriteSuit = suitCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        Player bestTarget = null;
        for (Player target : eligibleTargets) {
            Card faceUp = target.getPlayedHand().getCardFaceUp();
            if (faceUp != null && faceUp.getSuit().equals(favoriteSuit)) {
                bestTarget = target;
                break; // Found a card that matches the collection goal
            }
        }

        // If no target matches, pick one randomly
        if (bestTarget == null && !eligibleTargets.isEmpty()) {
            bestTarget = eligibleTargets.get(random.nextInt(eligibleTargets.size()));
        }

        return bestTarget;
    }

    @Override
    public boolean chooseFaceUpOrDown(Player self, Player target) {
        Card faceUp = target.getPlayedHand().getCardFaceUp();
        if (faceUp != null) {
            Map<String, Long> suitCounts = countSuits(self);
            String favoriteSuit = suitCounts.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);

            // Take visible card if it matches the favorite suit
            if (favoriteSuit != null && faceUp.getSuit().equals(favoriteSuit)) {
                return true;
            }
        }

        // Otherwise, small chance (30%) to gamble on a face-down card
        return random.nextDouble() > 0.3;
    }

    private Map<String, Long> countSuits(Player self) {
        Map<String, Long> suitCounts = new HashMap<>();
        for (Card c : self.getJest().getCardList()) {
            suitCounts.put(c.getSuit(), suitCounts.getOrDefault(c.getSuit(), 0L) + 1);
        }
        return suitCounts;
    }

    @Override
    public String toString() {
        return "CollectorBot";
    }
}
