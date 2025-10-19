package jestgame.gamemodes;
import jestgame.game.GameLogic;
import jestgame.model.*;

public interface GameMode {
    String getName();

    /** Called during setup or game start */
    default void onSetup(GameLogic game) {}

    /** Called during the offer phase */
    default void onOffer(Player player) {}

    /** Called during each turn if you want to change logic */
    default void onTurn(Player player) {}

    default boolean hideOffersFromOthers() {
        return false;
    }
}
