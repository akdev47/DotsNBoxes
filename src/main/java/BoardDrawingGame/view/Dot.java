package BoardDrawingGame.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


/**
 * Represents a dot on the game board.
 */
public class Dot extends Circle {
    private final int row;
    private final int col;



    /**
     * Constructs a Dot with specified center coordinates, radius, row, and column.
     *
     * @param centerX The x-coordinate of the center.
     * @param centerY The y-coordinate of the center.
     * @param radius  The radius of the dot.
     * @param row     The row index of the dot on the game board.
     * @param col     The column index of the dot on the game board.
     */
    public Dot(double centerX, double centerY, double radius, int row, int col) {
        super(centerX, centerY, radius, Color.BLACK);
        this.row = row;
        this.col = col;
    }

    /**
     * Gets the row index of the dot on the game board.
     *
     * @return The row index.
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column index of the dot on the game board.
     *
     * @return The column index.
     */
    public int getCol() {
        return col;
    }


    /**
     * Retrieves a Dot object from a Circle object if it is an instance of Dot.
     *
     * @param circle The Circle object to convert.
     * @return Dot object if the Circle is an instance of Dot, otherwise null.
     */
    public static Dot getDotFromCircle(Circle circle) {
        if (circle instanceof Dot) {
            return (Dot) circle;
        } else {
            return null;
        }
    }



    /**
     * Checks if the provided Dot object represents the same dot.
     *
     * @param dotx The Dot object to compare.
     * @return True if the dots are the same, false otherwise.
     */
    public boolean isSameDot(Dot dotx) {

        return (this.row== dotx.getRow() && this.col == dotx.getCol());

    }



}