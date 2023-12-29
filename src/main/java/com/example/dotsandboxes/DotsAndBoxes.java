package com.example.dotsandboxes;

import java.util.Objects;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DotsAndBoxes extends Application {


    private Pane pane;
    private Dot startDot;

    private Game game;

    private Text infoText;
    private Text player1ScoreText;
    private Text player2ScoreText;

    public static final int GRID_SIZE = 6; // Adjust grid size as needed
    private static final double DOT_RADIUS = 7.0;

    @Override
    public void start(Stage primaryStage) {
        game = new Game();
        pane = new Pane();
        drawDots(pane);

        infoText = new Text("Player 1 turn");
        infoText.setStyle("-fx-font-size: 14;");


        player1ScoreText = new Text("Player 1 score: 0");
        infoText.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        player2ScoreText = new Text("Player 2 score: 0");
        infoText.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");


        Group group = new Group(pane,infoText, player1ScoreText,player2ScoreText);
        Scene scene = new Scene(group, 500, 500);
        infoText.setLayoutX(50);
        infoText.setLayoutY(480);

        player1ScoreText.setLayoutX(220);
        player1ScoreText.setLayoutY(480);
        player2ScoreText.setLayoutX(340);
        player2ScoreText.setLayoutY(480);

        pane.setMinSize(500, 500);
        pane.setMaxSize(500, 500);

        primaryStage.setTitle("Dots and Boxes");
        primaryStage.setScene(scene);

        primaryStage.setResizable(false);

        pane.setOnMouseClicked(this::handleMouseClick);

        primaryStage.show();
    }

    private void handleMouseClick(MouseEvent event) {

        Object source = event.getSource();

        if (source instanceof Circle) {
            Circle clickedCircle = (Circle) source;
            Dot clickedDot = Dot.getDotFromCircle(clickedCircle);


            if (startDot == null) {

                startDot = clickedDot;

                startDot.setFill(game.getCurrentPlayerColor());
            } else if (areAdjacentDots( startDot, clickedDot)) {
                drawLineBetweenDots( startDot, clickedDot);
                startDot.setFill(Color.BLACK);
                setConnectedDots(startDot,clickedDot);
                if(!checkIfSquare(startDot,clickedDot)) {
                    game.switchTurn();
                    infoText.setText(game.toString());
                }
                else {
                    if(game.isGameTurnPlayer1()) {
                        game.incrementPlayer1Score();
                        player1ScoreText.setText("Player 1 score: " + game.getPlayer1().getPlayerScore());
                    }
                    else {
                        game.incrementPlayer2Score();
                        player2ScoreText.setText("Player 2 score: " + game.getPlayer2().getPlayerScore());
                    }

                }

                startDot = null;
                clickedDot = null;

            }
        }


    }

    private void drawLineBetweenDots(Dot startDot, Dot endDot) {
//        Line line = new Line(startDot.getCenterX(), startDot.getCenterY(), endDot.getCenterX(), endDot.getCenterY());
//        line.setStroke(Color.BLACK);

        Dot firstDot = getDotAt(startDot.getRow(), startDot.getCol());
        Dot lastDot = getDotAt(endDot.getRow(), endDot.getCol());

        ConnectLine line = new ConnectLine(startDot.getCenterX(), startDot.getCenterY(),
         endDot.getCenterX(), endDot.getCenterY(),game.getCurrentPlayerColor(),firstDot,lastDot);

        line.setStrokeWidth(4);

//        game.switchTurn();
        pane.getChildren().add(line);
    }

    private void setConnectedDots(Dot startDot, Dot endDot) {

        Dot firstDot = getDotAt(startDot.getRow(), startDot.getCol());
        Dot lastDot = getDotAt(endDot.getRow(), endDot.getCol());

        assert firstDot != null;
        assert lastDot != null;

        if(startDot.getCol()-endDot.getCol()==-1) {
            firstDot.setConnectedRightDot(lastDot);
            lastDot.setConnectedLeftDot(firstDot);
        }

        else if(startDot.getCol()-endDot.getCol()==1) {
            firstDot.setConnectedLeftDot(lastDot);
            lastDot.setConnectedRightDot(firstDot);
        }
        else if(startDot.getRow()-endDot.getRow()==-1) {
            firstDot.setConnectedDownDot(lastDot);
            lastDot.setConnectedUpDot(firstDot);
        }
        else if(startDot.getRow()-endDot.getRow()==1) {
            firstDot.setConnectedUpDot(lastDot);
            lastDot.setConnectedDownDot(firstDot);
        }


    }

    private Dot getDotAt(int row, int col) {
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

    private ConnectLine getLineBetweenDots(Dot startDot, Dot endDot) {
        for (Node node : pane.getChildren()) {
            if (node instanceof ConnectLine) {
                ConnectLine line = (ConnectLine) node;
                if ((line.getStartDot() == startDot && line.getEndDot() == endDot) ||
                        (line.getStartDot() == endDot && line.getEndDot()== startDot)) {
                    return line;
                }
            }
        }
        return null;
    }

    private boolean checkIfSquare(Dot startDot, Dot endDot) {
        Dot firstDot = getDotAt(startDot.getRow(), startDot.getCol());
        Dot lastDot = getDotAt(endDot.getRow(), endDot.getCol());
        ConnectLine connectedLine = getLineBetweenDots(firstDot,lastDot);


        assert firstDot != null;
        if (firstDot.getRow() == 0 && Objects.requireNonNull(lastDot).getRow()==0&&lastDot.getCol() < GRID_SIZE) {
            assert connectedLine != null;
            return checkTopSide(firstDot, lastDot, startDot, endDot, connectedLine);
        } else if (firstDot.getCol()==0 && firstDot.getRow()< GRID_SIZE && Objects.requireNonNull(
                lastDot).getCol()==0) {
            assert connectedLine != null;
            return checkLeftSide(firstDot, lastDot, startDot, endDot, connectedLine);
        }
        else if (firstDot.getRow()==GRID_SIZE-1 && firstDot.getCol()< GRID_SIZE && Objects.requireNonNull(
                lastDot).getRow()==GRID_SIZE-1) {
            assert connectedLine != null;
            return checkBottomSide(firstDot, lastDot, startDot, endDot, connectedLine);

        }
        else if (firstDot.getCol()==GRID_SIZE-1 && firstDot.getRow()< GRID_SIZE && Objects.requireNonNull(
                lastDot).getCol()==GRID_SIZE-1) {
            assert connectedLine != null;
            return checkRightSide(firstDot, lastDot, startDot, endDot, connectedLine);
        } else {
            assert lastDot != null;
            if (firstDot.getRow()==lastDot.getRow() && Math.abs(firstDot.getCol()-lastDot.getCol())==1) {
                assert connectedLine != null;
                boolean a = checkTopSide(firstDot, lastDot, startDot, endDot, connectedLine);
                boolean b = checkBottomSide(firstDot,lastDot,startDot,endDot,connectedLine);

                if(a&b) {
                    if(game.isGameTurnPlayer1()) {
                        game.incrementPlayer1Score();
                    }
                    else {
                        game.incrementPlayer2Score();
                    }
                }
                return a | b;
            }
            else if (firstDot.getCol()==lastDot.getCol() && Math.abs(firstDot.getRow()-lastDot.getRow())==1) {
                assert connectedLine != null;
                boolean a = checkRightSide(firstDot, lastDot, startDot, endDot, connectedLine);
                boolean b = checkLeftSide(firstDot,lastDot,startDot,endDot,connectedLine);



                if(a&&b) {
                    if(game.isGameTurnPlayer1()) {
                        game.incrementPlayer1Score();
                    }
                    else {
                        game.incrementPlayer2Score();
                    }
                }
                return a | b;
            }
            else {
                return false;
            }
        }


    }

    public boolean checkTopSide(Dot firstDot, Dot lastDot, Dot startDot, Dot endDot, ConnectLine connectedLine) {

        Color firstLineColor = connectedLine.getLineColor();

        if(lastDot.getCol()<firstDot.getCol()) {
            Dot temp = firstDot;
            firstDot = lastDot;
            lastDot = temp;

            startDot = firstDot;
            endDot = lastDot;
        }
        System.out.println("Ok1");
        if (firstDot.getConnectedRightDot() == lastDot &&  lastDot.getConnectedLeftDot() == firstDot) {
            System.out.println("Ok2");
            firstDot = getDotAt(startDot.getRow(), startDot.getCol() + 1);
            lastDot = getDotAt(endDot.getRow()+1, endDot.getCol());
            ConnectLine nextLine1 = getLineBetweenDots(firstDot,lastDot);

            if (firstDot.getConnectedDownDot() == lastDot && lastDot.getConnectedUpDot() == firstDot) {
                System.out.println("Ok3");
                firstDot = getDotAt(startDot.getRow()+1, startDot.getCol()+1);
                lastDot = getDotAt(endDot.getRow()+1, endDot.getCol()-1);
                ConnectLine nextLine2 = getLineBetweenDots(firstDot,lastDot);

                if (firstDot.getConnectedLeftDot() == lastDot && lastDot.getConnectedRightDot()==firstDot) {
                    System.out.println("Ok4");
                    firstDot = getDotAt(startDot.getRow() + 1, startDot.getCol());
                    lastDot = getDotAt(endDot.getRow() , endDot.getCol() - 1);
                    ConnectLine nextLine3 = getLineBetweenDots(firstDot,lastDot);

                    if (firstDot != null && lastDot != null &&
                            firstDot.getConnectedUpDot() == lastDot && lastDot.getConnectedDownDot()==firstDot) {
                        System.out.println("SQUARE TOP SIDE");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkLeftSide(Dot firstDot, Dot lastDot, Dot startDot, Dot endDot,ConnectLine connectedLine) {

        Color firstLineColor = connectedLine.getLineColor();

        if(lastDot.getRow()<firstDot.getRow()) {
            Dot temp = firstDot;
            firstDot = lastDot;
            lastDot = temp;

            startDot = firstDot;
            endDot = lastDot;
        }
        System.out.println("Ok1");
        if (firstDot.getConnectedDownDot() == lastDot &&  lastDot.getConnectedUpDot() == firstDot) {
            System.out.println("Ok2");
            firstDot = getDotAt(startDot.getRow()+1, startDot.getCol());
            lastDot = getDotAt(endDot.getRow(), endDot.getCol()+1);
            ConnectLine nextLine1 = getLineBetweenDots(firstDot,lastDot);

            assert firstDot != null;
            if (firstDot.getConnectedRightDot() == lastDot) {
                assert lastDot != null;
                if (lastDot.getConnectedLeftDot() == firstDot) {
                        System.out.println("Ok3");
                        firstDot = getDotAt(startDot.getRow() + 1, startDot.getCol() + 1);
                        lastDot = getDotAt(endDot.getRow() - 1, endDot.getCol() + 1);
                        ConnectLine nextLine2 = getLineBetweenDots(firstDot, lastDot);

                        assert firstDot != null;
                        if (firstDot.getConnectedUpDot() == lastDot) {
                            assert lastDot != null;
                            if (lastDot.getConnectedDownDot() == firstDot) {

                                    System.out.println("Ok4");
                                    firstDot = getDotAt(startDot.getRow(), startDot.getCol() + 1);
                                    lastDot = getDotAt(endDot.getRow() - 1, endDot.getCol());
                                    ConnectLine nextLine3 = getLineBetweenDots(firstDot, lastDot);

                                    if (firstDot != null && lastDot != null && firstDot.getConnectedLeftDot() == lastDot && lastDot.getConnectedRightDot() == firstDot) {
                                        System.out.println("SQUARE LEFT SIDE");
                                        return true;
                                    }

                            }
                        }

                }
            }
        }
        return false;
    }

    public boolean checkBottomSide(Dot firstDot, Dot lastDot, Dot startDot, Dot endDot, ConnectLine connectedLine) {

        Color firstLineColor = connectedLine.getLineColor();

        if(lastDot.getCol()<firstDot.getCol()) {
            Dot temp = firstDot;
            firstDot = lastDot;
            lastDot = temp;

            startDot = firstDot;
            endDot = lastDot;
        }
        System.out.println("Ok1");
        if (firstDot.getConnectedRightDot() == lastDot &&  lastDot.getConnectedLeftDot() == firstDot) {
            System.out.println("Ok2");
            firstDot = getDotAt(startDot.getRow(), startDot.getCol()+1);
            lastDot = getDotAt(endDot.getRow()-1, endDot.getCol());
            ConnectLine nextLine1 = getLineBetweenDots(firstDot,lastDot);

            assert firstDot != null;
            if (firstDot.getConnectedUpDot() == lastDot) {
                assert lastDot != null;
                if (lastDot.getConnectedDownDot() == firstDot) {
                        System.out.println("Ok3");
                        firstDot = getDotAt(startDot.getRow() - 1, startDot.getCol() + 1);
                        lastDot = getDotAt(endDot.getRow() - 1, endDot.getCol() - 1);
                        ConnectLine nextLine2 = getLineBetweenDots(firstDot, lastDot);

                        assert firstDot != null;
                        if (firstDot.getConnectedLeftDot() == lastDot) {
                            assert lastDot != null;
                            if (lastDot.getConnectedRightDot() == firstDot) {
                                    System.out.println("Ok4");
                                    firstDot = getDotAt(startDot.getRow() - 1, startDot.getCol());
                                    lastDot = getDotAt(endDot.getRow(), endDot.getCol() - 1);
                                    ConnectLine nextLine3 = getLineBetweenDots(firstDot, lastDot);

                                    if (firstDot != null && lastDot != null && firstDot.getConnectedDownDot() == lastDot && lastDot.getConnectedUpDot() == firstDot) {
                                        System.out.println("SQUARE BOTTOM SIDE");
                                        return true;
                                    }

                            }
                        }

                }
            }
        }
        return false;
    }

    public boolean checkRightSide(Dot firstDot, Dot lastDot, Dot startDot, Dot endDot, ConnectLine connectedLine) {

        Color firstLineColor = connectedLine.getLineColor();

        if(lastDot.getRow()<firstDot.getRow()) {
            Dot temp = firstDot;
            firstDot = lastDot;
            lastDot = temp;

            startDot = firstDot;
            endDot = lastDot;
        }
        System.out.println("Ok1");
        if (firstDot.getConnectedDownDot() == lastDot &&  lastDot.getConnectedUpDot() == firstDot) {
            System.out.println("Ok2");
            firstDot = getDotAt(startDot.getRow()+1, startDot.getCol());
            lastDot = getDotAt(endDot.getRow(), endDot.getCol()-1);
            ConnectLine nextLine1 = getLineBetweenDots(firstDot,lastDot);

            assert firstDot != null;
            if (firstDot.getConnectedLeftDot() == lastDot) {
                assert lastDot != null;
                if (lastDot.getConnectedRightDot() == firstDot) {

                        System.out.println("Ok3");
                        firstDot = getDotAt(startDot.getRow() + 1, startDot.getCol() - 1);
                        lastDot = getDotAt(endDot.getRow() - 1, endDot.getCol() - 1);
                        ConnectLine nextLine2 = getLineBetweenDots(firstDot, lastDot);

                        assert firstDot != null;
                        if (firstDot.getConnectedUpDot() == lastDot) {
                            assert lastDot != null;
                            if (lastDot.getConnectedDownDot() == firstDot) {

                                    System.out.println("Ok4");
                                    firstDot = getDotAt(startDot.getRow(), startDot.getCol() - 1);
                                    lastDot = getDotAt(endDot.getRow() - 1, endDot.getCol());
                                    ConnectLine nextLine3 = getLineBetweenDots(firstDot, lastDot);

                                    if (firstDot != null && lastDot != null && firstDot.getConnectedRightDot() == lastDot && lastDot.getConnectedLeftDot() == firstDot) {
                                        System.out.println("SQUARE RIGHT SIDE");
                                        return true;
                                    }

                            }
                        }

                }
            }
        }
        return false;
    }

    private boolean areAdjacentDots(Dot startDot, Dot endDot) {
        int row1 = startDot.getRow();
        int col1 = startDot.getCol();
        int row2 = endDot.getRow();
        int col2 = endDot.getCol();

        return (Math.abs(row1 - row2) == 1 && col1 == col2) || (Math.abs(col1 - col2) == 1 && row1 == row2);
    }

    private void drawDots(Pane pane) {
        double spacing = 500.0 / GRID_SIZE;

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                double x = col * spacing + DOT_RADIUS;
                double y = row * spacing + DOT_RADIUS;

                Dot dot = new Dot(x, y, DOT_RADIUS, row, col);

                dot.setOnMouseEntered(e -> {
                    dot.getScene().setCursor(Cursor.HAND);
                });

                dot.setOnMouseExited(e -> {
                    dot.getScene().setCursor(Cursor.DEFAULT);
                });


                dot.setOnMouseClicked(this::handleMouseClick);

                pane.getChildren().add(dot);
            }
        }
    }

    private void fillInSquare(Dot dot1, Dot dot2, Dot dot3, Dot dot4) {
        Polygon square = new Polygon(
                dot1.getCenterX(), dot1.getCenterY(),
                dot2.getCenterX(), dot2.getCenterY(),
                dot3.getCenterX(), dot3.getCenterY(),
                dot4.getCenterX(), dot4.getCenterY()
        );

        square.setFill(game.getCurrentPlayerColor());
        pane.getChildren().add(square);
    }


    public static void main(String[] args) {
        launch(args);
    }
}

