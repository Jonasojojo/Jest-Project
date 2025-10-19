package model;

import expansion.Extension;
import game.GameLogic;
import visitors.CardValueVisitor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Jest hand evaluator.
 * Extends `Cards` and provides logic to compute the total points for this hand.
 */
public class Jest extends Cards {


    private final List<Extension> extensions; // list of active expansions

    // Constructor
    public Jest(List<Extension> extensions) {
        this.extensions = extensions;
    }

    public Jest() {
        this.extensions = List.of();
    }

    /**
     * Count the number of valid pairs in the hand.

     * A "pair" here is defined as exactly two cards of the same rank,
     * but only clubs or spades are considered.

     * Stream steps:
     *  - filter: keep only clubs and spades
     *  - groupingBy: group remaining cards by rank
     *  - values().stream(): iterate over the groups
     *  - filter(list -> list.size() == 2): keep only groups with exactly two cards
     *  - count(): return the number of such groups
     */
    private long numberOfPairs() {
        return getCardList().stream()
                .filter(c -> c.getSuit() == Card.Suit.CLUB || c.getSuit() == Card.Suit.SPADE)
                .collect(Collectors.groupingBy(Card::getRank))
                .values().stream()
                .filter(list -> list.size() == 2)
                .count();
    }

    /**
     * Calculate total points for this hand.

     * Steps:
     *  - Create a CardValueVisitor with the hand context (e.g. hearts, jokers).
     *  - For each card: accept the visitor and read the value produced by the visitor.
     *    (The visitor is reused; it is expected to update its value when visiting each card.)
     *  - Sum all card values and add pair bonus points (numberOfPairs * GameConstants.valueOfPairs).
     */
    public long calculatePoints() {
        CardValueVisitor visitor = new CardValueVisitor(getCardList(), extensions);

        long cardPoints = getCardList().stream()
                .mapToInt(card -> {
                    card.accept(visitor);
                    return visitor.getValue();
                })
                .sum();

        long pairPoints = numberOfPairs() * GameConstants.valueOfPairs;

        return cardPoints + pairPoints;
    }
}
