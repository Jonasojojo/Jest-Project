package jestgame.visitors;

import jestgame.model.Player;
import jestgame.model.Trophy;

import java.util.*;

public interface TrophyAwardVisitor {
    Player visit(Trophy trophy, List<Player> players);
}
