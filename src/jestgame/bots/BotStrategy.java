package jestgame.bots;

import jestgame.model.Player;
import jestgame.model.Card;

import java.util.List;

public interface BotStrategy {

    /** Decide which card index to place face-up in own offer (0 or 1) */
    int chooseCardToOffer(Player self);

    /** Decide which player to target */
    Player chooseTarget(Player self, List<Player> eligibleTargets);

    /** Decide whether to take face-up (true) or face-down (false) card from target */
    boolean chooseFaceUpOrDown(Player self, Player target);
}