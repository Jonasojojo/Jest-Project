package jestgame.game;

import jestgame.gamemodes.GameMode;
import jestgame.model.Player;

import java.util.List;
import java.util.Scanner;


public class OfferManager {
    private final List<Player> players;
    private GameMode gameMode = null;

    public OfferManager(List<Player> players, GameMode gameMode) {
        this.players = players;
        this.gameMode = gameMode;
    }

    public void collectOffers() {
        for (Player p : players) {
            if (p.getHand().getCardList().size() < 2) {
                continue;
            }
            if (gameMode != null) {
                gameMode.onOffer(p);
            }

        }
    }



}
