package visitors;

import expansion.Extension;
import model.Card;
import model.Jest;
import model.Player;
import model.Trophy;

import java.util.List;

public class DefaultTrophyAwardVisitor implements TrophyAwardVisitor {
    private final List<Extension> extensions;
    @Override
    public Player visit(Trophy trophy, List<Player> players) {
        return switch (trophy.getObtainConditions()) {
            case highestClub -> awardHighest(players, Card.Suit.CLUB);
            case highestDiamond -> awardHighest(players, Card.Suit.DIAMOND);
            case highestSpade -> awardHighest(players, Card.Suit.SPADE);
            case highestHeart -> awardHighest(players, Card.Suit.HEART);
            case lowestClub -> awardLowest(players, Card.Suit.CLUB);
            case lowestDiamond -> awardLowest(players, Card.Suit.DIAMOND);
            case lowestSpade -> awardLowest(players, Card.Suit.SPADE);
            case lowestHeart -> awardLowest(players, Card.Suit.HEART);
            case majorityAs -> awardMajority(players, 1);
            case majorityTwo -> awardMajority(players, 2);
            case majorityThree -> awardMajority(players, 3);
            case majorityFour -> awardMajority(players, 4);
            case joker -> awardJoker(players);
            case bestJest -> awardBestJest(players);
            case bestJestNoJoke -> awardBestJestNoJoke(players);
        };
    }

    public DefaultTrophyAwardVisitor(List<Extension> extensions) {
        this.extensions = extensions;
    }

    private Player awardHighest(List<Player> players, Card.Suit suit) {
        Player awardedPlayer = null;
        int bestValue = Integer.MIN_VALUE;

        for (Player p : players) {
            CardValueVisitor valueVisitor = new CardValueVisitor(p.getJest().getCardList(), extensions);
            for (Card c : p.getJest().getCardList()) {
                if (c.getSuit() == suit) {
                    c.accept(valueVisitor);
                    int val = valueVisitor.getValue();
                    if (val > bestValue) {
                        bestValue = val;
                        awardedPlayer = p;
                    }
                }
            }
        }
        return awardedPlayer;
    }

    private Player awardLowest(List<Player> players, Card.Suit suit) {
        Player awardedPlayer = null;
        int worstValue = Integer.MAX_VALUE;

        for (Player p : players) {
            CardValueVisitor valueVisitor = new CardValueVisitor(p.getJest().getCardList(),extensions);
            for (Card c : p.getJest().getCardList()) {
                if (c.getSuit() == suit) {
                    c.accept(valueVisitor);
                    int val = valueVisitor.getValue();
                    if (val < worstValue) {
                        worstValue = val;
                        awardedPlayer = p;
                    }
                }
            }
        }
        return awardedPlayer;
    }

    private Player awardMajority(List<Player> players, int rank) {
        Player awardedPlayer = null;
        int bestCount = 0;

        for (Player p : players) {
            long count = p.getJest().getCardList().stream()
                    .filter(c -> c.getRank() == rank)
                    .count();

            if (count > bestCount) {
                bestCount = (int) count;
                awardedPlayer = p;
            }
        }
        return awardedPlayer;
    }

    private Player awardJoker(List<Player> players) {
        for (Player p : players) {
            boolean hasJoker = p.getJest().getCardList().stream()
                    .anyMatch(c -> c.getSuit() == Card.Suit.JOKER);
            if (hasJoker) return p;
        }
        return null;
    }

    private Player awardBestJest(List<Player> players) {
        Player awardedPlayer = null;
        long bestScore = Long.MIN_VALUE;

        for (Player p : players) {
            long score = p.getJest().calculatePoints();
            if (score > bestScore) {
                bestScore = score;
                awardedPlayer = p;
            }
        }
        return awardedPlayer;
    }

    private Player awardBestJestNoJoke(List<Player> players) {
        Player awardedPlayer = null;
        long bestScore = Long.MIN_VALUE;

        for (Player p : players) {
            Jest copy = p.getJest();
            copy.getCardList().removeIf(c -> c.getSuit() == Card.Suit.JOKER);
            long score = copy.calculatePoints();
            if (score > bestScore) {
                bestScore = score;
                awardedPlayer = p;
            }
        }
        return awardedPlayer;
    }
}
