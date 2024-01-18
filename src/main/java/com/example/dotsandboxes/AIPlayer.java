package com.example.dotsandboxes;

import javafx.animation.PauseTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import static com.example.dotsandboxes.DotsAndBoxes.GRID_SIZE;

public class AIPlayer extends Player {

    public interface InfoTextListener {
        void onInfoTextChanged(String newText);
    }

    public interface ScoreUpdateListener {
        void onPlayer1ScoreUpdated(int newScore);
        void onPlayer2ScoreUpdated(int newScore);
    }




    private final Utility utility;
    private Game game;


    private InfoTextListener infoTextListener;
    private ScoreUpdateListener scoreUpdateListener;

    public void setInfoTextListener(InfoTextListener listener) {
        this.infoTextListener = listener;
    }

    public void setScoreUpdateListener(ScoreUpdateListener listener) {
        this.scoreUpdateListener = listener;
    }

    public AIPlayer(Color color, String name, Game game) {
        super(color, name);
        utility = new Utility();
        this.game = game;
    }




    public void makeRandomAIMove(Pane pane, Game game) {
        boolean moveFound = false;

        while(!moveFound) {

            if(Math.random() < 0.5) {
                int randomRow = (int) Math.floor(Math.random() * GRID_SIZE);
                int randomCol = (int) Math.floor(Math.random() * (GRID_SIZE-1));

                Pane paneCopy = utility.duplicatePane(pane);
                utility.copyDotsConnectionRelationship(pane,paneCopy);

                Dot startDot = utility.getDotAt(paneCopy,randomRow,randomCol);
                Dot endDot = utility.getDotAt(paneCopy,randomRow,randomCol+1);



                utility.drawLineBetweenDots(game,paneCopy,startDot, endDot);
                utility.setConnectedDots(paneCopy, startDot, endDot);

                Dot OGstartDot = utility.getDotAt(pane,randomRow,randomCol);
                Dot OGendDot = utility.getDotAt(pane,randomRow,randomCol+1);

                if(!checkAnyLeftLines(pane)) {
                    if(utility.getLineBetweenDots(pane,OGstartDot,OGendDot)==null && !game.checkIfSquareOrThreeSides(paneCopy,startDot,endDot,3)) {
                        utility.setConnectedDots(pane,OGstartDot,OGendDot);
                        utility.drawLineBetweenDots(game,pane,OGstartDot, OGendDot);

                        moveFound=true;

                        //                    System.out.println("AI HAS MADE A MOVE IN ROW :" + randomRow + "AND  BETWEEN COLUMN: " + randomCol + " AND " + randomCol+1);
                    }
                }
                else {
                    if(utility.getLineBetweenDots(pane,OGstartDot,OGendDot)==null) {
                        utility.setConnectedDots(pane,OGstartDot,OGendDot);
                       utility.drawLineBetweenDots(game,pane,OGstartDot, OGendDot);

                        moveFound=true;

                        //                    System.out.println("AI HAS MADE A MOVE IN ROW :" + randomRow + "AND  BETWEEN COLUMN: " + randomCol + " AND " + randomCol+1);
                    }
                }



            }
            else {
                int randomRow = (int) Math.floor(Math.random() * (GRID_SIZE-1));
                int randomCol = (int) Math.floor(Math.random() * GRID_SIZE);

                Pane paneCopy = utility.duplicatePane(pane);
                utility.copyDotsConnectionRelationship(pane,paneCopy);

                Dot startDot = utility.getDotAt(paneCopy,randomRow,randomCol);
                Dot endDot = utility.getDotAt(paneCopy,randomRow+1,randomCol);



                utility.drawLineBetweenDots(game,paneCopy,startDot, endDot);
                utility.setConnectedDots(paneCopy, startDot, endDot);

                Dot OGstartDot = utility.getDotAt(pane,randomRow,randomCol);
                Dot OGendDot = utility.getDotAt(pane,randomRow+1,randomCol);

                if(!checkAnyLeftLines(pane)) {
                    if(utility.getLineBetweenDots(pane,OGstartDot,OGendDot)==null && !game.checkIfSquareOrThreeSides(paneCopy,startDot,endDot,3)) {
                        utility.setConnectedDots(pane,OGstartDot,OGendDot);
                        utility.drawLineBetweenDots(game,pane,OGstartDot, OGendDot);

                        moveFound=true;

                        //                    System.out.println("AI HAS MADE A MOVE IN ROW :" + randomRow + "AND  BETWEEN COLUMN: " + randomCol + " AND " + randomCol+1);
                    }
                }
                else {
                    if(utility.getLineBetweenDots(pane,OGstartDot,OGendDot)==null) {
                        utility.setConnectedDots(pane,OGstartDot,OGendDot);
                        utility.drawLineBetweenDots(game,pane,OGstartDot, OGendDot);

                        moveFound=true;

                        //                    System.out.println("AI HAS MADE A MOVE IN ROW :" + randomRow + "AND  BETWEEN COLUMN: " + randomCol + " AND " + randomCol+1);
                    }
                }
            }

        }
    }

    public boolean makeAIMoveForPoint(Pane pane,int sideCount) {

        boolean foundMove = false;

        //check horizontally

        for(int row=0;row<GRID_SIZE;row++) {
            for(int col=0;col<GRID_SIZE-1;col++) {
                Dot startDot = utility.getDotAt(pane,row,col);
                Dot endDot = utility.getDotAt(pane,row,col+1);

                if(utility.getLineBetweenDots(pane,startDot,endDot)==null) {
                    //                    System.out.println("AI IS ATTEMPTING TO DRAW A LINE IN ROW :" + row + "AND  BETWEEN COLUMN: " + col + " AND " + Math.addExact(col,1));

                    Pane paneCopy = utility.duplicatePane(pane);
                    Dot startCopyDot = utility.getDotAt(paneCopy,row,col);
                    Dot endCopyDot = utility.getDotAt(paneCopy,row,col+1);

                    utility.drawLineBetweenDots(game,paneCopy,startCopyDot, endCopyDot);
                    utility.setConnectedDots(paneCopy, startCopyDot, endCopyDot);



                    //                    System.out.println("Original Pane Children Count: " + pane.getChildren().size());
                    //                    System.out.println("Duplicated Pane Children Count: " + paneCopy.getChildren().size());



                    if(game.checkIfSquareOrThreeSides(paneCopy, startCopyDot, endCopyDot,sideCount)) {

                        utility.drawLineBetweenDots(game,pane,startDot, endDot);
                        utility.setConnectedDots(pane,startDot,endDot);

                        //                        System.out.println("AI HAS MADE A MOVE IN ROW :" + row + "AND  BETWEEN COLUMN: " + col + " AND " + Math.addExact(col,1));


                        foundMove = true;

                        if(sideCount==4) {

                            game.checkIfSquareOrThreeSides(pane, startCopyDot, endCopyDot,sideCount);

                            System.out.println("ai made square");

                            if(game.isGameTurnPlayer1()) {
                                game.incrementPlayer1Score();
                                if (scoreUpdateListener != null) {
                                    scoreUpdateListener.onPlayer1ScoreUpdated(game.getPlayer1().getPlayerScore());
                                }
//                                player1ScoreText.setText("Player 1 score: " + game.getPlayer1().getPlayerScore());
                            }
                            else {
                                game.incrementPlayer2Score();
                                if (scoreUpdateListener != null) {
                                    scoreUpdateListener.onPlayer2ScoreUpdated(game.getPlayer2().getPlayerScore());
                                }
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
                Dot startDot = utility.getDotAt(pane,row,col);
                Dot endDot = utility.getDotAt(pane,row+1,col);

                if(utility.getLineBetweenDots(pane,startDot,endDot)==null) {
                    //                    System.out.println("AI IS ATTEMPTING TO DRAW A LINE IN ROW :" + row + "AND  BETWEEN COLUMN: " + col + " AND " + Math.addExact(col,1));

                    Pane paneCopy = utility.duplicatePane(pane);
                    Dot startCopyDot = utility.getDotAt(paneCopy,row,col);
                    Dot endCopyDot = utility.getDotAt(paneCopy,row+1,col);

                    utility.drawLineBetweenDots(game,paneCopy,startCopyDot, endCopyDot);
                    utility.setConnectedDots(paneCopy, startCopyDot, endCopyDot);


                    //                    System.out.println("Original Pane Children Count: " + pane.getChildren().size());
                    //                    System.out.println("Duplicated Pane Children Count: " + paneCopy.getChildren().size());



                    if(game.checkIfSquareOrThreeSides(paneCopy, startCopyDot, endCopyDot,4)) {



                        utility.drawLineBetweenDots(game,pane,startDot, endDot);
                        utility.setConnectedDots(pane,startDot,endDot);

                        //                        System.out.println("AI HAS MADE A MOVE IN ROW :" + row + "AND  BETWEEN COLUMN: " + col + " AND " + Math.addExact(col,1));
                        foundMove = true;

                        if(sideCount==4) {

                            game.checkIfSquareOrThreeSides(pane, startCopyDot, endCopyDot,sideCount);

                            System.out.println("ai made square");

                            if(game.isGameTurnPlayer1()) {
                                game.incrementPlayer1Score();
                                if (scoreUpdateListener != null) {
                                    scoreUpdateListener.onPlayer1ScoreUpdated(game.getPlayer1().getPlayerScore());
                                }
                                //                                player1ScoreText.setText("Player 1 score: " + game.getPlayer1().getPlayerScore());
                            }
                            else {
                                game.incrementPlayer2Score();
                                if (scoreUpdateListener != null) {
                                    scoreUpdateListener.onPlayer2ScoreUpdated(game.getPlayer2().getPlayerScore());
                                }
                            }
                        }


                        return true;
                    }

                }


            }
        }

        return false;
    }

//    public void makeAIMoveAndContinueAIVSAI(Pane pane) {
//        PauseTransition additionalPause = new PauseTransition(Duration.millis(500));
//        additionalPause.setOnFinished(e -> {
//            if (!game.isGameOver()) {
//                if (!game.isGameTurnPlayer1()) {
//                    boolean keepFindingSquare = true;
//                    while (keepFindingSquare && !game.isGameOver()) {
//                        keepFindingSquare = makeAIMoveForPoint(pane,4);
//                        if (game.isGameOver()) {
//                            break;
//                        }
//                    }
//                } else {
//                    boolean keepFindingSquare = true;
//                    while (keepFindingSquare && !game.isGameOver()) {
//                        keepFindingSquare = makeAIMoveForPoint(pane,4);
//
//                        if (game.isGameOver()) {
//                            break;
//                        }
//                    }
//                }
//
//
//
//                PauseTransition additionalPause2 = new PauseTransition(Duration.millis(500));
//                additionalPause2.setOnFinished(e2 -> {
//                    if (game.isGameOver()) {
//                        makeRandomAIMove(pane,game);
//                    }
//
//                    game.switchTurn();
////                    infoText.setText(game.toString());
//                    infoTextListener.onInfoTextChanged(game.toString());
//                    makeAIMoveAndContinueAIVSAI(pane); // Call the next iteration
//                });
//                additionalPause2.play();
//            }
//        });
//        additionalPause.play();
//    }


//    public void makeAIMoveAndContinueAIVSAI(Pane pane) {
//        PauseTransition additionalPause = new PauseTransition(Duration.millis(1000));
//
//        additionalPause.setOnFinished(e -> {
//            if (!game.isGameTurnPlayer1()) {
//                boolean keepFindingSquare = true;
//                while (keepFindingSquare && !game.isGameOver()) {
//                    keepFindingSquare = makeAIMoveForPoint(pane,4);
//                    if (game.isGameOver()) {
//                        break;
//                    }
//                }
//            }
//            else {
//                boolean keepFindingSquare = true;
//                while (keepFindingSquare && !game.isGameOver()) {
//                    keepFindingSquare = makeAIMoveForPoint(pane,4);
//                    if (game.isGameOver()) {
//                        break;
//                    }
//                }
//            }
//
//            //                boolean oneMovesAvailable = true;
//            //                if (!isGameOver()) {
//            //                   oneMovesAvailable = makeAIMoveForPoint(1);
//            //                }
//            //
//            //                boolean twoMovesAvailable = true;
//            //                if(!isGameOver() &&  !oneMovesAvailable) {
//            //                    twoMovesAvailable  = makeAIMoveForPoint(2);
//            //                }
//
//
//            //                PauseTransition additionalPause2 = new PauseTransition(Duration.millis(700));
//            //                additionalPause2.setOnFinished(e2 -> {
//            if (!game.isGameOver()) {
//                makeRandomAIMove(pane,game);
//            }
//
//            game.switchTurn();
//            if(infoTextListener!=null) {
//                infoTextListener.onInfoTextChanged(game.toString());
//            }
//            //                });
//            //                additionalPause2.play();
//
//
//
//            makeAIMoveAndContinueAIVSAI(pane);
//
//
//
//        });
//
//        additionalPause.play();
//    }


    public void makeAIMoveAndContinue(boolean isAiVsAi, Pane pane) {
        PauseTransition additionalPause = new PauseTransition(Duration.millis(500));

        additionalPause.setOnFinished(e -> {

                    boolean keepFindingSquare = true;
                    while (keepFindingSquare && !game.isGameOver()) {
                        keepFindingSquare = makeAIMoveForPoint(pane,4);
                        if (game.isGameOver()) {
                            break;
                        }
                    }

                if (!game.isGameOver()) {
                    makeRandomAIMove(pane,game);
                }

                game.switchTurn();

                 if(infoTextListener!=null) {
                     infoTextListener.onInfoTextChanged(game.toString());
                 }




            if(isAiVsAi && !game.isGameOver()) {
                makeAIMoveAndContinue(isAiVsAi,pane);
            }

        });

        additionalPause.play();
    }

    public boolean checkAnyLeftLines(Pane pane) {


        //check horizontally
        for(int row=0;row<DotsAndBoxes.GRID_SIZE;row++) {
            for(int col=0;col<DotsAndBoxes.GRID_SIZE-1;col++) {
                Dot startDot = utility.getDotAt(pane,row,col);
                Dot endDot = utility.getDotAt(pane,row,col+1);

                if(utility.getLineBetweenDots(pane,startDot,endDot)==null) {
                    //                    System.out.println("AI IS ATTEMPTING TO DRAW A LINE IN ROW :" + row + "AND  BETWEEN COLUMN: " + col + " AND " + Math.addExact(col,1));

                    Pane paneCopy = utility.duplicatePane(pane);
                    Dot startCopyDot = utility.getDotAt(paneCopy,row,col);
                    Dot endCopyDot = utility.getDotAt(paneCopy,row,col+1);

                    utility.drawLineBetweenDots(game,paneCopy,startCopyDot, endCopyDot);
                    utility.setConnectedDots(paneCopy, startCopyDot, endCopyDot);



                    //                    System.out.println("Original Pane Children Count: " + pane.getChildren().size());
                    //                    System.out.println("Duplicated Pane Children Count: " + paneCopy.getChildren().size());



                    if(!game.checkIfSquareOrThreeSides(paneCopy, startCopyDot, endCopyDot,3)) {
                        return false;

                    }



                }


            }

        }


        //check horizontally
        for(int col=0;col<DotsAndBoxes.GRID_SIZE;col++) {
            for(int row=0;row<DotsAndBoxes.GRID_SIZE-1;row++) {
                Dot startDot = utility.getDotAt(pane,row,col);
                Dot endDot = utility.getDotAt(pane,row+1,col);

                if(utility.getLineBetweenDots(pane,startDot,endDot)==null) {
                    //                    System.out.println("AI IS ATTEMPTING TO DRAW A LINE IN ROW :" + row + "AND  BETWEEN COLUMN: " + col + " AND " + Math.addExact(col,1));

                    Pane paneCopy = utility.duplicatePane(pane);
                    Dot startCopyDot = utility.getDotAt(paneCopy,row,col);
                    Dot endCopyDot = utility.getDotAt(paneCopy,row+1,col);

                    utility.drawLineBetweenDots(game, paneCopy,startCopyDot, endCopyDot);
                    utility.setConnectedDots(paneCopy, startCopyDot, endCopyDot);

                    if(!game.checkIfSquareOrThreeSides(paneCopy, startCopyDot, endCopyDot,3)) {
                        return false;

                    }



                }


            }

        }
        return true;
    }

}
