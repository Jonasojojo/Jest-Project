package jestgame.bots;

import jestgame.model.Player;
import jestgame.model.Card;

import java.util.List;

public interface BotStrategy {

    int chooseCardToOffer(Player self);

    Player chooseTarget(Player self, List<Player> eligibleTargets);

    boolean chooseFaceUpOrDown(Player self, Player target);
}