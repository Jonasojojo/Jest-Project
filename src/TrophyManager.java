import java.util.*;

public class TrophyManager {
    private final List<Trophy> trophies = new ArrayList<>();

    public void setupTrophies(Deck deck, int numberOfPlayers) {
        trophies.clear();
        if (GameConstants.numberOfPlayers == 3) {
            trophies.add(new Trophy(deck.drawCard()));
            System.out.println("the first trophy is " + trophies.get(0).getRank() + trophies.get(0).getSuit() + " win condition is " + trophies.get(0).getObtainConditions());
            trophies.add(new Trophy(deck.drawCard()));
            System.out.println("the second trophy is " + trophies.get(1).getRank() + trophies.get(1).getSuit() + " win condition is " + trophies.get(1).getObtainConditions());
        } else {
            trophies.add(new Trophy(deck.drawCard()));
            System.out.println("the first trophy is " + trophies.get(0).getRank() + trophies.get(0).getSuit() + " win condition is " + trophies.get(0).getObtainConditions());
        }
    }

    public List<Trophy> getTrophies() {
        return trophies;
    }

    public void awardTrophies(List<Player> players) {
        Map<Player, Trophy> trophiesToBeAwarded = new HashMap<>();
        for (Trophy trophy : trophies) {
            Player winner = trophy.awardTrophy(players);
            if (winner != null) {
                trophiesToBeAwarded.put(winner, trophy);
            }
        }
        for (Player p : trophiesToBeAwarded.keySet()) {
            p.getJest().addCardToCards(trophiesToBeAwarded.get(p));
        }
    }
}