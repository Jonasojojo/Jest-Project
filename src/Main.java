import jestgame.bots.BotManager;
import jestgame.bots.BotStrategy;
import jestgame.expansion.StarExtension;
import jestgame.game.GameLogic;
import jestgame.gamemodes.GameMode;
import jestgame.gamemodes.GameModeManager;
import jestgame.model.GameConstants;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Welcome to Jest! ===");

        // You can set number of players here or in a constants class
        GameConstants.numberOfPlayers = 3; // Example

        BotManager botManager = new BotManager();
        Map<String, BotStrategy> botConfigs = botManager.configureBots();

        // New: select a game mode
        GameModeManager modeManager = new GameModeManager();
        GameMode selectedMode = modeManager.selectGameMode();



        // Create and run the game
        GameLogic game = new GameLogic(selectedMode);
        Scanner sc = new Scanner(System.in);
        System.out.print("Enable Star Expansion? (y/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("y")) {
            game.enableExpansion(new StarExtension());
        }


        // Ask how many humans
        System.out.print("Number of human players (1-3): ");
        int humans = Integer.parseInt(sc.nextLine());

        game.setupGame(humans, botConfigs);
        game.playGame();

        System.out.println("=== Game Over ===");
    }
}
