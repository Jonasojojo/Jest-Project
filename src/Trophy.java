import java.util.List;

public class Trophy extends Card{
    private final ObtainConditions obtainConditions;

    public enum ObtainConditions {
        highestHeart, highestDiamond, highestClub, highestSpade,
        lowestHeart, lowestDiamond, lowestClub, lowestSpade,
        majorityAs, majorityTwo, majorityThree,majorityFour,
        joker,
        bestJest,
        bestJestNoJoke,
    }

    public Trophy(Card card) {
        super(card.getSuit(), card.getRank());
        this.obtainConditions = getObtainCondition(card.getSuit(), card.getRank());
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
        } else if (rank == 4){
            return switch (suit) {
                case HEART -> ObtainConditions.joker;
                case DIAMOND -> ObtainConditions.bestJestNoJoke;
                case SPADE -> ObtainConditions.lowestClub;
                case CLUB -> ObtainConditions.lowestSpade;
                default -> throw new IllegalArgumentException("Invalid suit: " + suit);
            };
        }
        else { //joker case
            return ObtainConditions.bestJest;
        }
    }

    public Player awardTrophy(List<Player> players){
        return switch (obtainConditions){
            case highestClub -> awardHighest(players, Suit.CLUB) ;
            case highestDiamond -> awardHighest(players, Suit.DIAMOND);
            case highestSpade -> awardHighest(players, Suit.SPADE);
            case highestHeart -> awardHighest(players, Suit.HEART);
            case lowestClub -> awardLowest(players, Suit.CLUB);
            case lowestDiamond -> awardLowest(players, Suit.DIAMOND);
            case lowestSpade -> awardLowest(players, Suit.SPADE);
            case lowestHeart -> awardLowest(players, Suit.HEART);
            case majorityAs -> awardMajority(players, 1);
            case majorityTwo -> awardMajority(players, 2);
            case majorityThree -> awardMajority(players, 3);
            case majorityFour -> awardMajority(players, 4);
            case joker ->  awardJoker(players);
            case bestJest -> awardBestJest(players);
            case bestJestNoJoke -> awardBestJestNoJoke(players);
        };
    }

    private Player awardHighest(List<Player> players, Suit suit){
        Player awardedPlayer = null;
        int bestCardOfSuit = -1;
        for (Player p : players){
            for(Card c : p.getJest().getCardList()){
                if (c.getSuit()==suit){
                    int cardValue = p.getJest().createCardValue(c);
                    if ( cardValue > bestCardOfSuit){
                        awardedPlayer = p;
                        bestCardOfSuit = cardValue;
                    }

                }
            }
        }
        return awardedPlayer;
    }

    private Player awardLowest(List<Player> players, Suit suit){
        Player awardedPlayer = null;
        int worstCardOfSuit = 100;
        for (Player p : players){
            for(Card c : p.getJest().getCardList()){
                if (c.getSuit()==suit){
                    int cardValue = p.getJest().createCardValue(c);
                    if ( cardValue < worstCardOfSuit){
                        awardedPlayer = p;
                        worstCardOfSuit = cardValue;
                    }

                }
            }
        }
        return awardedPlayer;
    }

    private Player awardMajority(List<Player> players, int rank){
        Player awardedPlayer = null;
        int bestNumberOfRank=0;
        for (Player p : players){
            int numberOfRank = 0;
            for (Card c : p.getJest().getCardList()){
                if (c.getRank() == rank){
                    numberOfRank++;
                }
            }
            if (numberOfRank> bestNumberOfRank){
                bestNumberOfRank = numberOfRank;
                awardedPlayer = p;
            }
        }
        return awardedPlayer;
    }

    private Player awardJoker(List<Player> players){
        Player awardedPlayer = null;
        outerLoop:
        for (Player p : players){
            for (Card c : p.getJest().getCardList()){
                if (c.getSuit() == Suit.JOKER ){
                    awardedPlayer = p;
                    break outerLoop;
                }
            }
        }
        return awardedPlayer;
    }

    private Player awardBestJest(List<Player> players){
        Player awardedPlayer = null;
        long bestJest=-100;
        for (Player p : players){
            long jestPoints=p.getJest().calculatePoints();
            if (jestPoints > bestJest){
                bestJest = jestPoints;
                awardedPlayer = p;
            }
        }
        return awardedPlayer;
    }

    private Player awardBestJestNoJoke(List<Player> players) {
        Player awardedPlayer = null;
        long bestJest = -100;
        for (Player p : players) {


            boolean containsJoker = false;
            for (Card c : p.getJest().getCardList()) {
                if (c.getSuit() == Suit.JOKER) {
                    containsJoker = true;
                    break;
                }
            }


            if (!containsJoker) {
                long jestPoints = p.getJest().calculatePoints();
                if (jestPoints > bestJest) {
                    bestJest = jestPoints;
                    awardedPlayer = p;
                }
            } else {
                Jest copyNoJoke = p.getJest();
                copyNoJoke.getCardList().removeIf(card -> card.getSuit() == Suit.JOKER);
                long jestPoints = copyNoJoke.calculatePoints();
                if (jestPoints > bestJest) {
                    bestJest = jestPoints;
                    awardedPlayer = p;
                }
            }

        }
        return awardedPlayer;
    }
}
