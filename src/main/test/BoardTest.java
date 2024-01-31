import BoardDrawingGame.Entities.Board;
import BoardDrawingGame.logic.Utility;
import BoardDrawingGame.view.Line;
import jdk.jshell.execution.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class contains JUnit test cases for testing the functionality of the Board class.
 */
public class BoardTest {

    Board board;
    Utility utility;
    /**
     * Sets up a new board and the boards utility before each JUnit execution.
     */
    @BeforeEach
    public void setUp() {
        board = new Board(5);
        utility = new Utility(board);





    }


    /**
     * Tests the markLine method of the Board class.
     */
    @Test
    public void testMarkLine(){

        //test that the line is marked when placed.
        board.markLine("H", 0, 0);
        assertTrue(board.getHorizontalLines()[0][0]);

        board.markLine("V", 0, 0);
        assertTrue(board.getVerticalLines()[0][0]);

        //test there is no line at a index where a line is not placed.
        assertFalse(board.getVerticalLines()[1][1]);
    }


    /**
     * Tests the isValidMove method of the Board class.
     */
    @Test
    public void testValidMove() {

        //test that the line is marked when placed.
        board.markLine("H", 0, 0);

        board.markLine("V", 0, 0);

        Line line = new Line();
        line.setHorizontalOrVertical("H");
        line.setHorizontalIndex(2);
        line.setHorizontalIndex(2);

        //this move is not on the board. it should be valid.
        assertTrue(board.isValidMove(line));

        //now we place that move.
        board.markLine("H",2 , 2);


        Line line2 = new Line();
        line2.setHorizontalOrVertical("H");
        line2.setHorizontalIndex(2);
        line2.setVerticalIndex(2);

        board.markLine(line2.getHorizontalOrVertical(),line2.getHorizontalIndex(),
                       line.getVerticalIndex());

        //since we placed that move, this move should not be valid anymore.
        assertFalse(board.isValidMove(line2));
    }


    /**
     * Tests the markLine method with server index of the Board class.
     */
    @Test
    public void testMarkLineServerIndex() {

        // test for if server works as expected, server 1 = line("H",0,1)
        int serverIndex = 1;

        board.markLine(serverIndex);


        Line line = new Line();
        line.setHorizontalOrVertical("H");
        line.setHorizontalIndex(0);
        line.setVerticalIndex(1);

        assertFalse(board.isValidMove(line));

        //this passes lets try a couple more server 21 = line("V",1,5)

        int serverIndex2 = 21;

        board.markLine(serverIndex2);


        line = new Line();
        line.setHorizontalOrVertical("V");
        line.setHorizontalIndex(1);
        line.setVerticalIndex(5);

        assertFalse(board.isValidMove(line));


        // one more attempt; server 46 = line("H",4,2);

        int serverIndex3 = 46;
        board.markLine(serverIndex3);

        line = new Line();
        line.setHorizontalOrVertical("H");
        line.setHorizontalIndex(4);
        line.setVerticalIndex(2);

        assertFalse(board.isValidMove(line));




    }



}
