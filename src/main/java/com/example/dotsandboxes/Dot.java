package com.example.dotsandboxes;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static com.example.dotsandboxes.DotsAndBoxes.GRID_SIZE;

public class Dot extends Circle {
    private int row;
    private int col;

    Dot connectedUpDot = null;
    Dot connectedDownDot = null;

    Dot connectedRightDot = null;

    Dot connectedLeftDot = null;

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

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getIndex() {
        return row * GRID_SIZE + col;
    }

    public static Dot getDotFromCircle(Circle circle) {
        if (circle instanceof Dot) {
            return (Dot) circle;
        } else {
            return null;
        }
    }

    public void setConnectedUpDot(Dot nextDot) {
        connectedUpDot = nextDot;
    }

    public void setConnectedDownDot(Dot nextDot) {
        connectedDownDot = nextDot;
    }

    public void setConnectedRightDot(Dot nextDot) {
        connectedRightDot = nextDot;
    }

    public void setConnectedLeftDot(Dot nextDot) {
        connectedLeftDot = nextDot;
    }

    public Dot getConnectedUpDot() {
        return connectedUpDot;
    }

    public Dot getConnectedDownDot() {
        return connectedDownDot;
    }

    public Dot getConnectedRightDot() {
        return connectedRightDot;
    }

    public Dot getConnectedLeftDot() {
        return connectedLeftDot;
    }

    public boolean isSameDot(Dot dotx) {

        return (this.row== dotx.getRow() && this.col == dotx.getCol());

    }

    public boolean isDotFullyConnected() {

        return this.getConnectedUpDot() != null && this.getConnectedDownDot() != null
                && this.getConnectedRightDot() != null && this.getConnectedLeftDot() != null;

    }

    public void printDotConnections(Dot dot1, Dot dot2) {
        if (dot1.getConnectedUpDot() != null) {
        System.out.println("First dot is connected to up");
        }
        if(dot1.getConnectedDownDot() != null) {
            System.out.println("First dot is connected to down");
        }
        if(dot1.getConnectedRightDot()!=null) {
            System.out.println("First dot is connected to right");
        }
        if(dot1.getConnectedLeftDot()!=null) {
            System.out.println("First dot is connected to left");
        }

        if (dot2.getConnectedUpDot() != null) {
            System.out.println("Second dot is connected to up");
        }
        if(dot2.getConnectedDownDot() != null) {
            System.out.println("Second dot is connected to down");
        }
        if(dot2.getConnectedRightDot()!=null) {
            System.out.println("Second dot is connected to right");
        }
        if(dot2.getConnectedLeftDot()!=null) {
            System.out.println("Second dot is connected to left");
        }




}





}