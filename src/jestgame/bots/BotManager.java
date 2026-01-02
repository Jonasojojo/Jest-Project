package jestgame.bots;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class BotManager {

    private final Scanner sc = new Scanner(System.in);


    public Map<String, BotStrategy> configureBots() {
        Map<String, BotStrategy> botConfigs = new LinkedHashMap<>();

        System.out.print("Number of bots: ");
        int numBots = readInt(0, 3); // max 3 bots

        for (int i = 1; i <= numBots; i++) {
            System.out.println("Strategy for bot " + i);
            System.out.print("1 strategic \n 2 RiskyBot \n 3 CollectorBot (in construction) ");
            String strategyInput = sc.nextLine().trim();
            BotStrategy strategy = chooseStrategy(strategyInput);

            botConfigs.put("Bot " + i, strategy);
        }

        return botConfigs;
    }


    private BotStrategy chooseStrategy(String input) {
        // You can add more strategy implementations here later
        if (input.equalsIgnoreCase("1")) {
            return new MaxPoints();
        } else if (input.equalsIgnoreCase("2")) {
            return new RiskyBot();
        } else if (input.equalsIgnoreCase("3")) {
            System.out.println("CollectorBot is still in construction, defaulting to 'strategic'.");
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
            } catch (NumberFormatException ignored) {
            }
            System.out.print("Invalid input, enter a number between " + min + " and " + max + ": ");
        }
        return value;
    }
}
