package jestgame.model;

import jestgame.expansion.Extension;
import jestgame.visitors.CardValueVisitor;

import java.util.List;
import java.util.stream.Collectors;


public class Jest extends Cards {


    private final List<Extension> extensions; // list of active expansions

    // Constructor
    public Jest(List<Extension> extensions) {
        this.extensions = extensions;
    }

    public Jest() {
        this.extensions = List.of();
    }


    private long numberOfPairs() {
        return getCardList().stream()
                .filter(c -> c.getSuit() == Card.Suit.CLUB || c.getSuit() == Card.Suit.SPADE)
                .collect(Collectors.groupingBy(Card::getRank))
                .values().stream()
                .filter(list -> list.size() == 2)
                .count();
    }


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
