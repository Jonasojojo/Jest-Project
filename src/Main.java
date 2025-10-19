import expansion.StarExtension;
import game.GameLogic;
import model.GameConstants;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Welcome to Jest! ===");

        // You can set number of players here or in a constants class
        GameConstants.numberOfPlayers = 3; // Example

        // Create and run the game
        GameLogic game = new GameLogic();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enable Star Expansion? (y/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("y")) {
            game.enableExpansion(new StarExtension());
        }
        game.playGame();

        System.out.println("=== Game Over ===");
    }
}
