import java.util.*;

public class TrophyManager {
    private final List<Trophy> trophies = new ArrayList<>();

    public void setupTrophies(Deck deck, int numberOfPlayers) {
        trophies.clear();

        if (numberOfPlayers == 3) {
            addTrophy(deck, "first");
            addTrophy(deck, "second");
        } else {
            addTrophy(deck, "first");
        }
    }

    private void addTrophy(Deck deck, String label) {
        Trophy trophy = new Trophy(deck.drawCard());
        trophies.add(trophy);
        System.out.println("The " + label + " trophy is " +
                trophy.getRank() + trophy.getSuit() +
                " | Win condition: " + trophy.getObtainConditions());
    }

    public List<Trophy> getTrophies() {
        return trophies;
    }

    public void awardTrophies(List<Player> players) {
        Map<Player, Trophy> trophiesToBeAwarded = new HashMap<>();
        TrophyAwardVisitor awardVisitor = new DefaultTrophyAwardVisitor();

        for (Trophy trophy : trophies) {
            Player winner = trophy.accept(awardVisitor, players);
            if (winner != null) {
                trophiesToBeAwarded.put(winner, trophy);
            }
        }

        for (Map.Entry<Player, Trophy> entry : trophiesToBeAwarded.entrySet()) {
            Player player = entry.getKey();
            Trophy trophy = entry.getValue();
            player.getJest().addCardToCards(trophy);
            System.out.println(player.getPlayerName() + " won trophy: " +
                    trophy.getRank() + trophy.getSuit() +
                    " (" + trophy.getObtainConditions() + ")");
        }
    }
}