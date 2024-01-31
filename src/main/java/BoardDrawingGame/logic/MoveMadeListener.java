package BoardDrawingGame.logic;

import BoardDrawingGame.view.SquaresToDrawForUI;
/**
 * The  MoveMadeListener interface defines methods to be implemented by classes that listen for move events
 * and game over events in a Dots and Boxes game.
 *
 * Classes implementing this interface can be registered as listeners to be notified when a move is
 * made or when the game is over.
 *
 * @author Ali Kaan Uysal
 * @author Bas Van Dijk
 */
public interface MoveMadeListener {

    /**
     * Called when a move is made during the game. Provides information about the move
     * to be drawn on the UI.
     *
     * @param info Information about the move made.
     */
    void onMoveMade(SquaresToDrawForUI info);

    /**
     * Called when the game is over. Notifies listeners that the game has ended.
     */
    void gameOver();

}
