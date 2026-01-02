package jestgame.model;

import jestgame.expansion.*;
import jestgame.visitors.*;

import java.util.List;

public class Trophy extends Card {
    private final ObtainConditions obtainConditions;

    public enum ObtainConditions {
        highestHeart, highestDiamond, highestClub, highestSpade,
        lowestHeart, lowestDiamond, lowestClub, lowestSpade,
        majorityAs, majorityTwo, majorityThree, majorityFour,
        joker,
        bestJest,
        bestJestNoJoke,
    }

    public Trophy(Card card, List<Extension> extensions) {
        super(card.getSuit(), card.getRank());

        ObtainConditions cond = null;
        for (Extension e : extensions) {
            cond = e.getTrophyCondition(card);
            if (cond != null) break;
        }

        if (cond != null) {
            this.obtainConditions = cond;
        } else {
            this.obtainConditions = getObtainCondition(card.getSuit(), card.getRank());
        }
    }

    public ObtainConditions getObtainConditions() {
        return obtainConditions;
    }

    private ObtainConditions getObtainCondition(Card.Suit suit, int rank) {
        if (rank == 1) {
            return switch (suit) {
                case HEART -> ObtainConditions.joker;
                case DIAMOND -> ObtainConditions.majorityFour;
                case SPADE -> ObtainConditions.highestClub;
                case CLUB -> ObtainConditions.highestSpade;
                default -> throw new IllegalArgumentException("Invalid suit: " + suit);
            };
        } else if (rank == 2) {
            return switch (suit) {
                case HEART -> ObtainConditions.joker;
                case DIAMOND -> ObtainConditions.highestDiamond;
                case SPADE -> ObtainConditions.majorityThree;
                case CLUB -> ObtainConditions.lowestHeart;
                default -> throw new IllegalArgumentException("Invalid suit: " + suit);
            };
        } else if (rank == 3) {
            return switch (suit) {
                case HEART -> ObtainConditions.joker;
                case DIAMOND -> ObtainConditions.lowestDiamond;
                case SPADE -> ObtainConditions.majorityTwo;
                case CLUB -> ObtainConditions.highestHeart;
                default -> throw new IllegalArgumentException("Invalid suit: " + suit);
            };
        } else if (rank == 4) {
            return switch (suit) {
                case HEART -> ObtainConditions.joker;
                case DIAMOND -> ObtainConditions.bestJestNoJoke;
                case SPADE -> ObtainConditions.lowestClub;
                case CLUB -> ObtainConditions.lowestSpade;
                default -> throw new IllegalArgumentException("Invalid suit: " + suit);
            };
        } else { // Joker case
            return ObtainConditions.bestJest;
        }
    }

    public Player accept(TrophyAwardVisitor visitor, List<Player> players) {
        return visitor.visit(this, players);
    }
}
