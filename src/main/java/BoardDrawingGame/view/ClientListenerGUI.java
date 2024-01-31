package BoardDrawingGame.view;

public interface ClientListenerGUI {


    /**
     * Handles the event when a new game is started.
     *
     * @param player1Name The name of the first player.
     * @param player2Name The name of the second player.
     */
    void handleNewGame(String player1Name, String player2Name) ;

    /**
     * Handles the event when a move is made in the game.
     *
     * @param move Information about the move made.
     */
    void handleMove(String move);

    /**
     * Handles the event to display informational text in the GUI.
     */
    void handleInfoText();

    /**
     * Handles the event when the game is over.
     *
     * @param reason The reason for the game's end.
     * @param winner The name of the winner, if applicable.
     */
    void handleGameOver(String reason, String winner);

    /**
     * Initiates the process for the AI (Artificial Intelligence) to make a move in the game.
     * This method is called when the client needs to request a move from the AI for the server.
     */
    void callAIMoveForServer();
}
