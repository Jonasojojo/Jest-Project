package jestgame.visitors;

import jestgame.expansion.Extension;
import jestgame.model.Card;
import jestgame.model.GameConstants;

import java.util.List;

public class CardValueVisitor implements CardVisitor {

    private final List<Extension> extensions;
    private int value;
    private long heartsCount;
    private boolean hasJoker;
    private boolean aceWorthFive;

    public CardValueVisitor(List<Card> cards, List<Extension> extensions) {
        this.extensions = extensions;
        this.heartsCount = cards.stream()
                .filter(c -> c.getSuit() == Card.Suit.HEART)
                .count();

        this.hasJoker = cards.stream()
                .anyMatch(c -> c.getSuit() == Card.Suit.JOKER);

        this.aceWorthFive = cards.stream()
                .filter(c -> c.getRank() == 1)
                .count() == 1;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void visit(Card card) {
        int rank = card.getRank();
        Card.Suit suit = card.getSuit();

        int cardValue = switch (suit) {
            case JOKER -> (heartsCount == 0) ? GameConstants.jokerIfNoHeart : rank;
            case HEART -> {
                if (hasJoker && heartsCount == 4) yield rank;
                else if (hasJoker) yield -rank;
                else yield 0;
            }
            case DIAMOND -> -rank;
            default -> rank;
        };

        for (Extension e : extensions) {
            Integer customValue = e.getCustomCardValue(card, this);
            if (customValue != null) {
                this.value = customValue;
                return;
            }
        }


        if (rank == 1 && aceWorthFive) {
            cardValue *= 5;
        }

        this.value = cardValue;
    }
}
