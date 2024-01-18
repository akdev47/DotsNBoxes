package BoardDrawingGame.src;

import javafx.scene.paint.Color;

public class Player {

    Color playerColor;
    Utility utility;
    Board board;

    Line currentMove;

    public Player(Board board, Utility utility,Color playerColor) {
        this.board = board;
        this.utility = utility;
        this.playerColor = playerColor;
        currentMove = null;
    }

    public Player() { }

    public Color getPlayerColor() {
        return playerColor;
    }
    public Line getNextMove() {
        return currentMove;

    }

    public void setPlayerMove(Line line) {
        this.currentMove = line;
    }
}
