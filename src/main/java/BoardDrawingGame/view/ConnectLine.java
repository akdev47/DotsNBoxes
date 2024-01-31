package BoardDrawingGame.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Custom extension of JavaFX Line class representing a line connecting two dots on the game board,
 * for the GUI only.
 */
public class ConnectLine extends Line {
    private Dot startDot;
    private Dot endDot;

    /**
     * Constructs a ConnectLine with specified start and end coordinates, line color,
     * and connected dots.
     *
     * @param startX    The x-coordinate of the starting point.
     * @param startY    The y-coordinate of the starting point.
     * @param endX      The x-coordinate of the ending point.
     * @param endY      The y-coordinate of the ending point.
     * @param lineColor The color of the line.
     * @param startDot  The Dot representing the starting point of the line.
     * @param endDot    The Dot representing the ending point of the line.
     */
    public ConnectLine(double startX, double startY, double endX, double endY, Color lineColor, Dot startDot, Dot endDot) {
        super(startX, startY, endX, endY);
        this.startDot = startDot;
        this.endDot = endDot;
        setStroke(lineColor);
    }



    /**
     * Gets the Dot representing the starting point of the line.
     *
     * @return The Dot representing the starting point of the line.
     */
    public Dot getStartDot() {
        return startDot;
    }

    /**
     * Gets the Dot representing the ending point of the line.
     *
     * @return The Dot representing the ending point of the line.
     */
    public Dot getEndDot() {
        return endDot;
    }
}
