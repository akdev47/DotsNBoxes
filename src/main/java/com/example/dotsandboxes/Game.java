package com.example.dotsandboxes;

import java.util.Objects;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import static com.example.dotsandboxes.DotsAndBoxes.GRID_SIZE;

public class Game {

    private Player player1;
    private Player player2;

    private Utility utility;

    private boolean gameTurnPlayer1 = true;


    private double maxScore;

    public Game() {
        utility = new Utility();
        maxScore = Math.pow(GRID_SIZE-1,2);
    }

    public void incrementPlayer1Score() {
        player1.incrementScore();
    }

    public void incrementPlayer2Score() {
        player2.incrementScore();
    }

    public boolean isGameOver() {
        return ((player1.getPlayerScore() + player2.getPlayerScore())==maxScore);
    }

    public void resetGame() {
        gameTurnPlayer1 = true;
        player1.resetPlayerScore();
        player2.resetPlayerScore();
    }

    public void switchTurn() {
        gameTurnPlayer1 = !gameTurnPlayer1;
    }

    public Color getCurrentPlayerColor() {
        if(gameTurnPlayer1) {
            return player1.getColor();
        }
        else {
            return player2.getColor();
        }
    }

    public void incrementCurrentPlayer() {
        if(gameTurnPlayer1) {
            incrementPlayer1Score();
        }
        else {
            incrementPlayer2Score();
        }
    }

    public String toString() {
        if(gameTurnPlayer1) {
            return "Player 1 Turn";
        }
        else {
            return "Player 2 Turn";
        }
    }
    public boolean isGameTurnPlayer1() {
        return gameTurnPlayer1;
    }
    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }




    public boolean checkIfSquareOrThreeSides(Pane pane, Dot startDot, Dot endDot, int sideCount) {

        Dot firstDot = utility.getDotAt(pane, startDot.getRow(), startDot.getCol());
        Dot lastDot = utility.getDotAt(pane, endDot.getRow(), endDot.getCol());
        ConnectLine connectedLine = utility.getLineBetweenDots(pane, firstDot,lastDot);


        assert firstDot != null;
        if (firstDot.getRow() == 0 && Objects.requireNonNull(lastDot).getRow()==0&&lastDot.getCol() < DotsAndBoxes.GRID_SIZE) {
            assert connectedLine != null;
            return getTopSideLineNum(pane, firstDot, lastDot, startDot, endDot)==sideCount;
        } else if (firstDot.getCol()==0 && firstDot.getRow()< DotsAndBoxes.GRID_SIZE && Objects.requireNonNull(
                lastDot).getCol()==0) {
            assert connectedLine != null;
            return getLeftSideLineNum(pane, firstDot, lastDot, startDot, endDot)==sideCount;
        }
        else if (firstDot.getRow()==DotsAndBoxes.GRID_SIZE-1 && firstDot.getCol()< DotsAndBoxes.GRID_SIZE && Objects.requireNonNull(
                lastDot).getRow()==DotsAndBoxes.GRID_SIZE-1) {
            assert connectedLine != null;
            return getBottomSideLineNum(pane, firstDot, lastDot, startDot, endDot)==sideCount;

        }
        else if (firstDot.getCol()==DotsAndBoxes.GRID_SIZE-1 && firstDot.getRow()< DotsAndBoxes.GRID_SIZE && Objects.requireNonNull(
                lastDot).getCol()==DotsAndBoxes.GRID_SIZE-1) {
            assert connectedLine != null;
            return getRightSideLineNum(pane, firstDot, lastDot, startDot, endDot)==sideCount;
        } else {
            assert lastDot != null;
            if (firstDot.getRow()==lastDot.getRow() && Math.abs(firstDot.getCol()-lastDot.getCol())==1) {
                assert connectedLine != null;
                boolean a = getTopSideLineNum(pane, firstDot, lastDot, startDot, endDot)==sideCount;
                boolean b = getBottomSideLineNum(pane, firstDot, lastDot, startDot, endDot)==sideCount;

                if(a&b && sideCount==4) {
                    if(isGameTurnPlayer1()) {
                        incrementPlayer1Score();
                    }
                    else {
                        incrementPlayer2Score();
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
                    if(isGameTurnPlayer1()) {
                        incrementPlayer1Score();
                    }
                    else {
                        incrementPlayer2Score();
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
        firstDot = utility.getDotAt(pane, startDot.getRow(), startDot.getCol() + 1);
        lastDot = utility.getDotAt(pane, endDot.getRow() + 1, endDot.getCol());


        if (firstDot.getConnectedDownDot() == lastDot && lastDot.getConnectedUpDot() == firstDot) {
            lineCount++;
        }
        //                System.out.println("Ok3");
        firstDot = utility.getDotAt(pane, startDot.getRow() + 1, startDot.getCol() + 1);
        lastDot = utility.getDotAt(pane, endDot.getRow() + 1, endDot.getCol() - 1);


        if (firstDot.getConnectedLeftDot() == lastDot && lastDot.getConnectedRightDot() == firstDot) {
            lineCount++;
        }
        //                    System.out.println("Ok4");
        firstDot = utility.getDotAt(pane, startDot.getRow() + 1, startDot.getCol());
        lastDot = utility.getDotAt(pane, endDot.getRow(), endDot.getCol() - 1);


        if (firstDot != null && lastDot != null && firstDot.getConnectedUpDot() == lastDot && lastDot.getConnectedDownDot() == firstDot) {
            lineCount++;
        }
        if(lineCount==4) {
            utility.fillInSquare(this,pane,startDot, endDot, utility.getDotAt(pane, endDot.getRow() + 1, endDot.getCol()),
                         utility. getDotAt(pane, startDot.getRow() + 1, startDot.getCol()));
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
        firstDot = utility.getDotAt(pane, startDot.getRow() + 1, startDot.getCol());
        lastDot = utility.getDotAt(pane, endDot.getRow(), endDot.getCol() + 1);




        if (firstDot.getConnectedRightDot() == lastDot && lastDot.getConnectedLeftDot() == firstDot) {
            lineCount++;
        }
        //                        System.out.println("Ok3");
        firstDot = utility.getDotAt(pane, startDot.getRow() + 1, startDot.getCol() + 1);
        lastDot = utility.getDotAt(pane, endDot.getRow() - 1, endDot.getCol() + 1);



        assert firstDot != null;
        if (firstDot.getConnectedUpDot() == lastDot && lastDot.getConnectedDownDot() == firstDot) {
            lineCount++;
            //                                  System.out.println("Ok4");

        }
        firstDot = utility.getDotAt(pane, startDot.getRow(), startDot.getCol() + 1);
        lastDot = utility.getDotAt(pane, endDot.getRow() - 1, endDot.getCol());



        if (firstDot != null && lastDot != null && firstDot.getConnectedLeftDot() == lastDot && lastDot.getConnectedRightDot() == firstDot) {
            lineCount++;
        }

        if(lineCount==4) {
            utility.fillInSquare(this, pane,startDot, endDot, utility.getDotAt(pane, endDot.getRow(), endDot.getCol() + 1),
                         utility.getDotAt(pane, startDot.getRow(), startDot.getCol() + 1));
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
        firstDot = utility.getDotAt(pane,startDot.getRow(), startDot.getCol()+1);
        lastDot = utility.getDotAt(pane,endDot.getRow()-1, endDot.getCol());


        if (firstDot.getConnectedUpDot() == lastDot && lastDot.getConnectedDownDot() == firstDot) {

            lineCount++;
        }
        //                        System.out.println("Ok3");
        firstDot = utility.getDotAt(pane,startDot.getRow() - 1, startDot.getCol() + 1);
        lastDot = utility.getDotAt(pane,endDot.getRow() - 1, endDot.getCol() - 1);


        if (firstDot.getConnectedLeftDot() == lastDot && lastDot.getConnectedRightDot() == firstDot) {
            lineCount++;
        }
        //                                   System.out.println("Ok4");
        firstDot = utility.getDotAt(pane,startDot.getRow() - 1, startDot.getCol());
        lastDot = utility.getDotAt(pane,endDot.getRow(), endDot.getCol() - 1);



        if (firstDot != null && lastDot != null && firstDot.getConnectedDownDot() == lastDot && lastDot.getConnectedUpDot() == firstDot) {
            lineCount++;
        }

        if(lineCount==4) {
            utility.fillInSquare(this,pane, startDot,endDot,utility.getDotAt(pane,endDot.getRow()-1, endDot.getCol()),utility.getDotAt(
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
        firstDot = utility.getDotAt(pane,startDot.getRow()+1, startDot.getCol());
        lastDot = utility.getDotAt(pane,endDot.getRow(), endDot.getCol()-1);



        if (firstDot.getConnectedLeftDot() == lastDot && lastDot.getConnectedRightDot() == firstDot) {

            lineCount++;
        }
        //                        System.out.println("Ok3");
        firstDot = utility.getDotAt(pane,startDot.getRow() + 1, startDot.getCol() - 1);
        lastDot = utility.getDotAt(pane,endDot.getRow() - 1, endDot.getCol() - 1);



        if (firstDot.getConnectedUpDot() == lastDot && lastDot.getConnectedDownDot() == firstDot) {

            lineCount++;
        }

        //                                    System.out.println("Ok4");
        firstDot = utility.getDotAt(pane,startDot.getRow(), startDot.getCol() - 1);
        lastDot = utility.getDotAt(pane,endDot.getRow() - 1, endDot.getCol());

        if (firstDot != null && lastDot != null && firstDot.getConnectedRightDot() == lastDot && lastDot.getConnectedLeftDot() == firstDot) {
            lineCount++;
        }
        if(lineCount==4) {
            utility.fillInSquare(this,pane,startDot,endDot,utility.getDotAt(pane,endDot.getRow(), endDot.getCol()-1),utility.getDotAt(
                    pane,startDot.getRow(), startDot.getCol()-1));
        }
        //                                       System.out.println("SQUARE RIGHT SIDE");


        return lineCount;
    }







}
