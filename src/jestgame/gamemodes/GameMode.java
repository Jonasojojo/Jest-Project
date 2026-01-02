package jestgame.gamemodes;
import jestgame.game.GameLogic;
import jestgame.model.*;

public interface GameMode {
    String getName();

    default void onSetup(GameLogic game) {}

    default void onOffer(Player player) {}

    default void onTurn(Player player) {}

    default boolean hideOffersFromOthers() {
        return false;
    }
}
