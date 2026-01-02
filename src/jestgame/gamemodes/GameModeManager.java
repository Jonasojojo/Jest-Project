package jestgame.gamemodes;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import jestgame.gamemodes.BlindMode;

public class GameModeManager {
    private final List<GameMode> availableModes = new ArrayList<>();
    private GameMode selectedMode;

    public GameModeManager() {
        // Register available modes
        availableModes.add(new ClassicMode());
        availableModes.add(new BlindMode());
        availableModes.add(new ReversalMode());
    }

    public GameMode selectGameMode() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nAvailable game modes:");
        for (int i = 0; i < availableModes.size(); i++) {
            System.out.println((i + 1) + ". " + availableModes.get(i).getName());
        }

        int choice = -1;
        while (choice < 1 || choice > availableModes.size()) {
            System.out.print("Choose a game mode (1-" + availableModes.size() + "): ");
            if (sc.hasNextInt()) choice = sc.nextInt();
            else sc.next();
        }

        selectedMode = availableModes.get(choice - 1);
        System.out.println("You selected: " + selectedMode.getName());
        return selectedMode;
    }

    public GameMode getSelectedMode() {
        return selectedMode;
    }
}
