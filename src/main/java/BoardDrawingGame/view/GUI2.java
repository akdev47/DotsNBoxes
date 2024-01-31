package BoardDrawingGame.view;

import BoardDrawingGame.ServerClient.Client.Client;
import BoardDrawingGame.ServerClient.Client.ClientListener;
import BoardDrawingGame.logic.Game;
import BoardDrawingGame.logic.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The DotsAndBoxesGUI class represents the graphical user interface for the Dots and Boxes game.
 * It extends the JavaFX Application class and implements the MoveMadeListener interface.
 *
 * @author Ali Kaan Uysal
 * @author Bas Van Dijk
 */
public class GUI2 extends Application implements MoveMadeListener, ClientListenerGUI,ClientListener {

    private Pane pane;
    private Dot startDot = null;

    private Game game;


    private Button resetButton;
    private Button startButton;

    private Button goToServerScreenButton;

    private Button serverGameOverButton;

    private Button serverChangePlayerButton;

    private Button processServerCommandButton;

    private Button serverListButton;

    private Button serverQueueButton;

    private Button serverDisconnectButton;

    private Button toggleButton;
    private Text infoText = new Text("Player 1 turn");

    private Text player1ScoreText = new Text("Player 1 score: 0");

    private Text player2ScoreText = new Text("Player 1 score: 0");


    private Text gameOverText;

    private Text serverGameOverText;

    private Text serverInfoText;

    private Text serverStatusText;

    private TextField serverTextField;

    private Stage gameScreen;
    private Scene gameScene;

    private Scene serverSetUpScene;
    private Scene gameOverScene;

    private Scene serverGameOverScene;

    private Scene startScene;

    private final Color currentTheme = Color.BLACK;
    private final Color currentThemeOpposite = Color.WHITE;

    private boolean currentThemeDark = true;



    private boolean isPlayerVsPlayer = true;
    private boolean isAIVsAI = false;

    private boolean isPlayerVsAI = false;

    private boolean isServerGame = false;

    private boolean serverAddressRecieved = false;

    private boolean choosePlayerOption = false;

    private boolean userWillEnterUsername = false;


    private boolean isAIPlayerEasyMode = false;

    private ChoiceBox<String> serverPlayerChoices;


    private Group startGroup;

    private Group serverSetUpGroup;
    private Group gameGroup;
    private Group gameOverGroup;

    private Group serverGameOverGroup;

    private String serverAddress;
    private int serverPort;

    private Client client;

    private String serverPlayer1Name;

    private String serverPlayer2Name;

    private boolean isServerPlayerHumanPlayer = true;

    public static final int GRID_SIZE = 6;
    private static final double DOT_RADIUS = 7.0;

    /**
     * The entry point for the JavaFX application.
     *
     * @param gameScreen The primary stage for the application.
     * @throws FileNotFoundException If the specified file for the background image is not found.
     */
    @Override
    public void start(Stage gameScreen) throws FileNotFoundException {
        this.gameScreen = gameScreen;
        game = new Game();
        game.setMoveMadeListener(this);
        pane = new Pane();

        infoText = new Text("Player 1 turn");
        infoText.setFill(Color.WHITE);
        infoText.setStyle("-fx-font-size: 14;");

        player1ScoreText = new Text("Player 1 score: 0");
        player1ScoreText.setFill(Color.WHITE);
        infoText.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        player2ScoreText = new Text("Player 2 score: 0");
        player2ScoreText.setFill(Color.WHITE);
        infoText.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");


        gameGroup = new Group(pane,infoText, player1ScoreText,player2ScoreText);


        startButton = new Button("Start Game");
        startButton.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        startButton.setOnAction(e -> startGame());
        startButton.setLayoutX(210);
        startButton.setLayoutY(250);

        toggleButton = new Button("Switch Game Theme");
        toggleButton.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        toggleButton.setOnAction(e -> switchTheme());
        toggleButton.setLayoutX(180);
        toggleButton.setLayoutY(300);


        goToServerScreenButton = new Button("Start Server Connection");
        goToServerScreenButton.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        goToServerScreenButton.setOnAction(e -> switchToServerScreen());
        goToServerScreenButton.setLayoutX(180);
        goToServerScreenButton.setLayoutY(250);
        goToServerScreenButton.setVisible(false);


        serverInfoText = new Text("Enter Server address: ");
        serverInfoText.setFill(Color.BLACK);
        serverInfoText.setStyle("-fx-font-size: 14;");
        serverInfoText.setLayoutX(100);
        serverInfoText.setLayoutY(120);

        serverStatusText = new Text("STATUS");
        serverStatusText.setFill(Color.RED);
        serverStatusText.setStyle("-fx-font-size: 14;");
        serverStatusText.setLayoutX(100);
        serverStatusText.setLayoutY(140);

        serverTextField = new TextField();
        serverTextField.setMinWidth(100);
        serverTextField.setLayoutX(100);
        serverTextField.setLayoutY(160);


        //TODO: change the e ->
        processServerCommandButton = new Button("Send");
        processServerCommandButton.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        processServerCommandButton.setOnAction(e -> {

            try {
                processServerSend();
            } catch (IOException | InterruptedException | NumberFormatException ex) {
                serverPlayerChoices.setVisible(false);
                serverTextField.setVisible(true);
                serverTextField.setText("");
                serverStatusText.setFill(Color.RED);
                serverInfoText.setText("Enter Server address:");
                serverStatusText.setText("Error connecting to server, please try again!");

                serverAddressRecieved = false;

                choosePlayerOption = false;

                userWillEnterUsername = false;
            }

        });
        processServerCommandButton.setLayoutX(100);
        processServerCommandButton.setLayoutY(190);


        serverListButton = new Button("View All Connected Players");
        serverListButton.setStyle("-fx-font-size: 13; -fx-padding: 10;");

        serverListButton.setOnAction(e -> {
            client.sendList();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            serverTextField.setText(client.getCurrentList());
        });

        serverListButton.setLayoutX(100);
        serverListButton.setLayoutY(170);
        serverListButton.setVisible(false);

        serverChangePlayerButton = new Button("Change Player");
        serverChangePlayerButton.setStyle("-fx-font-size: 13; -fx-padding: 10;");

        serverChangePlayerButton.setOnAction(e -> {

            serverQueueButton.setVisible(true);
            if(isServerPlayerHumanPlayer) {
                serverStatusText.setText("Current player: Human");
                client.setHumanPlayer();
            }
            else {
                if(isAIPlayerEasyMode) {
                    client.setAIPlayer();
                    serverStatusText.setText("Current player: EASY AI");
                    client.getAIPlayer().setEasyMode(true);
                }
                else {
                    client.setAIPlayer();
                    serverStatusText.setText("Current player: AI");
                    client.getAIPlayer().setEasyMode(false);
                }


            }

        });

        serverChangePlayerButton.setLayoutX(100);
        serverChangePlayerButton.setLayoutY(380);
        serverChangePlayerButton.setVisible(false);



        serverQueueButton = new Button("Queue For A Game");
        serverQueueButton.setStyle("-fx-font-size: 13; -fx-padding: 10;");

        serverQueueButton.setOnAction(e -> {

            if(!client.getInQueue()) {
                serverStatusText.setText("Waiting for another player...");
                serverQueueButton.setText("Exit the queue");
                client.setInQueue(true);
                client.sendQueue();
            }
            else {
                serverStatusText.setText("Removed you from the queue.");
                serverQueueButton.setText("Queue For A Game");
                client.setInQueue(false);
                client.sendQueue();
            }

        });
        serverQueueButton.setLayoutX(280);
        serverQueueButton.setLayoutY(170);
        serverQueueButton.setVisible(false);





        serverDisconnectButton = new Button("Disconnect from server");
        serverDisconnectButton.setStyle("-fx-font-size: 13; -fx-padding: 10;");

        serverDisconnectButton.setOnAction(e -> {


            handleDisconnect();

        });
        serverDisconnectButton.setLayoutX(220);
        serverDisconnectButton.setLayoutY(380);
        serverDisconnectButton.setVisible(false);


        serverPlayerChoices = new ChoiceBox<>();
        serverPlayerChoices.getItems().addAll("Player","AIPlayer","EASY AIPlayer");
        serverPlayerChoices.setValue("Player");
        serverPlayerChoices.setLayoutX(100);
        serverPlayerChoices.setLayoutY(160);
        serverPlayerChoices.setVisible(false);


        serverPlayerChoices.setOnAction(event -> {
            String selectedMode = serverPlayerChoices.getValue();
            isServerPlayerHumanPlayer = selectedMode.equals("Player");

            if(!isServerPlayerHumanPlayer) {
                isAIPlayerEasyMode = selectedMode.equals("EASY AIPlayer");
            }
        });

        serverSetUpGroup = new Group(serverInfoText,serverStatusText,serverPlayerChoices,
                                     serverTextField, processServerCommandButton,
                                     serverListButton,serverQueueButton,serverChangePlayerButton, serverDisconnectButton);

        serverSetUpScene = new Scene(serverSetUpGroup, 500, 500);



        ChoiceBox<String> gameModeChoiceBox = new ChoiceBox<>();
        gameModeChoiceBox.getItems().addAll("Player vs Player", "Player vs AI", "AI vs AI","Connect To Server");
        gameModeChoiceBox.setValue("Player vs Player");
        gameModeChoiceBox.setLayoutX(190);
        gameModeChoiceBox.setLayoutY(200);

        gameModeChoiceBox.setOnAction(event -> {
            String selectedMode = gameModeChoiceBox.getValue();
            isPlayerVsPlayer = selectedMode.equals("Player vs Player");
            isAIVsAI = selectedMode.equals("AI vs AI");
            isPlayerVsAI = selectedMode.equals("Player vs AI");
            isServerGame = selectedMode.equals("Connect To Server");

            if(isAIVsAI) {
                game.setGameMode("AIvsAI");
                isPlayerVsPlayer = false;
                isAIVsAI = false;
                isPlayerVsAI = false;
                startButton.setVisible(true);
                goToServerScreenButton.setVisible(false);
            }
            else if(isPlayerVsPlayer) {
                game.setGameMode("PlayerVSPlayer");
                isAIVsAI = false;
                isPlayerVsAI = false;
                isServerGame = false;
                startButton.setVisible(true);
                goToServerScreenButton.setVisible(false);
            }
            else if(isPlayerVsAI) {
                game.setGameMode("PlayerVSAI");
                isAIVsAI = false;
                isServerGame = false;
                isPlayerVsPlayer = false;
                startButton.setVisible(true);
                goToServerScreenButton.setVisible(false);

            } else if (isServerGame) {
                game.setGameMode("Connect To Server");
                isAIVsAI = false;
                isPlayerVsAI = false;
                isPlayerVsPlayer = false;
                startButton.setVisible(false);
                goToServerScreenButton.setVisible(true);

            }
            System.out.println("Game mode: " + selectedMode);
            System.out.println(isAIVsAI);
            System.out.println(isPlayerVsPlayer);
        });

        startGroup = new Group(startButton, goToServerScreenButton, toggleButton, gameModeChoiceBox);


        gameOverText = new Text("Game Over!");
        gameOverText.setFill(Color.WHITE);
        gameOverText.setStyle("-fx-font-size: 20;");
        gameOverText.setLayoutX(80);
        gameOverText.setLayoutY(200);

        resetButton = new Button("Reset Game");
        resetButton.setStyle("-fx-font-size: 10; -fx-padding: 8;");
        resetButton.setOnAction(e -> resetGame());
        resetButton.setLayoutX(210);
        resetButton.setLayoutY(250);

        gameOverGroup = new Group(gameOverText,resetButton);


        serverGameOverText = new Text("Server Game Over!");
        serverGameOverText.setFill(Color.WHITE);
        serverGameOverText.setStyle("-fx-font-size: 20;");
        serverGameOverText.setLayoutX(80);
        serverGameOverText.setLayoutY(200);

        serverGameOverButton = new Button("Reset Game");
        serverGameOverButton.setStyle("-fx-font-size: 10; -fx-padding: 8;");

        serverGameOverButton.setOnAction(e -> {
            gameScreen.setScene(serverSetUpScene);
            String currentPlayer;
            if(isServerPlayerHumanPlayer) {
                currentPlayer = "Human player";
            }
            else {
                currentPlayer = "AI player";
            }
            serverPlayer1Name = "";

            serverPlayer2Name = "";
            serverPlayerChoices.setLayoutY(340);
            serverPlayerChoices.setVisible(true);
            serverChangePlayerButton.setVisible(true);
            serverQueueButton.setVisible(false);
            serverDisconnectButton.setVisible(true);
            serverStatusText.setText("You can change your player. Current player: " + currentPlayer);
        });
        serverGameOverButton.setLayoutX(210);
        serverGameOverButton.setLayoutY(250);

        serverGameOverGroup = new Group(serverGameOverText,serverGameOverButton);


        startScene = new Scene(startGroup,500,500);
        startScene.setFill(Color.BLACK);

        gameOverScene = new Scene(gameOverGroup, 500, 500);
        gameOverScene.setFill(Color.BLACK);

        serverGameOverScene = new Scene(serverGameOverGroup,500,500);
        serverGameOverScene.setFill(Color.BLACK);

        gameScene = new Scene(gameGroup, 600, 500);
        gameScene.setFill(Color.BLACK);

        infoText.setLayoutX(50);
        infoText.setLayoutY(480);

        player1ScoreText.setLayoutX(220);
        player1ScoreText.setLayoutY(480);
        player2ScoreText.setLayoutX(340);
        player2ScoreText.setLayoutY(480);

        pane.setMinSize(700, 700);
        pane.setMaxSize(700, 700);

        gameScreen.setTitle("Dots and Boxes");
        gameScreen.setOnCloseRequest(windowEvent -> {
            if(client!=null) {
                handleDisconnect();
            }
            System.exit(0);
        });
        gameScreen.setScene(startScene);
        gameScreen.setResizable(false);

        pane.setOnMouseClicked(this::handleMouseClick);

        gameScreen.show();
    }

    /**
     * Switches the game theme between dark and light.
     */
    private void switchTheme() {

        if(currentThemeDark) {
            currentThemeDark = false;
            gameScene.setFill(currentThemeOpposite);
            gameOverScene.setFill(currentThemeOpposite);
            startScene.setFill(currentThemeOpposite);
            player1ScoreText.setFill(currentTheme);
            player2ScoreText.setFill(currentTheme);
            infoText.setFill(currentTheme);
            gameOverText.setFill(currentTheme);

        }
        else {
            currentThemeDark = true;
            gameScene.setFill(currentTheme);
            gameOverScene.setFill(currentTheme);
            startScene.setFill(currentTheme);
            player1ScoreText.setFill(currentThemeOpposite);
            player2ScoreText.setFill(currentThemeOpposite);
            infoText.setFill(currentThemeOpposite);
            gameOverText.setFill(currentThemeOpposite);
        }

        if(currentThemeDark) {
            System.out.println("Current Theme is dark");
        }
        else {
            System.out.println("Current Theme is light");
        }

    }

    /**
     * Handles the mouse click event.
     *
     * @param event The MouseEvent representing the mouse click event.
     */
    private void handleMouseClick(MouseEvent event)  {

        if(game.getGameMode().equals("PlayerVSPlayer") || (game.getGameMode().equals("PlayerVSAI") && game.isPlayer1Turn()) || game.getGameMode().equals("Connect To Server") &&  client.getHumanPlayer()!= null && client.getPlayerTurn()) {

            Object source = event.getSource();

            if (source instanceof Circle ) {
                Circle clickedCircle = (Circle) source;
                Dot clickedDot = Dot.getDotFromCircle(clickedCircle);


                if (startDot == null) {

                    startDot = clickedDot;


                    if(!isServerGame) {
                        startDot.setFill(game.getCurrentPlayer().getPlayerColor());
                    } else {
                        startDot.setFill(client.getPlayerColor());
                    }



                } else if (game.getUtility().areAdjacentDots(startDot, clickedDot)) {

                    if (getLineBetweenDots(startDot, clickedDot) == null) {

                        if(!isServerGame) {
                            game.getCurrentPlayer().setPlayerMove(null);
                            drawLineBetweenDots(startDot, clickedDot);
                        }


                        if (currentThemeDark) {
                            startDot.setFill(currentThemeOpposite);
                        } else {
                            startDot.setFill(currentTheme);
                        }




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



                        if(!isServerGame) {
                            game.getCurrentPlayer().setPlayerMove(playerLine);
                            game.processMove();
                        }
                        else {
                            int lineServerIndex = playerLine.getMoveIndexForServer();
                            client.sendMove(lineServerIndex);
                        }
                        startDot = null;



                    }

                }


            }


        }

    }




    /**
     * Draws the dots on the game board.
     *
     * @param pane The Pane representing the game board.
     */
    private void drawDots(Pane pane) {
        double spacing = 500.0 / GRID_SIZE;

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                double x = col * spacing + DOT_RADIUS;
                double y = row * spacing + DOT_RADIUS;

                Dot dot = new Dot(x, y, DOT_RADIUS, row, col);


                if(currentThemeDark) {
                    dot.setFill(Color.WHITE);
                }
                else {
                    dot.setFill(Color.BLACK);
                }


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


    /**
     * Resets the game state and displays the start scene.
     */
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

    /**
     * Starts the game by displaying the game scene.
     */
    public void startGame() {

        game.setMoveMadeListener(this);
        gameScreen.setScene(gameScene);
        removeCurrentDots();
        drawDots(pane);
        game.startGame();

    }


    /**
     * Callback method invoked when a move is made in the game.
     *
     * @param move The move made in the game.
     */
    @Override
    public  void onMoveMade(SquaresToDrawForUI move) {


        infoText.setVisible(true);

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

    /**
     * Method called when the game is over.
     */
    @Override
    public void gameOver() {
        if(game.isGameOver()) {
            System.out.println("Game Over");
            int winnerPlayer;
            if(game.getScorePlayer1() > game.getScorePlayer2()) {
                winnerPlayer = 1;
            }
            else {
                winnerPlayer = 2;
            }
            gameOverText.setText("Game Over! Player " + winnerPlayer+  " won. Final Score: " + game.getScorePlayer1() + "-"+game.getScorePlayer2());
            gameScreen.setScene(gameOverScene);
        }
    }

    /**
     * Finds the array of dots corresponding to the lines forming a square.
     *
     * @param lines The array of lines representing the square.
     * @return An array of Dot objects representing the corners of the square.
     */
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

    /**
     * Retrieves the Dot at the specified row and column on the game board.
     *
     * @param row The row index of the Dot.
     * @param col The column index of the Dot.
     * @return The Dot at the specified position, or null if not found.
     */
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


    /**
     * Draws a line between two Dots on the game board.
     *
     * @param startDot The starting Dot for the line.
     * @param endDot   The ending Dot for the line.
     */
    public void drawLineBetweenDots(Dot startDot, Dot endDot) {

        Dot firstDot = getDotAt(startDot.getRow(), startDot.getCol());
        Dot lastDot = getDotAt(endDot.getRow(), endDot.getCol());

        ConnectLine line;

        line = new ConnectLine(startDot.getCenterX(), startDot.getCenterY(), endDot.getCenterX(), endDot.getCenterY(),
                               game.getCurrentPlayer().getPlayerColor(), firstDot, lastDot);


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

    public void drawLineBetweenDots(Dot startDot, Dot endDot, Color playerColor) {

        Dot firstDot = getDotAt(startDot.getRow(), startDot.getCol());
        Dot lastDot = getDotAt(endDot.getRow(), endDot.getCol());

        //TODO: change the else
        ConnectLine line;

        line = new ConnectLine(startDot.getCenterX(), startDot.getCenterY(), endDot.getCenterX(), endDot.getCenterY(),
                               playerColor, firstDot, lastDot);



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

    /**
     * Fills in a square formed by four Dots on the game board.
     *
     * @param dot1 The first Dot of the square.
     * @param dot2 The second Dot of the square.
     * @param dot3 The third Dot of the square.
     * @param dot4 The fourth Dot of the square.
     */
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
    public void fillInSquare(Dot dot1, Dot dot2, Dot dot3, Dot dot4, Color squareColor) {

        Polygon square = new Polygon(
                dot1.getCenterX(), dot1.getCenterY(),
                dot2.getCenterX(), dot2.getCenterY(),
                dot3.getCenterX(), dot3.getCenterY(),
                dot4.getCenterX(), dot4.getCenterY()
        );

        // System.out.println("square filled");
        square.setFill(squareColor);
        pane.getChildren().add(square);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(square.fillProperty(), Color.TRANSPARENT)),
                new KeyFrame(Duration.millis(2000), new KeyValue(square.fillProperty(), squareColor))
        );

        timeline.play();

        dot1.toFront();
        dot2.toFront();
        dot3.toFront();
        dot4.toFront();

    }
    /**
     * Retrieves the ConnectLine between two Dots on the game board.
     *
     * @param startDot The starting Dot.
     * @param endDot   The ending Dot.
     * @return The ConnectLine between the specified Dots, or null if not found.
     */
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

    /**
     * Removes all Dot objects from the game board.
     */
    public void removeCurrentDots() {
        pane.getChildren().removeIf(node -> node instanceof Dot);
    }

    public void switchToServerScreen() {
        gameScreen.setScene(serverSetUpScene);
    }

    public void processServerSend() throws IOException, InterruptedException {
        if(!serverAddressRecieved && !choosePlayerOption) {
            serverStatusText.setText("STATUS");
            serverAddress = serverTextField.getText();
            serverTextField.setText("");
            System.out.println("Server Address: " + serverAddress);
            serverAddressRecieved = true;
            serverInfoText.setText("Enter Server Port: ");
        }
        else if (serverAddressRecieved  && !choosePlayerOption){
            serverPort = Integer.parseInt(serverTextField.getText());
            System.out.println("Server Port: " + serverPort);
            serverInfoText.setText("Choose your Player choice");
            serverTextField.setVisible(false);
            serverPlayerChoices.setVisible(true);
            choosePlayerOption = true;
        } else if (serverAddressRecieved  && choosePlayerOption && !userWillEnterUsername) {

            client = new Client(serverAddress, serverPort);
            client.setGUIListener(this);
            //TODO: change client description
            client.sendHello("its lit!");

            if(isServerPlayerHumanPlayer) {
                client.setHumanPlayer();
                serverStatusText.setText("You are playing as human player.");
            } else {

                if(isAIPlayerEasyMode) {
                    client.setAIPlayer();
                    serverStatusText.setText("Current player: EASY AI");
                    client.getAIPlayer().setEasyMode(true);
                }
                else {
                    client.setAIPlayer();
                    serverStatusText.setText("Current player: AI");
                    client.getAIPlayer().setEasyMode(false);
                }

            }
            client.addListener(this);


            serverTextField.setVisible(true);
            serverTextField.setText("");
            serverPlayerChoices.setVisible(false);
            serverInfoText.setText("Enter your username: ");
            userWillEnterUsername = true;
            serverTextField.setStyle("-fx-text-fill: #6200ff;");


        } else if (serverAddressRecieved  && choosePlayerOption && userWillEnterUsername) {

            client.sendUsername(serverTextField.getText());
            serverTextField.setStyle("-fx-text-fill: black;");
            Thread.sleep(500);
            if(!client.getLogInSuccesfull()) {
                serverStatusText.setText("This username is taken. Please try another username!");
            }
            else {
                serverStatusText.setFill(Color.GREEN);
                serverInfoText.setText("Ready for a game of dots and boxes?");
                serverStatusText.setText("Successfully logged in!");
                client.setUsername(serverTextField.getText());
                processServerCommandButton.setVisible(false);
                serverListButton.setVisible(true);
                serverQueueButton.setVisible(true);

                serverTextField.setText("");
                serverTextField.setMinWidth(300);
                serverTextField.setMinHeight(100);
                serverTextField.setFocusTraversable(false);
                serverTextField.setEditable(false);
                serverTextField.setLayoutX(100);
                serverTextField.setLayoutY(220);
            }

        }


    }



    @Override
    public void handleNewGame(String player1Name, String player2Name)  {
        serverStatusText.setText("Found a game between " + player1Name + " and " + player2Name);

        serverPlayer1Name = player1Name;
        serverPlayer2Name = player2Name;


        Platform.runLater(() -> {
            gameScreen.setScene(gameScene);
            removeCurrentDots();
            drawDots(pane);

            player1ScoreText.setText(serverPlayer1Name + " score: 0" );
            player2ScoreText.setText(serverPlayer2Name + " score: 0" );
            infoText.setText(player1Name + "- turn");

            if(!isServerPlayerHumanPlayer) {

                if (client.getPlayerTurn()) {

                    if (client.getAIPlayer().getNextMove() != null) {
                        int moveToSendToServer = client.getAIPlayer().getNextMove()
                                .getMoveIndexForServer();
                        client.sendMove(moveToSendToServer);
                    }
                }
            }


        });




    }

    @Override
    public void handleMove(String move) {

        Line playerLine;
        if(isServerPlayerHumanPlayer) {
            playerLine = client.getHumanPlayer().getUtility().getServerIndexToLine(Integer.parseInt(move));
        }
        else {
            playerLine = client.getAIPlayer().getUtility().getServerIndexToLine(Integer.parseInt(move));
        }
        String horizontalOrVertical = playerLine.getHorizontalOrVertical();
        int row = playerLine.getHorizontalIndex();
        int col = playerLine.getVerticalIndex();

        Dot startDotFinal;
        Dot endDotFinal;

        if(horizontalOrVertical.equals("H")) {
            startDotFinal = getDotAt(row, col);
            endDotFinal = getDotAt(row, col + 1);
        }
        else if(horizontalOrVertical.equals("V")) {
            startDotFinal = getDotAt(row, col);
            endDotFinal = getDotAt(row + 1, col);
        }
        else {
            return;
        }

        final Dot startDot = startDotFinal;
        final Dot endDot = endDotFinal;

        Color lineColor = client.getPlayerTurn() ? client.getPlayerColor() : client.getOtherPlayerColor();



        Platform.runLater(() -> {


            drawLineBetweenDots(startDot, endDot, lineColor);

        });

        SquaresToDrawForUI moveInfo;
        if(isServerPlayerHumanPlayer) {
            moveInfo = client.getHumanPlayer().getUtility()
                    .getSquaresToDrawForUI(client.getHumanPlayer().getUtility().getServerIndexToLine(Integer.parseInt(move)));
        }
        else {
            moveInfo = client.getAIPlayer().getUtility()
                    .getSquaresToDrawForUI(client.getAIPlayer().getUtility().getServerIndexToLine(Integer.parseInt(move)));
        }
        int movePoint = moveInfo.getCurrentMovePoint();


        if(movePoint > 0 && client.getPlayerTurn() && client.getUsername().equals(serverPlayer1Name)) {
            client.incrementPlayerScore(1,movePoint);
        }
        else if(movePoint > 0 && !client.getPlayerTurn() && client.getUsername().equals(serverPlayer1Name)){
            client.incrementPlayerScore(2,movePoint);
        } else if(movePoint > 0 && client.getPlayerTurn() && client.getUsername().equals(serverPlayer2Name)){
            client.incrementPlayerScore(2,movePoint);
        }
        else if(movePoint > 0 && !client.getPlayerTurn() && client.getUsername().equals(serverPlayer2Name)){
            client.incrementPlayerScore(1,movePoint);
        }

        player1ScoreText.setText(serverPlayer1Name + " score: " + client.getCurrentGamePlayer1Score());
        player2ScoreText.setText(serverPlayer2Name + " score: " + client.getCurrentGamePlayer2Score());

        if (moveInfo.isSquare()) {


            if(moveInfo.getFirstSquare()!=null) {
                Line[] firstSquareLines = moveInfo.getFirstSquare();
                System.out.println(firstSquareLines.length);
                if(firstSquareLines[0]!=null) {

                    Platform.runLater(() -> {
                        Dot[] dotsForSquare = findDotsForSquare(firstSquareLines);
                        fillInSquare(dotsForSquare[0],dotsForSquare[1], dotsForSquare[3], dotsForSquare[2],lineColor);
                    });
                }
            }
            if(moveInfo.getSecondSquare()!=null) {
                Line[] secondSquareLines = moveInfo.getSecondSquare();
                if(secondSquareLines[0]!=null) {

                    Platform.runLater(() -> {
                        Dot[] dotsForSquare = findDotsForSquare(secondSquareLines);
                        fillInSquare(dotsForSquare[0],dotsForSquare[1], dotsForSquare[3], dotsForSquare[2],lineColor);
                    });

                }

            }

        }



    }

    @Override
    public void handleInfoText() {

        if(client.getPlayerTurn() && client.getUsername().equals(serverPlayer1Name)) {
            infoText.setText(client.getUsername() + "- turn");
        } else if (!client.getPlayerTurn() && client.getUsername().equals(serverPlayer1Name)) {
            infoText.setText("Waiting on " + serverPlayer2Name+"...");
        } else if(client.getPlayerTurn() && client.getUsername().equals(serverPlayer2Name)) {
            infoText.setText(client.getUsername() + "- turn");
        } else if (!client.getPlayerTurn() && client.getUsername().equals(serverPlayer2Name)) {
            infoText.setText("Waiting on " + serverPlayer1Name+"...");
        }
    }

    @Override
    public void handleGameOver(String reason, String winner) {
        serverGameOverText.setText("Game Over! " + winner + " won. Reason: " + reason);
        serverQueueButton.setText("Queue For A Game");

        Platform.runLater(() -> {
            pane.getChildren().clear();
            drawDots(pane);
            startDot = null;
            gameScreen.setScene(serverGameOverScene);
        });

    }

    @Override
    public void callAIMoveForServer() {

        if (client.getPlayerTurn()) {

            if (client.getAIPlayer().getNextMove() != null) {
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(event -> {
                    try {
                        int moveToSendToServer = client.getAIPlayer().getNextMove().getMoveIndexForServer();
                        client.sendMove(moveToSendToServer);
                    }catch (NullPointerException e) {
                        System.out.println("next move null");
                    }
                });
                pause.play();
            }
        }
    }



    @Override
    public void handleDisconnect() {

        System.out.println("Disconnected from server.");
        client.close();


        Platform.runLater(() -> {
            serverAddressRecieved = false;

            choosePlayerOption = false;

            userWillEnterUsername = false;
            serverInfoText.setText("Enter Server address: ");
            serverStatusText.setText("STATUS");
            serverStatusText.setFill(Color.RED);
            serverListButton.setVisible(false);
            serverDisconnectButton.setVisible(false);
            serverQueueButton.setVisible(false);
            processServerCommandButton.setVisible(true);
            serverChangePlayerButton.setVisible(false);

            serverPlayerChoices.setLayoutX(100);
            serverPlayerChoices.setLayoutY(160);


            serverTextField.setMinWidth(100);
            serverTextField.setMinHeight(20);
            serverTextField.setLayoutX(100);
            serverTextField.setLayoutY(160);
            serverTextField.setEditable(true);
            serverTextField.setText("");
            serverQueueButton.setText("Queue For A Game");
            pane.getChildren().removeAll();
            drawDots(pane);
            gameScreen.setScene(startScene);
        });


    }
}


