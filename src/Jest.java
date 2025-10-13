import java.util.stream.Collectors;


public class Jest extends Cards {

    // Count hearts in the hand
    private long numberOfHearts() {
        return getCardList().stream()
                .filter(card -> card.getSuit() == Card.Suit.HEART)
                .count();
    }

    // Check if a joker is in hand
    private boolean jokerInHand() {
        return getCardList().stream()
                .anyMatch(card -> card.getSuit() == Card.Suit.JOKER);
    }

    // Check if there's exactly one Ace
    private boolean isAceWorthFive() {
        return getCardList().stream()
                .filter(card -> card.getRank() == 1)
                .count() == 1;
    }

    // Calculate the value of a single card
    public int createCardValue(Card card) {
        int rank = card.getRank();
        Card.Suit suit = card.getSuit();
        long heartsCount = numberOfHearts();
        boolean hasJoker = jokerInHand();

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

        if (rank == 1 && isAceWorthFive()) {
            cardValue *= 5;
        }

        return cardValue;
    }


    private long numberOfPairs (){
        return getCardList().stream()
                .filter(c -> c.getSuit() == Card.Suit.CLUB || c.getSuit() == Card.Suit.SPADE)
                .collect(Collectors.groupingBy(Card::getRank))
                .values().stream()
                .filter(list -> list.size()==2)
                .count();
    }

    // Calculate total points
    public long calculatePoints() {
        long cardPoint= getCardList().stream()
                .mapToInt(this::createCardValue) // apply createCardValue to each card
                .sum();
        long pairPoints = numberOfPairs()*GameConstants.valueOfPairs;
        return cardPoint+pairPoints;
    }
}
