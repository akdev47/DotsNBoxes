package BoardDrawingGame.Entities;

import BoardDrawingGame.logic.Utility;
import BoardDrawingGame.view.Line;
import javafx.scene.paint.Color;

/**
 * The Player class represents a player in a Dots and Boxes game.
 * It stores information about the player's color, the game board, and the current move made
 * by the player.
 *
 * @author Ali Kaan Uysal
 * @author Bas Van Dijk
 */
public class Player {

    Color playerColor;
    Utility utility;
    Board board;

    Line currentMove;

    /**
     * Constructs a new Player with the specified game board, utility, and player color.
     *
     * @param board       The game board associated with the player.
     * @param utility     The utility class for game-related operations.
     * @param playerColor The color representing the player.
     */
    public Player(Board board, Utility utility,Color playerColor) {
        this.board = board;
        this.utility = utility;
        this.playerColor = playerColor;
        currentMove = null;
    }

    /**
     * Constructs a new Player with the specified game board and utility.
     *
     * @param board       The game board associated with the player.
     * @param utility     The utility class for game-related operations.
     */
    public Player(Board board, Utility utility) {
        this.board = board;
        this.utility = utility;
        currentMove = null;
    }

    /**
     * Default constructor for the Player class.
     */
    public Player() {
        board = new Board(5);
        utility = new Utility(board);
    }

    /**
     * Gets the color representing the player.
     *
     * @return The player's color.
     */
    public Color getPlayerColor() {
        return playerColor;
    }


    /**
     * Gets the current move made by the player.
     *
     * @return The current move made by the player as a Line object.
     */
    public Line getNextMove() {
        return currentMove;

    }
    /**
     * Sets the move made by the player.
     *
     * @param line The line representing the player's move.
     */
    public void setPlayerMove(Line line) {
        this.currentMove = line;
    }

    /**
     * Gets the board of player.
     *
     * @return The board of the player.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets the utility of player.
     *
     * @return The utility of the player.
     */
    public Utility getUtility() {
        return utility;
    }

}
