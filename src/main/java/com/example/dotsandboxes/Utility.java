package com.example.dotsandboxes;

import java.util.Objects;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

public class Utility {



    public void copyDotsConnectionRelationship(Pane originalPane, Pane copyPane) {
        for (int row = 0; row < DotsAndBoxes.GRID_SIZE; row++) {
            for (int col = 0; col < DotsAndBoxes.GRID_SIZE; col++) {
                Dot currentDot = getDotAt(originalPane, row, col);

                // Check right
                if (currentDot.getConnectedRightDot() != null) {
                    Dot dotAtCopyPane = getDotAt(copyPane, row, col);
                    Dot nextDotAtCopy = getDotAt(copyPane, row, col + 1);
                    if (dotAtCopyPane != null && nextDotAtCopy != null) {
                        setConnectedDots(copyPane, dotAtCopyPane, nextDotAtCopy);
                    }
                }

                // Check left
                if (currentDot.getConnectedLeftDot() != null) {
                    Dot dotAtCopyPane = getDotAt(copyPane, row, col);
                    Dot nextDotAtCopy = getDotAt(copyPane, row, col - 1);
                    if (dotAtCopyPane != null && nextDotAtCopy != null) {
                        setConnectedDots(copyPane, dotAtCopyPane, nextDotAtCopy);
                    }
                }

                // Check up
                if (currentDot.getConnectedUpDot() != null) {
                    Dot dotAtCopyPane = getDotAt(copyPane, row, col);
                    Dot nextDotAtCopy = getDotAt(copyPane, row - 1, col);
                    if (dotAtCopyPane != null && nextDotAtCopy != null) {
                        setConnectedDots(copyPane, dotAtCopyPane, nextDotAtCopy);
                    }
                }

                // Check down
                if (currentDot.getConnectedDownDot() != null) {
                    Dot dotAtCopyPane = getDotAt(copyPane, row, col);
                    Dot nextDotAtCopy = getDotAt(copyPane, row + 1, col);
                    if (dotAtCopyPane != null && nextDotAtCopy != null) {
                        setConnectedDots(copyPane, dotAtCopyPane, nextDotAtCopy);
                    }
                }
            }
        }
    }


    public void setConnectedDots(Pane panex,Dot startDot, Dot endDot) {

        Dot firstDot = getDotAt(panex, startDot.getRow(), startDot.getCol());
        Dot lastDot = getDotAt(panex,endDot.getRow(), endDot.getCol());


        if(startDot.getCol()-endDot.getCol()==-1) {
            firstDot.setConnectedRightDot(lastDot);
            lastDot.setConnectedLeftDot(firstDot);

            startDot.setConnectedRightDot(endDot);
            endDot.setConnectedLeftDot(startDot);
        }

        else if(startDot.getCol()-endDot.getCol()==1) {
            firstDot.setConnectedLeftDot(lastDot);
            lastDot.setConnectedRightDot(firstDot);

            startDot.setConnectedLeftDot(endDot);
            endDot.setConnectedRightDot(firstDot);
        }
        else if(startDot.getRow()-endDot.getRow()==-1) {
            firstDot.setConnectedDownDot(lastDot);
            lastDot.setConnectedUpDot(firstDot);

            startDot.setConnectedDownDot(endDot);
            endDot.setConnectedUpDot(startDot);
        }
        else if(startDot.getRow()-endDot.getRow()==1) {
            firstDot.setConnectedUpDot(lastDot);
            lastDot.setConnectedDownDot(firstDot);

            startDot.setConnectedUpDot(endDot);
            endDot.setConnectedDownDot(startDot);
        }


    }

    public Dot getDotAt(Pane pane, int row, int col) {
        for (Node node : pane.getChildren()) {
            if (node instanceof Dot) {
                Dot dot = (Dot) node;
                if (dot.getRow() == row && dot.getCol() == col) {
                    return dot;
                }
            }
        }
        return null;
    }



    public void drawLineBetweenDots(Game game, Pane pane, Dot startDot, Dot endDot) {
        //        Line line = new Line(startDot.getCenterX(), startDot.getCenterY(), endDot.getCenterX(), endDot.getCenterY());
        //        line.setStroke(Color.BLACK);

        Dot firstDot = getDotAt(pane,startDot.getRow(), startDot.getCol());
        Dot lastDot = getDotAt(pane,endDot.getRow(), endDot.getCol());


        ConnectLine line = new ConnectLine(startDot.getCenterX(), startDot.getCenterY(),
                                           endDot.getCenterX(), endDot.getCenterY(),game.getCurrentPlayerColor(),firstDot,lastDot);

        //        System.out.println("Start Dot: (" + startDot.getCenterX() + ", " + startDot.getCenterY() + ")");
        //        System.out.println("End Dot: (" + endDot.getCenterX() + ", " + endDot.getCenterY() + ")");
        //        System.out.println("Line Start: (" + line.getStartX() + ", " + line.getStartY() + ")");
        //        System.out.println("Line End: (" + line.getEndX() + ", " + line.getEndY() + ")");

        line.setStrokeWidth(4);
        pane.getChildren().add(line);


        double duration = 500;

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(line.endXProperty(), startDot.getCenterX())),
                new KeyFrame(Duration.ZERO, new KeyValue(line.endYProperty(), startDot.getCenterY())),
                new KeyFrame(Duration.millis(duration), new KeyValue(line.endXProperty(), endDot.getCenterX())),
                new KeyFrame(Duration.millis(duration), new KeyValue(line.endYProperty(), endDot.getCenterY()))
        );

        timeline.play();

        startDot.toFront();
        endDot.toFront();

    }


    public Pane duplicatePane(Pane originalPane) {
        Pane newPane = new Pane();

        for (Node node : originalPane.getChildren()) {
            if (node instanceof Dot) {
                Dot originalDot = (Dot) node;
                Dot newDot = new Dot(originalDot.getCenterX(), originalDot.getCenterY(),
                                     originalDot.getRadius(), originalDot.getRow(), originalDot.getCol());



                newPane.getChildren().add(newDot);

            } else if (node instanceof ConnectLine) {
                ConnectLine originalLine = (ConnectLine) node;
                Dot originalStartDot = originalLine.getStartDot();
                Dot originalEndDot = originalLine.getEndDot();

                Dot newStartDot = getDotAt(originalPane, originalStartDot.getRow(), originalStartDot.getCol());
                Dot newEndDot = getDotAt(originalPane, originalEndDot.getRow(), originalEndDot.getCol());

                if (newStartDot != null && newEndDot != null) {
                    ConnectLine newLine = new ConnectLine(newStartDot.getCenterX(), newStartDot.getCenterY(),
                                                          newEndDot.getCenterX(), newEndDot.getCenterY(), originalLine.getLineColor(), newStartDot, newEndDot);
                    newPane.getChildren().add(newLine);
                }
            }
        }
        copyDotsConnectionRelationship(originalPane, newPane);

        return newPane;
    }


    public boolean areAdjacentDots(Dot startDot, Dot endDot) {
        int row1 = startDot.getRow();
        int col1 = startDot.getCol();
        int row2 = endDot.getRow();
        int col2 = endDot.getCol();

        return (Math.abs(row1 - row2) == 1 && col1 == col2) || (Math.abs(col1 - col2) == 1 && row1 == row2);
    }








    public ConnectLine getLineBetweenDots(Pane pane, Dot startDot, Dot endDot) {
        for (Node node : pane.getChildren()) {
            if (node instanceof ConnectLine) {
                ConnectLine line = (ConnectLine) node;
                Dot lineStartDot = line.getStartDot();
                Dot lineEndDot = line.getEndDot();


                if ((lineStartDot.isSameDot(startDot) && lineEndDot.isSameDot(endDot) ) ||
                        (lineStartDot.isSameDot(endDot) && lineEndDot.isSameDot(startDot))) {
                    //                    System.out.println("Found line between dots!");
                    return line;
                }
            }

        }
        //        System.out.println("No line found between dots.");
        return null;
    }

    public void fillInSquare(Game game, Pane pane, Dot dot1, Dot dot2, Dot dot3, Dot dot4) {

        Polygon square = new Polygon(
                dot1.getCenterX(), dot1.getCenterY(),
                dot2.getCenterX(), dot2.getCenterY(),
                dot3.getCenterX(), dot3.getCenterY(),
                dot4.getCenterX(), dot4.getCenterY()
        );

        // System.out.println("square filled");
        square.setFill(game.getCurrentPlayerColor());
        pane.getChildren().add(square);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(square.fillProperty(), Color.TRANSPARENT)),
                new KeyFrame(Duration.millis(2000), new KeyValue(square.fillProperty(), game.getCurrentPlayerColor()))
        );

        timeline.play();

        dot1.toFront();
        dot2.toFront();
        dot3.toFront();
        dot4.toFront();

    }

}
