package jestgame.game;

import jestgame.expansion.Extension;
import jestgame.model.*;
import jestgame.visitors.*;
import java.util.*;

public class TrophyManager {
    private final List<Trophy> trophies = new ArrayList<>();
    private final List<Extension> extensions;

    public TrophyManager(List<Extension> extensions) {
        this.extensions = extensions;
    }

    public void setupTrophies(Deck deck, int numberOfPlayers) {
        trophies.clear();

        if (numberOfPlayers == 3) {
            addTrophy(deck, "first", extensions);
            addTrophy(deck, "second", extensions);
        } else {
            addTrophy(deck, "first", extensions);
        }
    }

    private void addTrophy(Deck deck, String label, List<Extension> extension) {
        Trophy trophy = new Trophy(deck.drawCard(), extension);
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
        TrophyAwardVisitor awardVisitor = new DefaultTrophyAwardVisitor(extensions);

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
