package jestgame.gamemodes;

import jestgame.model.Player;
import jestgame.model.Card;

public class ReversalMode implements GameMode {

    @Override
    public String getName() {
        return "Reversal Mode";
    }

    /** Used by TurnManager to decide order direction */
    public boolean reverseOrder() {
        return true;
    }
}
