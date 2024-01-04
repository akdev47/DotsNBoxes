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

public class DotsAndBoxes extends Application {


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


    private Group startGroup;
    private Group gameGroup;
    private Group gameOverGroup;

    private Player player1;
    private Player player2;

    public static final int GRID_SIZE = 6;
    private static final double DOT_RADIUS = 7.0;

    @Override
    public void start(Stage gameScreen) throws FileNotFoundException {
        this.gameScreen = gameScreen;
        game = new Game();
        player1 = new Player(Color.RED, "1");
        player2 = new Player(Color.BLUE,"2");
        game.setPlayers(player1,player2);
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
                isPlayerVsPlayer = false;
            }
            if(isPlayerVsPlayer) {
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

            if (source instanceof Circle) {
                Circle clickedCircle = (Circle) source;
                Dot clickedDot = Dot.getDotFromCircle(clickedCircle);


                if (startDot == null) {

                    startDot = clickedDot;

                    if (startDot.isDotFullyConnected()) {
                        startDot = null;
                    } else {
                        startDot.setFill(game.getCurrentPlayerColor());
                    }


                } else if (areAdjacentDots(startDot, clickedDot)) {

                    if (getLineBetweenDots(pane, startDot, clickedDot) == null) {
                        drawLineBetweenDots(pane, startDot, clickedDot);
                        startDot.setFill(Color.BLACK);
                        setConnectedDots(pane, startDot, clickedDot);
                        if (!checkIfSquareOrThreeSides(pane, startDot, clickedDot,4)) {
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
                        makeAIMoveAndContinue();
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
        makeAIMoveAndContinueAIVSAI();
    }

    private void makeAIMoveAndContinue() {
        PauseTransition additionalPause = new PauseTransition(Duration.millis(500));

        additionalPause.setOnFinished(e -> {
            if (!isPlayerVsPlayer) {
                if (!game.isGameTurnPlayer1()) {
                    boolean keepFindingSquare = true;
                    while (keepFindingSquare && !isGameOver()) {
                        keepFindingSquare = makeAIMoveForPoint(4);
                        if (isGameOver()) {
                            break;
                        }
                    }
                }
                else {
                    boolean keepFindingSquare = true;
                    while (keepFindingSquare && !isGameOver()) {
                        keepFindingSquare = makeAIMoveForPoint(4);
                        if (isGameOver()) {
                            break;
                        }
                    }
                }

//                boolean oneMovesAvailable = true;
//                if (!isGameOver()) {
//                   oneMovesAvailable = makeAIMoveForPoint(1);
//                }
//
//                boolean twoMovesAvailable = true;
//                if(!isGameOver() &&  !oneMovesAvailable) {
//                    twoMovesAvailable  = makeAIMoveForPoint(2);
//                }


//                PauseTransition additionalPause2 = new PauseTransition(Duration.millis(700));
//                additionalPause2.setOnFinished(e2 -> {
                    if (!isGameOver()) {
                        makeRandomAIMove();
                    }

                    game.switchTurn();
                    infoText.setText(game.toString());
//                });
//                additionalPause2.play();
            }

        });

        additionalPause.play();
    }
    private void makeAIMoveAndContinueAIVSAI() {
        PauseTransition additionalPause = new PauseTransition(Duration.millis(200));
        additionalPause.setOnFinished(e -> {
        if (!isPlayerVsPlayer && !game.isGameOver()) {
            if (!game.isGameTurnPlayer1()) {
                boolean keepFindingSquare = true;
                while (keepFindingSquare && !isGameOver()) {
                    keepFindingSquare = makeAIMoveForPoint(4);
                    if (isGameOver()) {
                        break;
                    }
                }
            } else {
                boolean keepFindingSquare = true;
                while (keepFindingSquare && !isGameOver()) {
                    keepFindingSquare = makeAIMoveForPoint(4);

                    if (isGameOver()) {
                        break;
                    }
                }
            }



            PauseTransition additionalPause2 = new PauseTransition(Duration.millis(200));
            additionalPause2.setOnFinished(e2 -> {
                if (!isGameOver()) {
                    makeRandomAIMove();
                }

                game.switchTurn();
                infoText.setText(game.toString());
                makeAIMoveAndContinueAIVSAI(); // Call the next iteration
            });
            additionalPause2.play();
        }
        });
        additionalPause.play();
    }
    public boolean makeAIMoveForPoint(int sideCount) {

        boolean foundMove = false;

        //check horizontally

        for(int row=0;row<GRID_SIZE;row++) {
            for(int col=0;col<GRID_SIZE-1;col++) {
                Dot startDot = getDotAt(pane,row,col);
                Dot endDot = getDotAt(pane,row,col+1);

                if(getLineBetweenDots(pane,startDot,endDot)==null) {
                    //                    System.out.println("AI IS ATTEMPTING TO DRAW A LINE IN ROW :" + row + "AND  BETWEEN COLUMN: " + col + " AND " + Math.addExact(col,1));

                    Pane paneCopy = duplicatePane(pane);
                    Dot startCopyDot = getDotAt(paneCopy,row,col);
                    Dot endCopyDot = getDotAt(paneCopy,row,col+1);

                    drawLineBetweenDots(paneCopy,startCopyDot, endCopyDot);
                    setConnectedDots(paneCopy, startCopyDot, endCopyDot);



                    //                    System.out.println("Original Pane Children Count: " + pane.getChildren().size());
                    //                    System.out.println("Duplicated Pane Children Count: " + paneCopy.getChildren().size());



                    if(checkIfSquareOrThreeSides(paneCopy, startCopyDot, endCopyDot,sideCount)) {

                        drawLineBetweenDots(pane,startDot, endDot);
                        setConnectedDots(pane,startDot,endDot);

                        //                        System.out.println("AI HAS MADE A MOVE IN ROW :" + row + "AND  BETWEEN COLUMN: " + col + " AND " + Math.addExact(col,1));


                        foundMove = true;

                        if(sideCount==4) {
                            if(game.isGameTurnPlayer1()) {
                                game.incrementPlayer1Score();
                                player1ScoreText.setText("Player 1 score: " + game.getPlayer1().getPlayerScore());
                            }
                            else {
                                game.incrementPlayer2Score();
                                player2ScoreText.setText("Player 2 score: " + game.getPlayer2().getPlayerScore());
                            }
                        }


                        return true;
                    }

                }


            }

        }



        //        // Check vertically
        //
        for(int col=0;col<GRID_SIZE;col++) {
            for(int row=0;row<GRID_SIZE-1;row++) {
                Dot startDot = getDotAt(pane,row,col);
                Dot endDot = getDotAt(pane,row+1,col);

                if(getLineBetweenDots(pane,startDot,endDot)==null) {
                    //                    System.out.println("AI IS ATTEMPTING TO DRAW A LINE IN ROW :" + row + "AND  BETWEEN COLUMN: " + col + " AND " + Math.addExact(col,1));

                    Pane paneCopy = duplicatePane(pane);
                    Dot startCopyDot = getDotAt(paneCopy,row,col);
                    Dot endCopyDot = getDotAt(paneCopy,row+1,col);

                    drawLineBetweenDots(paneCopy,startCopyDot, endCopyDot);
                    setConnectedDots(paneCopy, startCopyDot, endCopyDot);


                    //                    System.out.println("Original Pane Children Count: " + pane.getChildren().size());
                    //                    System.out.println("Duplicated Pane Children Count: " + paneCopy.getChildren().size());



                    if(checkIfSquareOrThreeSides(paneCopy, startCopyDot, endCopyDot,4)) {
                        drawLineBetweenDots(pane,startDot, endDot);
                        setConnectedDots(pane,startDot,endDot);

                        //                        System.out.println("AI HAS MADE A MOVE IN ROW :" + row + "AND  BETWEEN COLUMN: " + col + " AND " + Math.addExact(col,1));
                        foundMove = true;

                        if(game.isGameTurnPlayer1()) {
                            game.incrementPlayer1Score();
                            player1ScoreText.setText("Player 1 score: " + game.getPlayer1().getPlayerScore());
                        }
                        else {
                            game.incrementPlayer2Score();
                            player2ScoreText.setText("Player 2 score: " + game.getPlayer2().getPlayerScore());
                        }

                        return true;
                    }

                }


            }
        }

        return false;
    }

    public void makeRandomAIMove() {
        boolean moveFound = false;

        while(!moveFound) {

            if(Math.random() < 0.5) {
                int randomRow = (int) Math.floor(Math.random() * 6);
                int randomCol = (int) Math.floor(Math.random() * 5);

                Pane paneCopy = duplicatePane(pane);
                copyDotsConnectionRelationship(pane,paneCopy);

                Dot startDot = getDotAt(paneCopy,randomRow,randomCol);
                Dot endDot = getDotAt(paneCopy,randomRow,randomCol+1);



                drawLineBetweenDots(paneCopy,startDot, endDot);
                setConnectedDots(paneCopy, startDot, endDot);

                Dot OGstartDot = getDotAt(pane,randomRow,randomCol);
                Dot OGendDot = getDotAt(pane,randomRow,randomCol+1);

                if(!checkAnyLeftLines(pane)) {
                    if(getLineBetweenDots(pane,OGstartDot,OGendDot)==null && !checkIfSquareOrThreeSides(paneCopy,startDot,endDot,3)) {
                        setConnectedDots(pane,OGstartDot,OGendDot);
                        drawLineBetweenDots(pane,OGstartDot, OGendDot);

                        moveFound=true;

                        //                    System.out.println("AI HAS MADE A MOVE IN ROW :" + randomRow + "AND  BETWEEN COLUMN: " + randomCol + " AND " + randomCol+1);
                    }
                }
                else {
                    if(getLineBetweenDots(pane,OGstartDot,OGendDot)==null) {
                        setConnectedDots(pane,OGstartDot,OGendDot);
                        drawLineBetweenDots(pane,OGstartDot, OGendDot);

                        moveFound=true;

                        //                    System.out.println("AI HAS MADE A MOVE IN ROW :" + randomRow + "AND  BETWEEN COLUMN: " + randomCol + " AND " + randomCol+1);
                    }
                }



            }
            else {
                int randomRow = (int) Math.floor(Math.random() * 5);
                int randomCol = (int) Math.floor(Math.random() * 6);

                Pane paneCopy = duplicatePane(pane);
                copyDotsConnectionRelationship(pane,paneCopy);

                Dot startDot = getDotAt(paneCopy,randomRow,randomCol);
                Dot endDot = getDotAt(paneCopy,randomRow+1,randomCol);



                drawLineBetweenDots(paneCopy,startDot, endDot);
                setConnectedDots(paneCopy, startDot, endDot);

                Dot OGstartDot = getDotAt(pane,randomRow,randomCol);
                Dot OGendDot = getDotAt(pane,randomRow+1,randomCol);

                if(!checkAnyLeftLines(pane)) {
                    if(getLineBetweenDots(pane,OGstartDot,OGendDot)==null && !checkIfSquareOrThreeSides(paneCopy,startDot,endDot,3)) {
                        setConnectedDots(pane,OGstartDot,OGendDot);
                        drawLineBetweenDots(pane,OGstartDot, OGendDot);

                        moveFound=true;

                        //                    System.out.println("AI HAS MADE A MOVE IN ROW :" + randomRow + "AND  BETWEEN COLUMN: " + randomCol + " AND " + randomCol+1);
                    }
                }
                else {
                    if(getLineBetweenDots(pane,OGstartDot,OGendDot)==null) {
                        setConnectedDots(pane,OGstartDot,OGendDot);
                        drawLineBetweenDots(pane,OGstartDot, OGendDot);

                        moveFound=true;

                        //                    System.out.println("AI HAS MADE A MOVE IN ROW :" + randomRow + "AND  BETWEEN COLUMN: " + randomCol + " AND " + randomCol+1);
                    }
                }
            }

        }
    }

    public boolean checkAnyLeftLines(Pane pane) {


        //check horizontally
        for(int row=0;row<GRID_SIZE;row++) {
            for(int col=0;col<GRID_SIZE-1;col++) {
                Dot startDot = getDotAt(pane,row,col);
                Dot endDot = getDotAt(pane,row,col+1);

                if(getLineBetweenDots(pane,startDot,endDot)==null) {
                    //                    System.out.println("AI IS ATTEMPTING TO DRAW A LINE IN ROW :" + row + "AND  BETWEEN COLUMN: " + col + " AND " + Math.addExact(col,1));

                    Pane paneCopy = duplicatePane(pane);
                    Dot startCopyDot = getDotAt(paneCopy,row,col);
                    Dot endCopyDot = getDotAt(paneCopy,row,col+1);

                    drawLineBetweenDots(paneCopy,startCopyDot, endCopyDot);
                    setConnectedDots(paneCopy, startCopyDot, endCopyDot);



                    //                    System.out.println("Original Pane Children Count: " + pane.getChildren().size());
                    //                    System.out.println("Duplicated Pane Children Count: " + paneCopy.getChildren().size());



                    if(!checkIfSquareOrThreeSides(paneCopy, startCopyDot, endCopyDot,3)) {
                       return false;

                    }



                }


            }

        }




        //check horizontally
        for(int col=0;col<GRID_SIZE;col++) {
            for(int row=0;row<GRID_SIZE-1;row++) {
                Dot startDot = getDotAt(pane,row,col);
                Dot endDot = getDotAt(pane,row+1,col);

                if(getLineBetweenDots(pane,startDot,endDot)==null) {
                    //                    System.out.println("AI IS ATTEMPTING TO DRAW A LINE IN ROW :" + row + "AND  BETWEEN COLUMN: " + col + " AND " + Math.addExact(col,1));

                    Pane paneCopy = duplicatePane(pane);
                    Dot startCopyDot = getDotAt(paneCopy,row,col);
                    Dot endCopyDot = getDotAt(paneCopy,row+1,col);

                    drawLineBetweenDots(paneCopy,startCopyDot, endCopyDot);
                    setConnectedDots(paneCopy, startCopyDot, endCopyDot);

                    if(!checkIfSquareOrThreeSides(paneCopy, startCopyDot, endCopyDot,3)) {
                        return false;

                    }



                }


            }

        }
        return true;
    }



    private boolean isGameOver() {
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
            return true;
        }
        return false;
    }

    private void drawLineBetweenDots(Pane pane, Dot startDot, Dot endDot) {
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

    private void setConnectedDots(Pane panex,Dot startDot, Dot endDot) {

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

    private Dot getDotAt(Pane pane, int row, int col) {
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

    private ConnectLine getLineBetweenDots(Pane pane, Dot startDot, Dot endDot) {
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





    private boolean checkIfSquareOrThreeSides(Pane pane, Dot startDot, Dot endDot,int sideCount) {

        Dot firstDot = getDotAt(pane, startDot.getRow(), startDot.getCol());
        Dot lastDot = getDotAt(pane, endDot.getRow(), endDot.getCol());
        ConnectLine connectedLine = getLineBetweenDots(pane, firstDot,lastDot);


        assert firstDot != null;
        if (firstDot.getRow() == 0 && Objects.requireNonNull(lastDot).getRow()==0&&lastDot.getCol() < GRID_SIZE) {
            assert connectedLine != null;
            return getTopSideLineNum(pane, firstDot, lastDot, startDot, endDot)==sideCount;
        } else if (firstDot.getCol()==0 && firstDot.getRow()< GRID_SIZE && Objects.requireNonNull(
                lastDot).getCol()==0) {
            assert connectedLine != null;
            return getLeftSideLineNum(pane, firstDot, lastDot, startDot, endDot)==sideCount;
        }
        else if (firstDot.getRow()==GRID_SIZE-1 && firstDot.getCol()< GRID_SIZE && Objects.requireNonNull(
                lastDot).getRow()==GRID_SIZE-1) {
            assert connectedLine != null;
            return getBottomSideLineNum(pane, firstDot, lastDot, startDot, endDot)==sideCount;

        }
        else if (firstDot.getCol()==GRID_SIZE-1 && firstDot.getRow()< GRID_SIZE && Objects.requireNonNull(
                lastDot).getCol()==GRID_SIZE-1) {
            assert connectedLine != null;
            return getRightSideLineNum(pane, firstDot, lastDot, startDot, endDot)==sideCount;
        } else {
            assert lastDot != null;
            if (firstDot.getRow()==lastDot.getRow() && Math.abs(firstDot.getCol()-lastDot.getCol())==1) {
                assert connectedLine != null;
                boolean a = getTopSideLineNum(pane, firstDot, lastDot, startDot, endDot)==sideCount;
                boolean b = getBottomSideLineNum(pane, firstDot, lastDot, startDot, endDot)==sideCount;

                if(a&b && sideCount==4) {
                    if(game.isGameTurnPlayer1()) {
                        game.incrementPlayer1Score();
                    }
                    else {
                        game.incrementPlayer2Score();
                    }
                }

                if(sideCount==1) {
                    return a && b;
                }
                else {
                    return a || b;
                }

            }
            else if (firstDot.getCol()==lastDot.getCol() && Math.abs(firstDot.getRow()-lastDot.getRow())==1) {
                assert connectedLine != null;
                boolean a = getRightSideLineNum(pane, firstDot, lastDot, startDot, endDot)==sideCount;
                boolean b = getLeftSideLineNum(pane, firstDot, lastDot, startDot, endDot)==sideCount;



                if(a&&b && sideCount==4) {
                    if(game.isGameTurnPlayer1()) {
                        game.incrementPlayer1Score();
                    }
                    else {
                        game.incrementPlayer2Score();
                    }
                }
                if(sideCount==1) {
                    return a && b;
                }
                else {
                    return a || b;
                }
            }
            else {
                return false;
            }
        }


    }




    public int getTopSideLineNum(Pane pane , Dot firstDot, Dot lastDot, Dot startDot, Dot endDot) {

        int lineCount = 0;

        if(lastDot.getCol()<firstDot.getCol()) {
            Dot temp = firstDot;
            firstDot = lastDot;
            lastDot = temp;

            startDot = firstDot;
            endDot = lastDot;
        }
//        System.out.println("Ok1");
        if (firstDot.getConnectedRightDot() == lastDot &&  lastDot.getConnectedLeftDot() == firstDot) {
            lineCount++;
        }
            //            System.out.println("Ok2");
            firstDot = getDotAt(pane, startDot.getRow(), startDot.getCol() + 1);
            lastDot = getDotAt(pane, endDot.getRow() + 1, endDot.getCol());


            if (firstDot.getConnectedDownDot() == lastDot && lastDot.getConnectedUpDot() == firstDot) {
                lineCount++;
            }
                //                System.out.println("Ok3");
                firstDot = getDotAt(pane, startDot.getRow() + 1, startDot.getCol() + 1);
                lastDot = getDotAt(pane, endDot.getRow() + 1, endDot.getCol() - 1);


                if (firstDot.getConnectedLeftDot() == lastDot && lastDot.getConnectedRightDot() == firstDot) {
                    lineCount++;
                }
                    //                    System.out.println("Ok4");
                    firstDot = getDotAt(pane, startDot.getRow() + 1, startDot.getCol());
                    lastDot = getDotAt(pane, endDot.getRow(), endDot.getCol() - 1);


                    if (firstDot != null && lastDot != null && firstDot.getConnectedUpDot() == lastDot && lastDot.getConnectedDownDot() == firstDot) {
                        lineCount++;
                    }
                    if(lineCount==4) {
                        fillInSquare(startDot, endDot, getDotAt(pane, endDot.getRow() + 1, endDot.getCol()),
                                     getDotAt(pane, startDot.getRow() + 1, startDot.getCol()));
                    }








        return lineCount;
    }



    public int getLeftSideLineNum(Pane pane, Dot firstDot, Dot lastDot, Dot startDot, Dot endDot) {

        int lineCount = 0;

        if(lastDot.getRow()<firstDot.getRow()) {
            Dot temp = firstDot;
            firstDot = lastDot;
            lastDot = temp;

            startDot = firstDot;
            endDot = lastDot;
        }
//        System.out.println("Ok1");
        if (firstDot.getConnectedDownDot() == lastDot &&  lastDot.getConnectedUpDot() == firstDot) {
            lineCount++;
        }

            //            System.out.println("Ok2");
            firstDot = getDotAt(pane, startDot.getRow() + 1, startDot.getCol());
            lastDot = getDotAt(pane, endDot.getRow(), endDot.getCol() + 1);
            ConnectLine nextLine1 = getLineBetweenDots(pane, firstDot, lastDot);



            if (firstDot.getConnectedRightDot() == lastDot && lastDot.getConnectedLeftDot() == firstDot) {
                lineCount++;
            }
                //                        System.out.println("Ok3");
                firstDot = getDotAt(pane, startDot.getRow() + 1, startDot.getCol() + 1);
                lastDot = getDotAt(pane, endDot.getRow() - 1, endDot.getCol() + 1);
                ConnectLine nextLine2 = getLineBetweenDots(pane, firstDot, lastDot);


                assert firstDot != null;
                if (firstDot.getConnectedUpDot() == lastDot && lastDot.getConnectedDownDot() == firstDot) {
                    lineCount++;
                    //                                  System.out.println("Ok4");
                    ConnectLine nextLine3 = getLineBetweenDots(pane, firstDot, lastDot);
                }
        firstDot = getDotAt(pane, startDot.getRow(), startDot.getCol() + 1);
        lastDot = getDotAt(pane, endDot.getRow() - 1, endDot.getCol());



                    if (firstDot != null && lastDot != null && firstDot.getConnectedLeftDot() == lastDot && lastDot.getConnectedRightDot() == firstDot) {
                        lineCount++;
                    }

                    if(lineCount==4) {
                        fillInSquare(startDot, endDot, getDotAt(pane, endDot.getRow(), endDot.getCol() + 1),
                                     getDotAt(pane, startDot.getRow(), startDot.getCol() + 1));
                    }
                        //                                        System.out.println("SQUARE LEFT SIDE");
        return lineCount;
    }

    public int getBottomSideLineNum(Pane pane, Dot firstDot, Dot lastDot, Dot startDot, Dot endDot) {

        int lineCount=0;



        if(lastDot.getCol()<firstDot.getCol()) {
            Dot temp = firstDot;
            firstDot = lastDot;
            lastDot = temp;

            startDot = firstDot;
            endDot = lastDot;
        }
//        System.out.println("Ok1");
        if (firstDot.getConnectedRightDot() == lastDot &&  lastDot.getConnectedLeftDot() == firstDot) {
            lineCount++;
        }
//            System.out.println("Ok2");
            firstDot = getDotAt(pane,startDot.getRow(), startDot.getCol()+1);
            lastDot = getDotAt(pane,endDot.getRow()-1, endDot.getCol());
            ConnectLine nextLine1 = getLineBetweenDots(pane,firstDot,lastDot);

            if (firstDot.getConnectedUpDot() == lastDot && lastDot.getConnectedDownDot() == firstDot) {

                lineCount++;
            }
//                        System.out.println("Ok3");
                        firstDot = getDotAt(pane,startDot.getRow() - 1, startDot.getCol() + 1);
                        lastDot = getDotAt(pane,endDot.getRow() - 1, endDot.getCol() - 1);
                        ConnectLine nextLine2 = getLineBetweenDots(pane,firstDot, lastDot);

                        if (firstDot.getConnectedLeftDot() == lastDot && lastDot.getConnectedRightDot() == firstDot) {
                            lineCount++;
                        }
//                                   System.out.println("Ok4");
                                    firstDot = getDotAt(pane,startDot.getRow() - 1, startDot.getCol());
                                    lastDot = getDotAt(pane,endDot.getRow(), endDot.getCol() - 1);
                                    ConnectLine nextLine3 = getLineBetweenDots(pane,firstDot, lastDot);


                                    if (firstDot != null && lastDot != null && firstDot.getConnectedDownDot() == lastDot && lastDot.getConnectedUpDot() == firstDot) {
                                        lineCount++;
                                    }

                                    if(lineCount==4) {
                                        fillInSquare(startDot,endDot,getDotAt(pane,endDot.getRow()-1, endDot.getCol()),getDotAt(
                                                pane,startDot.getRow()-1, startDot.getCol()));
                                        System.out.println("SQUARE BOTTOM SIDE");
                                    }
//




        return lineCount;
    }

    public int getRightSideLineNum(Pane pane, Dot firstDot, Dot lastDot, Dot startDot, Dot endDot) {

        int lineCount = 0;

        if(lastDot.getRow()<firstDot.getRow()) {
            Dot temp = firstDot;
            firstDot = lastDot;
            lastDot = temp;

            startDot = firstDot;
            endDot = lastDot;
        }
//        System.out.println("Ok1");
        if (firstDot.getConnectedDownDot() == lastDot &&  lastDot.getConnectedUpDot() == firstDot) {
            lineCount++;
        }
//            System.out.println("Ok2");
            firstDot = getDotAt(pane,startDot.getRow()+1, startDot.getCol());
            lastDot = getDotAt(pane,endDot.getRow(), endDot.getCol()-1);
            ConnectLine nextLine1 = getLineBetweenDots(pane,firstDot,lastDot);


            if (firstDot.getConnectedLeftDot() == lastDot && lastDot.getConnectedRightDot() == firstDot) {

                lineCount++;
            }
//                        System.out.println("Ok3");
                        firstDot = getDotAt(pane,startDot.getRow() + 1, startDot.getCol() - 1);
                        lastDot = getDotAt(pane,endDot.getRow() - 1, endDot.getCol() - 1);
                        ConnectLine nextLine2 = getLineBetweenDots(pane,firstDot, lastDot);


                        if (firstDot.getConnectedUpDot() == lastDot && lastDot.getConnectedDownDot() == firstDot) {

                            lineCount++;
                        }

//                                    System.out.println("Ok4");
                                    firstDot = getDotAt(pane,startDot.getRow(), startDot.getCol() - 1);
                                    lastDot = getDotAt(pane,endDot.getRow() - 1, endDot.getCol());
                                    ConnectLine nextLine3 = getLineBetweenDots(pane,firstDot, lastDot);

                                    if (firstDot != null && lastDot != null && firstDot.getConnectedRightDot() == lastDot && lastDot.getConnectedLeftDot() == firstDot) {
                                        lineCount++;
                                    }
                                    if(lineCount==4) {
                                        fillInSquare(startDot,endDot,getDotAt(pane,endDot.getRow(), endDot.getCol()-1),getDotAt(
                                                pane,startDot.getRow(), startDot.getCol()-1));
                                    }
//                                       System.out.println("SQUARE RIGHT SIDE");


        return lineCount;
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

//        System.out.println("square filled");
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
        if(isAIVsAI) {
            startAIVsAIGame();
        }

    }

    private Pane duplicatePane(Pane originalPane) {
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

                Dot newStartDot = getDotAt(pane, originalStartDot.getRow(), originalStartDot.getCol());
                Dot newEndDot = getDotAt(pane, originalEndDot.getRow(), originalEndDot.getCol());

                if (newStartDot != null && newEndDot != null) {
                    ConnectLine newLine = new ConnectLine(newStartDot.getCenterX(), newStartDot.getCenterY(),
                                                          newEndDot.getCenterX(), newEndDot.getCenterY(), originalLine.getLineColor(), newStartDot, newEndDot);
                    newPane.getChildren().add(newLine);
                }
            }
        }
        copyDotsConnectionRelationship(pane, newPane);

        return newPane;
    }

    public void copyDotsConnectionRelationship(Pane originalPane, Pane copyPane) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
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

}

