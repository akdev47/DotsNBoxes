import BoardDrawingGame.Entities.Player;
import BoardDrawingGame.logic.Game;
import BoardDrawingGame.view.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    Game game = new Game();

    /**
     * Sets up a game before each test.
     */
    @BeforeEach
    public void setUp() {
        game.setGameMode("PlayerVSPlayer");
        game.startGame();
    }

    /**
     * Tests if the turn of the player gets switched successfully.
     */
    @Test
    public void switchMove(){
        Line line = new Line("H", 0, 0);
        game.getPlayer1().setPlayerMove(line);
        game.processMove();
        assertFalse(game.isPlayer1Turn());
    }

    /**
     * Tests game functionality of the makings of a square.
     * @throws InterruptedException for if the game ever gets interrupted by another thread.
     */
    @Test
    public void makeSquare() throws InterruptedException {
        // One square
        Line line1 = new Line("H", 0, 0);
        Line line2 = new Line("H", 1, 0);
        Line line3 = new Line("V", 0, 0);
        Line line4 = new Line("V", 0, 1);

        //Some moves
        Line line5 = new Line("H", 4, 0);
        Line line6 = new Line("H", 3, 0);
        Line line7 = new Line("H", 2, 0);

        game.getPlayer1().setPlayerMove(line1);
        game.processMove();
        game.getPlayer2().setPlayerMove(line5);
        game.processMove();

        game.getPlayer1().setPlayerMove(line2);
        game.processMove();
        game.getPlayer2().setPlayerMove(line6);
        game.processMove();

        game.getPlayer1().setPlayerMove(line3);
        game.processMove();
        game.getPlayer2().setPlayerMove(line7);
        game.processMove();

        game.getPlayer1().setPlayerMove(line4);
        game.processMove();

        assertEquals(1, game.getScorePlayer1());
        assertTrue(game.isPlayer1Turn());
    }

    /**
     * Tests the edge case of two squares being made at the same time.
     * @throws InterruptedException for if the game ever gets interrupted by another thread.
     */
    @Test
    public void makeDoubleSquare() throws InterruptedException {
        // Double square for player 1;  test if vertical line 6 makes the double square and earns
        //two points

        Line p1line1 = new Line("H", 0, 0);
        Line p1line2 = new Line("H", 0, 1);
        Line p1line3 = new Line("V", 0, 2);
        Line p1line4 = new Line("H", 1, 1);
        Line p1line5 = new Line("H", 1, 0);
        Line p1line6 = new Line("V", 0, 0);
        Line p1line7 = new Line("V", 0, 1);

        //random line to waste
        Line p1lineRandom = new Line("V", 3, 1);




        //Double square for player 2;
        Line p2line1 = new Line("H", 0, 4);
        Line p2line2 = new Line("V", 0, 4);
        Line p2line3 = new Line("V", 0, 5);
        Line p2line4 = new Line("V", 1, 4);
        Line p2line5 = new Line("V", 1, 5);
        Line p2line6 = new Line("H", 2, 4);
        Line p2line7 = new Line("H", 1, 4);

        game.getPlayer1().setPlayerMove(p1line1);
        game.processMove();
        game.getPlayer2().setPlayerMove(p2line1);
        game.processMove();

        game.getPlayer1().setPlayerMove(p1line2);
        game.processMove();
        game.getPlayer2().setPlayerMove(p2line2);
        game.processMove();

        game.getPlayer1().setPlayerMove(p1line3);
        game.processMove();
        game.getPlayer2().setPlayerMove(p2line3);
        game.processMove();

        game.getPlayer1().setPlayerMove(p1line4);
        game.processMove();
        game.getPlayer2().setPlayerMove(p2line4);
        game.processMove();

        game.getPlayer1().setPlayerMove(p1line5);
        game.processMove();
        game.getPlayer2().setPlayerMove(p2line5);
        game.processMove();

        game.getPlayer1().setPlayerMove(p1line6);
        game.processMove();
        game.getPlayer2().setPlayerMove(p2line6);
        game.processMove();

        game.getPlayer1().setPlayerMove(p1line7);
        game.processMove();

        game.getPlayer1().setPlayerMove(p1lineRandom);
        game.processMove();

        game.getPlayer2().setPlayerMove(p2line7);
        game.processMove();

        assertEquals(2, game.getScorePlayer1());
        assertEquals(2, game.getScorePlayer2());
        assertTrue(!game.isPlayer1Turn());
    }

    /**
     * This tests if the game ends when the board is full.
     * @throws InterruptedException for if the game gets interrupted.
     */
    @Test
    public void fullBoard() throws InterruptedException {
        for (int i = 0; i <= 5; i++){
            for (int j = 0; j <= 4; j++){
                Line line = new Line("H", i, j);
                if(game.isPlayer1Turn()){
                    game.getPlayer1().setPlayerMove(line);
                }
                else {
                    game.getPlayer2().setPlayerMove(line);
                }
                game.processMove();
            }
        }

        for (int i = 0; i <= 4; i++){
            for (int j = 0; j <= 5; j++){
                Line line = new Line("V", i, j);
                if(game.isPlayer1Turn()){
                    game.getPlayer1().setPlayerMove(line);
                }
                else {
                    game.getPlayer2().setPlayerMove(line);
                }
                game.processMove();
            }
        }
        assertTrue(game.isGameOver());
        assertTrue(game.getScorePlayer1() < game.getScorePlayer2());
    }

    /**
     * This is a totally random gameplay of the entire game where moves are made randomly till the game is completed.
     */
    @Test
    public void totallyRandomGamePlay(){
        while(game.getScorePlayer1() != game.getScorePlayer2()){
            setUp();
            while (!game.isGameOver()){



                if (game.isPlayer1Turn()) {
                    if ((int) (Math.random() * 2) == 0){
                        int randomRow = (int) (Math.random() * 6);
                        int randomCol = (int) (Math.random() * 5);
                        while(isLineOccupiedHorizontal(randomRow, randomCol)) {
                            randomRow = (int) (Math.random() * 6);
                            randomCol = (int) (Math.random() * 5);
                        }

                        Line line = new Line("H", randomRow , randomCol);
                        game.getPlayer1().setPlayerMove(line);
                    }
                    else {
                        int randomRow2 = (int) (Math.random() * 5);
                        int randomCol2 = (int) (Math.random() * 6);
                        while(isLineOccupiedVertical(randomRow2, randomCol2)) {
                            randomRow2 = (int) (Math.random() * 5);
                            randomCol2 = (int) (Math.random() * 6);
                        }
                        Line line = new Line("V", randomRow2 , randomCol2);
                        game.getPlayer1().setPlayerMove(line);
                    }
                }
                else{
                    if ((int) (Math.random() * 2) == 0){
                        int randomRow = (int) (Math.random() * 6);
                        int randomCol = (int) (Math.random() * 5);
                        while(isLineOccupiedHorizontal(randomRow, randomCol)) {
                            randomRow = (int) (Math.random() * 6);
                            randomCol = (int) (Math.random() * 5);
                        }

                        Line line = new Line("H", randomRow , randomCol);
                        game.getPlayer2().setPlayerMove(line);
                    }
                    else {
                        int randomRow2 = (int) (Math.random() * 5);
                        int randomCol2 = (int) (Math.random() * 6);
                        while(isLineOccupiedVertical(randomRow2, randomCol2)) {
                            randomRow2 = (int) (Math.random() * 5);
                            randomCol2 = (int) (Math.random() * 6);
                        }
                        Line line = new Line("V", randomRow2 , randomCol2);
                        game.getPlayer2().setPlayerMove(line);
                    }
                }
                game.processMove();
            }

            assertTrue(game.isGameOver());
            assertEquals(25,game.getScorePlayer1()+ game.getScorePlayer2());
            assertTrue(game.getScorePlayer1() != game.getScorePlayer2());

        }
    }

    /**
     *This tests the basic mechanics of players, like setting their names and scores and getting them and getting the winner which is the player with the highest score.
     */
    @Test
    public void basicPlayerMechanics(){
        //Tests if the name setter works.
        game.setPlayerNames("A", "B");
        assertEquals("A", game.getPlayer1Name());
        assertEquals("B", game.getPlayer2Name());

        //Tests if the score setter works.
        assertEquals(0, game.getScorePlayer1());
        game.incrementPointByName("A", 5);
        assertEquals(5, game.getScorePlayer1());

        assertEquals(0, game.getScorePlayer2());
        game.incrementPointByName("B", 3);
        assertEquals(3, game.getScorePlayer2());

        //Tests if player2 can win.
        game.gameIsOver();
        assertEquals(game.getPlayer1(), game.getWinner());
        game.resetGame();

        //Tests if tie works.
        game.gameIsOver();
        assertNull(game.getWinner());
    }
    private boolean isLineOccupiedHorizontal(int row, int col) {
        return game.getBoard().getHorizontalLines()[row][col];
    }

    private boolean isLineOccupiedVertical(int row, int col) {
        return game.getBoard().getVerticalLines()[row][col];
    }

}