import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Jest {
    private List<Card> jest = new ArrayList<>();

    public List<Card> getJest() {
        return jest;
    }

    public void addCardToJest(Card card) {
        jest.add(card);
    }

    // Count hearts in the hand
    private long numberOfHearts() {
        return jest.stream()
                .filter(card -> card.getSuit() == Suit.HEART)
                .count();
    }

    // Check if a joker is in hand
    private boolean jokerInHand() {
        return jest.stream()
                .anyMatch(card -> card.getSuit() == Suit.JOKER);
    }

    // Check if there's exactly one Ace
    private boolean isAceWorthFive() {
        return jest.stream()
                .filter(card -> card.getRank() == 1)
                .count() == 1;
    }

    // Calculate the value of a single card
    private int createCardValue(Card card) {
        int rank = card.getRank();
        Suit suit = card.getSuit();
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
        return jest.stream()
                .filter(c -> c.getSuit() == Suit.CLUB || c.getSuit() == Suit.SPADE)
                .collect(Collectors.groupingBy(Card::getRank))
                .values().stream()
                .filter(list -> list.size()==2)
                .count();
    }

    // Calculate total points
    public long calculatePoints() {
        long cardPoint= jest.stream()
                .mapToInt(this::createCardValue) // apply createCardValue to each card
                .sum();
        long pairPoints = numberOfPairs()*GameConstants.valueOfPairs;
        return cardPoint+pairPoints;
    }
}
