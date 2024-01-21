package BoardDrawingGame.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class Dot extends Circle {
    private final int row;
    private final int col;



    public Dot(double centerX, double centerY, double radius, int row, int col) {
        super(centerX, centerY, radius, Color.BLACK);
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }


    public static Dot getDotFromCircle(Circle circle) {
        if (circle instanceof Dot) {
            return (Dot) circle;
        } else {
            return null;
        }
    }




    public boolean isSameDot(Dot dotx) {

        return (this.row== dotx.getRow() && this.col == dotx.getCol());

    }



}