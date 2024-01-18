package BoardDrawingGame.src;


import com.example.dotsandboxes.AIPlayer;
import com.example.dotsandboxes.ConnectLine;
import com.example.dotsandboxes.Dot;
import com.example.dotsandboxes.Utility;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class DotsAndBoxesGUI extends Application implements MoveMadeListener {





    private Pane pane;
    private Dot startDot = null;

    private BoardDrawingGame.src.Game game;



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



    private boolean isPlayerVsPlayer = true;
    private boolean isAIVsAI = false;

    private Group startGroup;
    private Group gameGroup;
    private Group gameOverGroup;


    public static final int GRID_SIZE = 6;
    private static final double DOT_RADIUS = 7.0;

    @Override
    public void start(Stage gameScreen) throws FileNotFoundException {
        this.gameScreen = gameScreen;
        game = new Game();
        game.setMoveMadeListener(this);
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


        //        Image startBackgroundImage = new Image(getClass().getResourceAsStream("startBackground.jpg"));
        //        ImageView backgroundImageView = new ImageView(startBackgroundImage);



        ImageView imageView = new ImageView();
        try{
            InputStream stream = new FileInputStream("startBackground.jpg");
            Image image = new Image(stream);

            imageView.setImage(image);

            imageView.setX(10);
            imageView.setY(10);
            imageView.setFitWidth(gameScreen.getWidth());
            imageView.setPreserveRatio(true);

        } catch (FileNotFoundException e){
            e.printStackTrace();

        } catch (IOException e){

        }

        //        backgroundImageView.setFitWidth(gameScreen.getWidth());
        //        backgroundImageView.setFitHeight(gameScreen.getHeight());

        startButton = new Button("Start Game");
        startButton.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        startButton.setOnAction(e -> startGame());
        startButton.setLayoutX(210);
        startButton.setLayoutY(250);

        ChoiceBox<String> gameModeChoiceBox = new ChoiceBox<>();
        gameModeChoiceBox.getItems().addAll("Player vs Player", "Player vs AI", "AI vs AI");
        gameModeChoiceBox.setValue("Player vs Player");
        gameModeChoiceBox.setLayoutX(190);
        gameModeChoiceBox.setLayoutY(200);

        gameModeChoiceBox.setOnAction(event -> {
            String selectedMode = gameModeChoiceBox.getValue();
            isPlayerVsPlayer = selectedMode.equals("Player vs Player");
            isAIVsAI = selectedMode.equals("AI vs AI");

            if(isAIVsAI) {
                game.setGameMode("AIvsAI");
                isPlayerVsPlayer = false;
            }
            else if(isPlayerVsPlayer) {
                game.setGameMode("PlayerVSPlayer");
                isAIVsAI = false;
            }
            else  {
                game.setGameMode("PlayerVSAI");
                isAIVsAI = false;

            }
            System.out.println("Game mode: " + selectedMode);
            System.out.println(isAIVsAI);
            System.out.println(isPlayerVsPlayer);
        });

        startGroup = new Group(startButton,imageView, gameModeChoiceBox);







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

    /**
     * Handles the mouse click event.
     *
     * @param event The MouseEvent representing the mouse click event.
     */
    private void handleMouseClick(MouseEvent event)  {

        if(!game.getGameMode().equals("AIvsAI")) {

            Object source = event.getSource();

            if (source instanceof Circle ) {
                Circle clickedCircle = (Circle) source;
                Dot clickedDot = Dot.getDotFromCircle(clickedCircle);


                if (startDot == null) {

                    startDot = clickedDot;

                    if (startDot.isDotFullyConnected()) {
                        startDot = null;
                    } else {
                        startDot.setFill(game.getCurrentPlayer().getPlayerColor());
                    }


                } else if (game.getUtility().areAdjacentDots(startDot, clickedDot)) {

                    if (getLineBetweenDots(startDot, clickedDot) == null) {
                        game.getCurrentPlayer().setPlayerMove(null);
                        drawLineBetweenDots(startDot, clickedDot);
                        startDot.setFill(Color.BLACK);



                        Line playerLine = new Line();
                        if(startDot.getRow()==clickedDot.getRow()) {
                            playerLine.setHorizontalOrVertical("H");
                        } else {
                            playerLine.setHorizontalOrVertical("V");
                        }

                        if(playerLine.getHorizontalOrVertical().equals("H")) {
                            playerLine.setHorizontalIndex(startDot.getRow());
                            if(startDot.getCol()>clickedDot.getCol()) {
                                playerLine.setVerticalIndex(clickedDot.getCol());
                            }
                            else {
                                playerLine.setVerticalIndex(startDot.getCol());
                            }
                        }
                        else if(playerLine.getHorizontalOrVertical().equals("V")) {
                            playerLine.setVerticalIndex(startDot.getCol());
                            if(startDot.getRow()>clickedDot.getRow()) {
                                playerLine.setHorizontalIndex(clickedDot.getRow());
                            }
                            else {
                                playerLine.setHorizontalIndex(startDot.getRow());
                            }
                        }

                        game.getCurrentPlayer().setPlayerMove(playerLine);
                        game.processMove();
                        startDot = null;



                    }

                }


            }


        }

    }


    public void startAIVsAIGame() {

    }











    public boolean isGameOver() {
//        if(game.isGameOver()) {
//            if(isAIVsAI) {
//
//
//            }
//            else if(!isPlayerVsPlayer) {
//
//            }
//
//            int player1S = game.getPlayer1().getPlayerScore();
//            int player2S = game.getPlayer2().getPlayerScore();
//            String winnerPlayer;
//
//            if(player1S>player2S) {
//                winnerPlayer = game.getPlayer1().getName();
//            }
//            else {
//                winnerPlayer = game.getPlayer2().getName();
//
//            }
//            gameOverText.setText("Game Over! Player " + winnerPlayer+  " won. Final Score: " + player1S+"-"+player2S);
//            gameScreen.setScene(gameOverScene);
//            return true;
//        }
//        return false;
        return false;
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



    public static void main(String[] args) {
        launch(args);
    }

    public void resetGame() {
        gameScreen.setScene(startScene);
        pane.getChildren().clear();

        startDot = null;
        player1ScoreText.setText("Player 1 score: 0");
        player2ScoreText.setText("Player 2 score: 0");
        infoText.setText("Player 1 turn");
//        game.resetGame();



        drawDots(pane);

    }

    public void startGame() {
        gameScreen.setScene(gameScene);

       game.startGame();

    }


//    @Override
//    public void onInfoTextChanged(String newText) {
//        infoText.setText(newText);
//    }
//
//    @Override
//    public void onPlayer1ScoreUpdated(int newScore) {
////        player1ScoreText.setText("Player 1 score: " + game.getPlayer1().getPlayerScore());
//        isGameOver();
//    }
//
//    @Override
//    public void onPlayer2ScoreUpdated(int newScore) {
////        player2ScoreText.setText("Player 2 score: " + game.getPlayer2().getPlayerScore());
//        isGameOver();
//    }

    @Override
    public void onMoveMade(SquaresToDrawForUI move) {

        if(game.isPlayer1Turn()) {
            infoText.setText("Player 1 is playing");

        }
        else {
            infoText.setText("Player 2 is playing");
        }

        player1ScoreText.setText("Player 1 Score: " + game.getScorePlayer1());
        player2ScoreText.setText("Player 2 Score: " + game.getScorePlayer2());

        System.out.println("Move made");

            Line line = move.getLineToDraw();
            String horizontalOrVertical = line.getHorizontalOrVertical();
            int row = line.getHorizontalIndex();
            int col = line.getVerticalIndex();
            Dot startDot = null;
            Dot endDot = null;

            if(horizontalOrVertical.equals("H")) {
                 startDot = getDotAt(row,col);
                endDot = getDotAt(row,col+1);
            }
            else if(horizontalOrVertical.equals("V")) {
                startDot = getDotAt(row,col);
                endDot = getDotAt(row+1,col);
            }

            drawLineBetweenDots(startDot,endDot);

            if (move.isSquare()) {

                if(move.getFirstSquare()!=null) {
                    Line[] firstSquareLines = move.getFirstSquare();
                    System.out.println(firstSquareLines.length);
                    if(firstSquareLines[0]!=null) {
                        Dot[] dotsForSquare = findDotsForSquare(firstSquareLines);
                        fillInSquare(dotsForSquare[0],dotsForSquare[1], dotsForSquare[3], dotsForSquare[2]);
                    }
                }
                if(move.getSecondSquare()!=null) {
                    Line[] secondSquareLines = move.getSecondSquare();
                    if(secondSquareLines[0]!=null) {
                        Dot[] dotsForSquare = findDotsForSquare(secondSquareLines);
                        fillInSquare(dotsForSquare[0],dotsForSquare[1], dotsForSquare[3], dotsForSquare[2]);
                    }

                }
            }


    }


    private Dot[] findDotsForSquare(Line[] lines) {
        Dot[] dots = new Dot[4];

        for (int i = 0; i < lines.length; i++) {
            Line currentLine = lines[i];
            int row = currentLine.getHorizontalIndex();
            int col = currentLine.getVerticalIndex();

            if (currentLine.getHorizontalOrVertical().equals("H")) {
                dots[i] = getDotAt(row, col);
            } else if (currentLine.getHorizontalOrVertical().equals("V")) {
                dots[i] = getDotAt(row, col);
            }
        }

        // Sort the dots based on their coordinates
        Arrays.sort(dots, Comparator.comparing(Dot::getRow).thenComparing(Dot::getCol));

        dots[1] = getDotAt((Math.addExact(dots[1].getRow(),1)),Math.addExact(dots[1].getCol(),1));
        Arrays.sort(dots, Comparator.comparing(Dot::getRow).thenComparing(Dot::getCol));

        for(Dot i : dots) {
            System.out.print(" ("+i.getRow()+"," + i.getCol() +") ,");
        }
        System.out.println(" ");
        return dots;
    }
    public Dot getDotAt(int row, int col) {
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


    public void drawLineBetweenDots(Dot startDot, Dot endDot) {

        Dot firstDot = getDotAt(startDot.getRow(), startDot.getCol());
        Dot lastDot = getDotAt(endDot.getRow(), endDot.getCol());


        ConnectLine line = new ConnectLine(startDot.getCenterX(), startDot.getCenterY(),
                                           endDot.getCenterX(), endDot.getCenterY(),game.getCurrentPlayer().getPlayerColor(),firstDot,lastDot);


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

    public void fillInSquare(Dot dot1, Dot dot2, Dot dot3, Dot dot4) {

        Polygon square = new Polygon(
                dot1.getCenterX(), dot1.getCenterY(),
                dot2.getCenterX(), dot2.getCenterY(),
                dot3.getCenterX(), dot3.getCenterY(),
                dot4.getCenterX(), dot4.getCenterY()
        );

        // System.out.println("square filled");
        square.setFill(game.getCurrentPlayer().getPlayerColor());
        pane.getChildren().add(square);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(square.fillProperty(), Color.TRANSPARENT)),
                new KeyFrame(Duration.millis(2000), new KeyValue(square.fillProperty(), game.getCurrentPlayer().getPlayerColor()))
        );

        timeline.play();

        dot1.toFront();
        dot2.toFront();
        dot3.toFront();
        dot4.toFront();

    }

    public ConnectLine getLineBetweenDots( Dot startDot, Dot endDot) {
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

}

