package jestgame.bots;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
public class BotManager {

    private final Scanner sc = new Scanner(System.in);

    /**
     * Ask the user how many bots and what strategy each bot should use.
     * Returns a map of bot names to their chosen strategy.
     */
    public Map<String, BotStrategy> configureBots() {
        Map<String, BotStrategy> botConfigs = new LinkedHashMap<>();

        System.out.print("Number of bots: ");
        int numBots = readInt(0, 3); // max 3 bots, can be adjusted

        for (int i = 1; i <= numBots; i++) {
            System.out.print("Strategy for bot " + i + " (currently only 'strategic'): ");
            String strategyInput = sc.nextLine().trim();
            BotStrategy strategy = chooseStrategy(strategyInput);

            botConfigs.put("Bot " + i, strategy);
        }

        return botConfigs;
    }

    private BotStrategy chooseStrategy(String input) {
        // You can add more strategy implementations here later
        if (input.equalsIgnoreCase("strategic")) {
            return new MaxPoints();
        }

        System.out.println("Unknown strategy, defaulting to 'strategic'.");
        return new MaxPoints();
    }

    private int readInt(int min, int max) {
        int value;
        while (true) {
            try {
                value = Integer.parseInt(sc.nextLine());
                if (value >= min && value <= max) break;
            } catch (NumberFormatException ignored) {}
            System.out.print("Invalid input, enter a number between " + min + " and " + max + ": ");
        }
        return value;
    }
}
