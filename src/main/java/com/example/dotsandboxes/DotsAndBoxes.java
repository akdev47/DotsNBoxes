package com.example.dotsandboxes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

public class DotsAndBoxes extends Application implements AIPlayer.InfoTextListener, AIPlayer.ScoreUpdateListener {





    private Pane pane;
    private Dot startDot = null;

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

    private boolean isPlayerVsPlayer = true;
    private boolean isAIVsAI = false;


    private Utility utility;
    private Group startGroup;
    private Group gameGroup;
    private Group gameOverGroup;

    private Player player1 = new AIPlayer(Color.RED, "1",game);;
    private Player player2 = new AIPlayer(Color.BLUE,"2",game);

    public static final int GRID_SIZE = 6;
    private static final double DOT_RADIUS = 7.0;

    @Override
    public void start(Stage gameScreen) throws FileNotFoundException {
        this.gameScreen = gameScreen;
        game = new Game();
        utility = new Utility();
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
            if(isAIVsAI && !isPlayerVsPlayer) {
                isPlayerVsPlayer = false;
                player1 = new AIPlayer(Color.RED, "1",game);
                player2 = new AIPlayer(Color.BLUE,"2",game);
            }

            else if(isPlayerVsPlayer) {
                player1 = new Player(Color.RED, "1");
                player2 = new Player(Color.BLUE,"2");
                isAIVsAI = false;
            }
            else if(!isPlayerVsPlayer) {
                player1 = new Player(Color.RED, "1");
                player2 = new AIPlayer(Color.BLUE,"2",game);
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

        if(!isAIVsAI) {
            boolean filledSquare = false;

            Object source = event.getSource();

            if (source instanceof Circle ) {
                Circle clickedCircle = (Circle) source;
                Dot clickedDot = Dot.getDotFromCircle(clickedCircle);


                if (startDot == null) {

                    startDot = clickedDot;

                    if (startDot.isDotFullyConnected()) {
                        startDot = null;
                    } else {
                        startDot.setFill(game.getCurrentPlayerColor());
                    }


                } else if (utility.areAdjacentDots(startDot, clickedDot)) {

                    if (utility.getLineBetweenDots(pane, startDot, clickedDot) == null) {
                        utility.drawLineBetweenDots(game,pane, startDot, clickedDot);
                        startDot.setFill(Color.BLACK);
                        utility.setConnectedDots(pane, startDot, clickedDot);
                        if (!game.checkIfSquareOrThreeSides(pane, startDot, clickedDot,4)) {
                            game.switchTurn();
                            infoText.setText(game.toString());
                        } else {
                            filledSquare = true;

                            if (game.isGameTurnPlayer1()) {
                                System.out.println("Player 1 is filling the square.");
                            }
                            if (isPlayerVsPlayer) {
                                if (game.isGameTurnPlayer1()) {
                                    game.incrementPlayer1Score();
                                    player1ScoreText.setText("Player 1 score: " + game.getPlayer1().getPlayerScore());
                                } else {
                                    game.incrementPlayer2Score();
                                    player2ScoreText.setText("Player 2 score: " + game.getPlayer2().getPlayerScore());
                                }
                            } else {
                                if (game.isGameTurnPlayer1()) {
                                    game.incrementPlayer1Score();
                                    player1ScoreText.setText("Player 1 score: " + game.getPlayer1().getPlayerScore());
                                } else {
                                    game.incrementPlayer2Score();
                                    player2ScoreText.setText("Player 2 score: " + game.getPlayer2().getPlayerScore());
                                }
                            }


                        }


                        startDot = null;
                    }

                    //                PauseTransition pause = new PauseTransition(Duration.millis(4000));
                    //                pause.setOnFinished(e -> makeAIMoveAndContinue());// 1000 milliseconds = 1 second
                    //                pause.play();
                    if (!filledSquare) {

                        if(!isPlayerVsPlayer) {
                            ((AIPlayer) player2).makeAIMoveAndContinue(false,pane);
                        }

                    }


                    //                if(!isPlayerVsPlayer) {
                    //                    if(makeAIMove()) {
                    //                        game.incrementPlayer2Score();
                    //                        player2ScoreText.setText("Player 2 score: " + game.getPlayer2().getPlayerScore());
                    //
                    //                    }
                    //                }


                }


            }

            isGameOver();

        }

    }


    public void startAIVsAIGame() {

        ((AIPlayer) player1).makeAIMoveAndContinue(true,pane);
    }











    public boolean isGameOver() {
        if(game.isGameOver()) {
            if(isAIVsAI) {
                ((AIPlayer) player1).setInfoTextListener(null);
                ((AIPlayer) player1).setScoreUpdateListener(null);

            }
            else if(!isPlayerVsPlayer) {
                ((AIPlayer) player2).setInfoTextListener(null);
                ((AIPlayer) player2).setScoreUpdateListener(null);
            }

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
            return true;
        }
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
    game.resetGame();



    drawDots(pane);

    }

    public void startGame() {
        gameScreen.setScene(gameScene);
        game.setPlayers(player1,player2);
        if(isAIVsAI) {
            ((AIPlayer) player1).setInfoTextListener(this);
            ((AIPlayer) player1).setScoreUpdateListener(this);

            ((AIPlayer) player2).setInfoTextListener(this);
            ((AIPlayer) player2).setScoreUpdateListener(this);
             startAIVsAIGame();
        }
       else if(!isPlayerVsPlayer) {
            ((AIPlayer) player2).setInfoTextListener(this);
            ((AIPlayer) player2).setScoreUpdateListener(this);
        }

    }


    @Override
    public void onInfoTextChanged(String newText) {
        infoText.setText(newText);
    }

    @Override
    public void onPlayer1ScoreUpdated(int newScore) {
        player1ScoreText.setText("Player 1 score: " + game.getPlayer1().getPlayerScore());
        isGameOver();
    }

    @Override
    public void onPlayer2ScoreUpdated(int newScore) {
        player2ScoreText.setText("Player 2 score: " + game.getPlayer2().getPlayerScore());
        isGameOver();
    }
}

