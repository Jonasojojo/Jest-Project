public class Main {
    public static void main(String[] args) {
        System.out.println("=== Welcome to Jest! ===");

        // You can set number of players here or in a constants class
        GameConstants.numberOfPlayers = 3; // Example

        // Create and run the game
        GameLogic game = new GameLogic();
        game.playGame();

        System.out.println("=== Game Over ===");
    }
}