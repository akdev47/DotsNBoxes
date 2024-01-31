package BoardDrawingGame.logic;



import BoardDrawingGame.Entities.Board;
import BoardDrawingGame.Entities.Player;
import BoardDrawingGame.view.Line;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.paint.Color;

/**
 * The AIPlayer class represents an AI player in a Dots and Boxes game.
 * It extends the Player class and includes logic for making moves based on different strategies.
 * The AIPlayer can be adjusted with a specific game board and utility class for game operations.
 * The APlayer class also has a difficulty which can be adjusted when playing in a server game.
 *
 * The AIPlayer has the ability to make moves, including attempts to create a line that completes a
 * square (point), selecting random lines, and avoiding lines that would complete a square with
 * three sides already drawn, and calculating the best possible line.
 *
 * @author Ali Kaan Uysal
 * @author Bas Van Dijk
 */
public class AIPlayer extends Player {
    Board board;
    Utility utility;

    private Color playerColor;
    private boolean easyMode = false;

    /**
     * Constructs a new AIPlayer object with the specified game board, utility, and player color.
     *
     * @param board       The game board associated with the AIPlayer.
     * @param utility     The utility class for game-related operations.
     * @param playerColor The color representing the AIPlayer for the GUI.
     */
    public AIPlayer(Board board, Utility utility, Color playerColor)
    {
        this.board = board;
        this.utility = utility;
        this.playerColor = playerColor;
    }

    /**
     * Default constructor for the AIPlayer class.
     * It creates a new game board and utility class with a default grid size of 5.
     */
    public AIPlayer() {
        board = new Board(5);
        utility = new Utility(board);
    }


    /**
     * Gets the next move to be made by the AIPlayer based on different strategies.
     *
     * @return The next move as a Line object.
     */
    public Line getNextMove()
    {

        //our algorithm for our simple AI is just selecting an empty random move on the game board.
        //our algorithm for the non-simple AI is as follows;
        //1) first attempt to make a line that draws a square and gets a point.
        //2) if this line is not possible, then our AI selects a random line which does not form 3 sides,
        // which means that the opponent will not get free points!
        //3) the final step is reached when the AI has to place a line, but there are no lines which
        // do not form a three square left. At this point, the AI recursively calculates all the
        // points of each possible line, and selects the line which is the best move compared to
        // the rest.
        Line moveResult;

        //this is the AI is not set to easy mode. In default, the AI is not easy. The player can
        //set the AI to be easy when connecting to the server.

        if(!easyMode) {

             moveResult=getNextMoveForPoint();
            if(moveResult != null)
            {
                return moveResult;
            }
            // if move for points is not possible then first select a random line which does not form 3 sides
            else
            {

                moveResult = getNextMoveRandomNotThreeSides();
                if(moveResult != null)
                {
                    return moveResult;
                }
                else
                {
                    moveResult = getNextBestMoveFromThreePoints();   // select best Move > From All the rest

                }
            }
            return moveResult;
        }
        else {
            moveResult = getNextMoveRandom();
            return moveResult;
        }
        // First try to make the move for point

    }

    /**
     * Gets the next move to complete a square point if possible.
     *
     * @return The next move as a Line object, or null if no such move is possible.
     */
    Line getNextMoveForPoint()
    {

        //the utility methods calculates if the line placed makes a square, so if there are
        // 4 lines around the square.

        Line moveResult=null;
        for(int i=0;i<6;i++)
            for(int j=0;j<5;j++)
            {
                if (utility.calculatePoint("H", i, j) == 4)
                {
                    moveResult = new Line("H",i,j);
                    return moveResult;
                }
            }

        for(int i=0;i<5;i++)
            for(int j=0;j<6;j++)
            {
                if (utility.calculatePoint("V", i, j) == 4)
                {
                    moveResult = new Line("V",i,j);
                    return moveResult;
                }
            }

        return moveResult;
    }

    /**
     * Gets the next random move that does not complete a square with three sides already drawn.
     *
     * @return The next move as a Line object, or null.
     */
    Line getNextMoveRandomNotThreeSides()
    {
        Line moveResult=null;



        while(!utility.isOnlyThreePointLinesLeft())
        {
            Random rand = new Random();
            int i = rand.nextInt(6);
            int j = rand.nextInt(5);
            int hOrV = rand.nextInt(2);

            if(hOrV == 0)  // H
            {
                if(utility.calculatePoint("H", i, j) == 1  || utility.calculatePoint("H", i, j) == 2)
                {
                    moveResult = new Line();
                    moveResult.setHorizontalOrVertical("H");
                    moveResult.setHorizontalIndex(i);
                    moveResult.setVerticalIndex(j);
                    return moveResult;
                }
            }
            else  // V
            {
                if(utility.calculatePoint("V", j, i) == 1 || utility.calculatePoint("V", j, i) == 2  )
                {
                    moveResult = new Line();
                    moveResult.setHorizontalOrVertical("V");
                    moveResult.setHorizontalIndex(j);
                    moveResult.setVerticalIndex(i);
                    return moveResult;
                }
            }
        }

        return moveResult;
    }


    /**
     * Gets the next random move from empty lines on the board.
     *
     * @return The next move as a Line object if possible, or null.
     */
    Line getNextMoveRandom()
    {
        Line moveResult=null;



        while(utility.isGameFinished() != true)
        {
            Random rand = new Random();
            int i = rand.nextInt(6);
            int j = rand.nextInt(5);
            int hOrV = rand.nextInt(2);

            if(hOrV == 0)  // H
            {
                if(!board.getHorizontalLines()[i][j])
                {
                    moveResult = new Line();
                    moveResult.setHorizontalOrVertical("H");
                    moveResult.setHorizontalIndex(i);
                    moveResult.setVerticalIndex(j);
                    return moveResult;
                }
            }
            else  // V
            {
                if(!board.getVerticalLines()[j][i])
                {
                    moveResult = new Line();
                    moveResult.setHorizontalOrVertical("V");
                    moveResult.setHorizontalIndex(j);
                    moveResult.setVerticalIndex(i);
                    return moveResult;
                }
            }
        }

        return null;
    }

    /**
     * Determines the next best move from three points on the game board.
     *
     * @return The line representing the next best move.
     */
    Line getNextBestMoveFromThreePoints()
    {
        Line moveResult=null;

        //first, add all the possible left lines to an leftLines arraylist.
        ArrayList <Line>  leftLines = getAllThePossibleMovesFromThreePoints();

        int score = Integer.MAX_VALUE;

        //then we have to loop through all the lines and calculate the score of that line,
        //and keep track of the line with the minimum score
        for( Line currentLine  : leftLines)
        {
            int currentLineScore = calculateScoreForTheLine(currentLine);
            if(currentLineScore < score)
            {
                score = currentLineScore;
                moveResult = currentLine;

            }
        }

        return moveResult;
    }


    /**
     * Generates all possible moves from three points on the game board.
     *
     * @return ArrayList of Line objects representing possible moves left on the board.
     */
    ArrayList<Line> getAllThePossibleMovesFromThreePoints()
    {
        Line moveResult=null;
        ArrayList <Line>  leftLines = new ArrayList();

        // loops though all the vertical and horizontal lines adds it into the empty leftLines array
        // list if there is no line at the current indexes.
        for(int i=0;i<6;i++)
            for(int j=0;j<5;j++)
            {
                if (board.getHorizontalLines()[i][j] == false)
                {
                    moveResult = new Line();
                    moveResult.setHorizontalOrVertical("H");
                    moveResult.setHorizontalIndex(i);
                    moveResult.setVerticalIndex(j);

                    leftLines.add(moveResult);
                }
            }

        for(int i=0;i<5;i++)
            for(int j=0;j<6;j++)
            {
                if (board.getVerticalLines()[i][j] == false)
                {
                    moveResult = new Line();
                    moveResult.setHorizontalOrVertical("V");
                    moveResult.setHorizontalIndex(i);
                    moveResult.setVerticalIndex(j);

                    leftLines.add(moveResult);
                }
            }
        return leftLines;
    }

    /**
     * Recursively calculates all possible moves for a given point on the board.
     *
     * @param newUtility The utility object representing the current state of the game.
     * @param score The current score of the game.
     * @return The updated score after calculating all possible moves for the given point.
     */
    int getAllMovesForPoint(Utility newUtility,int score)
    {
        for(int i=0;i<6;i++)
            for(int j=0;j<5;j++)
            {

                // The recursion continues until no more squares can be completed,
                // at which point the method returns the final score. This also holds for vertical
                // lines.

                if (newUtility.calculatePoint("H", i, j) == 4)
                {
                    //if there is a square, update the score, mark the line on the board to
                    //call this method again;
                    score ++;
                    newUtility.getBoard().markLine("H",i,j);
                    //  we recursively call getAllMovesForPoint with the updated score
                    return getAllMovesForPoint(newUtility,score);
                }
            }

        for(int i=0;i<5;i++)
            for(int j=0;j<6;j++)
            {
                if (newUtility.calculatePoint("V", i, j) == 4)
                {
                    score ++;
                    newUtility.getBoard().markLine("V",i,j);
                    //  same for vertical lines
                    return getAllMovesForPoint(newUtility,score);
                }
            }

        return score;
    }

    /**
     * Calculates the score for a given line on the board.
     *
     * @param line The line for which the score needs to be calculated.
     * @return The score for the given line.
     */
    int calculateScoreForTheLine(Line line)
    {
        //we have to copy the board and send it to the getAllMovesForPoint with the newly created
        //utility object with the copied board, which will return the score for that line.

        Board copiedBoard = utility.copyBoard(board);
        copiedBoard.markLine(line.getHorizontalOrVertical(),line.getHorizontalIndex(),line.getVerticalIndex());
        Utility newUtility = new Utility(copiedBoard);
        int score = getAllMovesForPoint(newUtility,0);
        return score;
    }


    /**
     * Gets the utility class associated with the AIPlayer.
     *
     * @return The utility class.
     */
    public Utility getUtility() {
        return utility;
    }

    /**
     * Gets the game board associated with the AIPlayer.
     *
     * @return The game board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets the color representing the AIPlayer.
     *
     * @return The AIPlayer's color.
     */
    public Color getPlayerColor() {
        return playerColor;
    }


    /**
     * Gets the color representing the AIPlayer.
     *
     * @return The AIPlayer's color.
     */
    public void setEasyMode(boolean value) {
        this.easyMode = value;
    }
}
