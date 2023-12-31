package com.example.dotsandboxes;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class DotsAndBoxes extends Application {


    private Pane pane;
    private Dot startDot;

    private Game game;

    private Button resetButton;
    private Button startButton;
    private HBox buttonBox;
    private Text infoText;
    private Text player1ScoreText;
    private Text player2ScoreText;

    private Text gameOverText;

    private Stage gameScreen;
    private Scene gameScene;
    private Scene gameOverScene;

    private Scene startScene;


    private Group startGroup;
    private Group gameGroup;
    private Group gameOverGroup;

    public static final int GRID_SIZE = 6;
    private static final double DOT_RADIUS = 7.0;

    @Override
    public void start(Stage gameScreen) {
        this.gameScreen = gameScreen;
        game = new Game();
        pane = new Pane();
        drawDots(pane);

        infoText = new Text("Player 1 turn");
        infoText.setStyle("-fx-font-size: 14;");



//        buttonBox = new HBox(resetButton);
//        buttonBox.setLayoutX(130);
//        buttonBox.setLayoutY(460);

        player1ScoreText = new Text("Player 1 score: 0");
        infoText.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        player2ScoreText = new Text("Player 2 score: 0");
        infoText.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");


        gameGroup = new Group(pane,infoText, player1ScoreText,player2ScoreText);


        Image startBackgroundImage = new Image(getClass().getResourceAsStream(""));
        ImageView backgroundImageView = new ImageView(startBackgroundImage);

        backgroundImageView.setFitWidth(gameScreen.getWidth());
        backgroundImageView.setFitHeight(gameScreen.getHeight());

        startButton = new Button("Start Game");
        startButton.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        startButton.setOnAction(e -> startGame());
        startButton.setLayoutX(210);
        startButton.setLayoutY(250);

        startGroup = new Group(startButton,backgroundImageView);







        gameOverText = new Text("Game Over!");
        gameOverText.setStyle("-fx-font-size: 20;");
        gameOverText.setLayoutX(80);
        gameOverText.setLayoutY(200);

        resetButton = new Button("Reset Game");
        resetButton.setStyle("-fx-font-size: 10; -fx-padding: 8;");
        resetButton.setOnAction(e -> resetGame());
        resetButton.setLayoutX(210);
        resetButton.setLayoutY(250);

        gameOverGroup = new Group(gameOverText,resetButton);

        startScene = new Scene(startGroup,500,500);
        gameOverScene = new Scene(gameOverGroup, 500, 500);


        gameScene = new Scene(gameGroup, 500, 500);

        infoText.setLayoutX(50);
        infoText.setLayoutY(480);

        player1ScoreText.setLayoutX(220);
        player1ScoreText.setLayoutY(480);
        player2ScoreText.setLayoutX(340);
        player2ScoreText.setLayoutY(480);

        pane.setMinSize(500, 500);
        pane.setMaxSize(500, 500);

        gameScreen.setTitle("Dots and Boxes");
        gameScreen.setScene(startScene);

        gameScreen.setResizable(false);

        pane.setOnMouseClicked(this::handleMouseClick);

        gameScreen.show();
    }

    private void handleMouseClick(MouseEvent event) {

        Object source = event.getSource();

        if (source instanceof Circle) {
            Circle clickedCircle = (Circle) source;
            Dot clickedDot = Dot.getDotFromCircle(clickedCircle);


            if (startDot == null) {

                startDot = clickedDot;

                if(startDot.isDotFullyConnected()) {
                    startDot = null;
                }
                else {
                    startDot.setFill(game.getCurrentPlayerColor());
                }


            } else if (areAdjacentDots( startDot, clickedDot)) {

                if(getLineBetweenDots(startDot,clickedDot)==null) {
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
                }


            }
        }

        if(game.isGameOver()) {
            int player1S = game.getPlayer1().getPlayerScore();
            int player2S = game.getPlayer2().getPlayerScore();
            String winnerPlayer;

            if(player1S>player2S) {
               winnerPlayer = game.getPlayer1().getName();
            }
            else {
                winnerPlayer = game.getPlayer2().getName();

            }
            gameOverText.setText("Game Over! Player " + winnerPlayer+  " won. Final Score: " + player1S+"-"+player2S);
            gameScreen.setScene(gameOverScene);
        }


    }

    private void drawLineBetweenDots(Dot startDot, Dot endDot) {
//        Line line = new Line(startDot.getCenterX(), startDot.getCenterY(), endDot.getCenterX(), endDot.getCenterY());
//        line.setStroke(Color.BLACK);

        Dot firstDot = getDotAt(startDot.getRow(), startDot.getCol());
        Dot lastDot = getDotAt(endDot.getRow(), endDot.getCol());


        ConnectLine line = new ConnectLine(startDot.getCenterX(), startDot.getCenterY(),
         endDot.getCenterX(), endDot.getCenterY(),game.getCurrentPlayerColor(),firstDot,lastDot);

        System.out.println("Start Dot: (" + startDot.getCenterX() + ", " + startDot.getCenterY() + ")");
        System.out.println("End Dot: (" + endDot.getCenterX() + ", " + endDot.getCenterY() + ")");
        System.out.println("Line Start: (" + line.getStartX() + ", " + line.getStartY() + ")");
        System.out.println("Line End: (" + line.getEndX() + ", " + line.getEndY() + ")");

        double startX = startDot.getCenterX();
        double startY = startDot.getCenterY();
        double endX = endDot.getCenterX();
        double endY = endDot.getCenterY();


        line.setStrokeWidth(4);



//        game.switchTurn();










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

    private void setConnectedDots(Dot startDot, Dot endDot) {

        Dot firstDot = getDotAt(startDot.getRow(), startDot.getCol());
        Dot lastDot = getDotAt(endDot.getRow(), endDot.getCol());


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
                Dot lineStartDot = line.getStartDot();
                Dot lineEndDot = line.getEndDot();


                if ((lineStartDot.isSameDot(startDot) && lineEndDot.isSameDot(endDot) ) ||
                        (lineStartDot.isSameDot(endDot) && lineEndDot.isSameDot(startDot))) {
                    System.out.println("Found line between dots!");
                    return line;
                }
            }
        }
        System.out.println("No line found between dots.");
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
                        fillInSquare(startDot,endDot,getDotAt(endDot.getRow()+1, endDot.getCol()),getDotAt(
                                startDot.getRow()+1, startDot.getCol()));
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
                                        fillInSquare(startDot,endDot,getDotAt(endDot.getRow(), endDot.getCol()+1),getDotAt(
                                                startDot.getRow(), startDot.getCol()+1));
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
                                        fillInSquare(startDot,endDot,getDotAt(endDot.getRow()-1, endDot.getCol()),getDotAt(
                                                startDot.getRow()-1, startDot.getCol()));
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
                                        fillInSquare(startDot,endDot,getDotAt(endDot.getRow(), endDot.getCol()-1),getDotAt(
                                                startDot.getRow(), startDot.getCol()-1));
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

        System.out.println("square filled");
        square.setFill(game.getCurrentPlayerColor());
        pane.getChildren().add(square);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(square.fillProperty(), Color.TRANSPARENT)),
                new KeyFrame(Duration.millis(500), new KeyValue(square.fillProperty(), game.getCurrentPlayerColor()))
        );

        timeline.play();

        dot1.toFront();
        dot2.toFront();
        dot3.toFront();
        dot4.toFront();

    }


    public static void main(String[] args) {
        launch(args);
    }

    public void resetGame() {

    gameScreen.setScene(gameScene);
    pane.getChildren().clear();

    startDot = null;
    player1ScoreText.setText("Player 1 score: 0");
    player2ScoreText.setText("Player 2 score: 0");
    infoText.setText("Player 1 turn");
    game.resetGame();

    drawDots(pane);

    }

    public void startGame() {
        gameScreen.setScene(gameScene);
    }


}

