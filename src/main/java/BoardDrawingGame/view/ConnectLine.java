package BoardDrawingGame.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class ConnectLine extends Line {
    private Dot startDot;
    private Dot endDot;

    public ConnectLine(double startX, double startY, double endX, double endY, Color lineColor, Dot startDot, Dot endDot) {
        super(startX, startY, endX, endY);
        this.startDot = startDot;
        this.endDot = endDot;
        setStroke(lineColor);
    }


    public Dot getStartDot() {
        return startDot;
    }

    public Dot getEndDot() {
        return endDot;
    }
}
