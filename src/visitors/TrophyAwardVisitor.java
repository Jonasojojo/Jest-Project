package visitors;

import model.Player;
import model.Trophy;

import java.util.*;

public interface TrophyAwardVisitor {
    Player visit(Trophy trophy, List<Player> players);
}
