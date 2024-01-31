package BoardDrawingGame.logic;

import BoardDrawingGame.Entities.Board;
import BoardDrawingGame.Entities.Player;
import BoardDrawingGame.view.Line;
import BoardDrawingGame.view.SquaresToDrawForUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * The Game class represents the game logic for a Dots and Boxes game.
 * It manages player turns, moves, scores, and the overall state of the game.
 *
 * The game supports different game modes, including Player vs Player, Player vs AI, and AI vs AI,
 * in offline mode.
 *
 * @author Ali Kaan Uysal
 * @author Bas Van Dijk
 */
public class Game {

	private String player1Name;

	private String player2Name;
	private Player player1;
	private Player player2;

	private String gameMode = "PlayerVSPlayer";

	private Board board;
	private Utility utility;

	private Timeline timeline;
	private Timeline playerVSAItimeline;

	private int moveCountPlayer1 = 1;
	private int moveCountPlayer2 = 1;

	private int scorePlayer1 = 0;
	private int scorePlayer2 = 0;

	private boolean gameOver = false;
	private boolean player1Turn = true;
	private MoveMadeListener moveMadeListener;


	/**
	 * Default constructor for the Game class.
	 * It initializes a new game board and utility.
	 */
	public Game() {
		board = new Board(5);
		utility = new Utility(board);
	}


	/**
	 * Sets the game mode for the current game instance.
	 *
	 * @param gameMode The game mode to set.
	 */
	public void setGameMode(String gameMode) {
		this.gameMode = gameMode;
	}

	/**
	 * Gets the utility class associated with the current game instance.
	 *
	 * @return The utility class.
	 */
	public Utility getUtility() {
		return utility;
	}

	/**
	 * Gets the current game mode.
	 *
	 * @return The current game mode.
	 */
	public String getGameMode() {
		return gameMode;
	}

	/**
	 * Gets the current player whose turn it is.
	 *
	 * @return The current player.
	 */
	public Player getCurrentPlayer() {
		if(player1Turn) {
			return player1;
		}
		else {
			return player2;
		}
	}
	/**
	 * Sets a listener for the GUI to be notified when a move is made or when the game is over.
	 *
	 * @param moveMadeListener The listener to set.
	 */
	public void setMoveMadeListener(MoveMadeListener moveMadeListener) {
		this.moveMadeListener = moveMadeListener;
	}
	/**
	 * Sets up the game with the chosen game mode, by setting its players.
	 *
	 * @param board   The game board to set up for the players.
	 * @param utility The utility class for game-related operations.
	 */
	public void setUpGameDEMO(Board board, Utility utility) {
		if(getGameMode().equals("AIvsAI")) {
			player1 = new AIPlayer(board, utility, Color.RED);
			player2 =  new AIPlayer(board,utility,Color.BLUE);
		}

		else if(getGameMode().equals("PlayerVSPlayer")) {
			player1 = new Player(board, utility, Color.RED);
			player2 =  new Player(board,utility,Color.BLUE);
		} else if (getGameMode().equals("PlayerVSAI")) {
			player1 = new Player(board, utility, Color.RED);
			player2 =  new AIPlayer(board,utility,Color.BLUE);
		}
	}

	/**
	 * Starts the game by initializing the board, utility, and players. It also sets up the game loop.
	 */
	public void startGame() {


		board = new Board(5);
		utility = new Utility(board);

		setUpGameDEMO(board, utility);

		System.out.println("Players Start Playing");



		player1Turn = true;


		if(getGameMode().equals("AIvsAI")) {
			timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> processMove()));
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.play();
		}

	}

	/**
	 * Calls the AI move if the current player is an AIPlayer in a Player vs. AI mode.
	 */
	public void callAIMove() {
		if(getGameMode().equals("PlayerVSAI") && getCurrentPlayer() instanceof AIPlayer) {
			playerVSAItimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> processMove()));
			playerVSAItimeline.setCycleCount(Timeline.INDEFINITE);
			playerVSAItimeline.play();
		}
	}


	/**
	 * Processes the move for the current player, updating the board, scores, and notifying the listener.
	 */
	public void processMove() {
		if (!utility.isGameFinished()) {
			if (player1Turn) // first player's turn
			{

				//if the game mode is player vs player for example this timeline is null and we
				//would get an error
				if(playerVSAItimeline!=null) {
					playerVSAItimeline.stop();
				}
				Line move = player1.getNextMove();
				SquaresToDrawForUI squaresToDrawForUI = utility.getSquaresToDrawForUI(move);
				System.out.println("Move Player 1: " + moveCountPlayer1++ + " " + move.getHorizontalOrVertical() + move.getHorizontalIndex() + move.getVerticalIndex());
				if (utility.calculatePoint(move.getHorizontalOrVertical(), move.getHorizontalIndex(), move.getVerticalIndex()) != 4) {
					if (moveMadeListener != null) {
						moveMadeListener.onMoveMade(squaresToDrawForUI);
					}
					player1Turn = false;
				} else  // Player 1 has made a square
				{
					scorePlayer1 = scorePlayer1 + squaresToDrawForUI.getCurrentMovePoint();
					squaresToDrawForUI.setPlayer1Score(scorePlayer1);
					if (moveMadeListener != null) {
						moveMadeListener.onMoveMade(squaresToDrawForUI);
					}
				}

				board.markLine(move.getHorizontalOrVertical(), move.getHorizontalIndex(), move.getVerticalIndex());
				if(getGameMode().equals("PlayerVSAI") && getCurrentPlayer() instanceof AIPlayer) {
					callAIMove();
				}

				if(utility.isGameFinished()) {
					gameIsOver();
				}

				// HERE: Do not forget to send squaresToDrawForUI  to the UI to draw this line and squares and points

			} else {
				if(playerVSAItimeline!=null) {
					playerVSAItimeline.stop();
				}
				Line move = player2.getNextMove();
				SquaresToDrawForUI squaresToDrawForUI = utility.getSquaresToDrawForUI(move);
				System.out.println("Move Player 2: " + moveCountPlayer2++ + " " + move.getHorizontalOrVertical() + move.getHorizontalIndex() + move.getVerticalIndex());
				if (utility.calculatePoint(move.getHorizontalOrVertical(), move.getHorizontalIndex(), move.getVerticalIndex()) != 4) {
					if (moveMadeListener != null) {
						moveMadeListener.onMoveMade(squaresToDrawForUI);
					}
					player1Turn = true;
				} else  // Player 2 has made a square
				{
					scorePlayer2 = scorePlayer2 + squaresToDrawForUI.getCurrentMovePoint();
					squaresToDrawForUI.setPlayer2Score(scorePlayer2);
					if (moveMadeListener != null) {
						moveMadeListener.onMoveMade(squaresToDrawForUI);
					}
				}
				board.markLine(move.getHorizontalOrVertical(), move.getHorizontalIndex(), move.getVerticalIndex());

				if(getGameMode().equals("PlayerVSAI") && getCurrentPlayer() instanceof AIPlayer) {
					callAIMove();
				}

				if(utility.isGameFinished()) {
					gameIsOver();
				}


			}
		} else {
			gameIsOver();
		}
	}
	/**
	 * Handles the game over scenario, stops the timelines, and prints the final scores and winner.
	 */
	public void gameIsOver() {
		gameOver = true;

		if (moveMadeListener != null) {
			moveMadeListener.gameOver();
		}

		if(timeline!=null) {
			timeline.stop();
		}

		System.out.println("Game Ended");
		System.out.println("Player 1 Score: " + scorePlayer1 + " Player 1 Move Count: " + --moveCountPlayer1);
		System.out.println("Player 2 Score: " + scorePlayer2 + " Player 2 Move Count: " + --moveCountPlayer2);
		if (scorePlayer1 > scorePlayer2) {
			System.out.println("Player 1 won the game");
		} else if (scorePlayer1 < scorePlayer2) {
			System.out.println("Player 2 won the game");
		} else {
			System.out.println("Tie");
		}
	}
	/**
	 * Checks if the game is over.
	 *
	 * @return boolean, true if the game is over.
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * Resets the game by stopping timelines and resetting game variables.
	 */
	public void resetGame() {
		System.out.println("Reseted Game");
		timeline = null;
		moveMadeListener = null;
		playerVSAItimeline = null;
		player1 = null;
		player2 = null;
		moveCountPlayer1 = 1;
		moveCountPlayer2 = 1;
		scorePlayer1 = 0;
		scorePlayer2 = 0;
		player1Turn = true;
		gameOver =  false;

	}



	/**
	 * Gets the score of Player 1.
	 *
	 * @return The score of Player 1.
	 */
	public int getScorePlayer1() {
		return scorePlayer1;
	}

	/**
	 * Checks if it is currently Player 1's turn.
	 *
	 * @return true if it is Player 1's turn, false otherwise.
	 */
	public boolean isPlayer1Turn() {
		return player1Turn;
	}

	/**
	 * Gets the score of Player 2.
	 *
	 * @return The score of Player 2.
	 */
	public int getScorePlayer2() {
		return scorePlayer2;
	}


	/**
	 * Gets the game board associated with the current game instance.
	 *
	 * @return The game board.
	 */
	public Board getBoard() {
		return board;
	}

//	public void setPlayers(Player player1, Player player2) {
//		this.player1 = player1;
//		this.player2 = player2;
//	}

	/**
	 * Sets the names of the players.
	 *
	 * @param playerName1 The name of Player 1.
	 * @param playerName2 The name of Player 2.
	 */
	public void setPlayerNames(String playerName1, String playerName2) {
		this.player1Name = playerName1;
		this.player2Name = playerName2;
	}

	/**
	 * Gets the name of Player 1.
	 *
	 * @return The name of Player 1.
	 */
	public String getPlayer1Name() {
		return player1Name;
	}
	/**
	 * Gets the name of Player 2.
	 *
	 * @return The name of Player 2.
	 */
	public String getPlayer2Name() {
		return player2Name;
	}

	/**
	 * Returns the first player.
	 *
	 * @return The first players object.
	 */
	public Player getPlayer1() {
		return player1;
	}

	/**
	 * Returns the second player.
	 *
	 * @return The second players object.
	 */
	public Player getPlayer2() {
		return player2;
	}


	/**
	 * Increments the points of a player by the specified amount.
	 *
	 * @param playerName The name of the player whose points are to be incremented.
	 * @param points The number of points to be added to the player's score.
	 */
	public void incrementPointByName(String playerName, int points) {
		if(playerName.equals(player1Name)) {
			scorePlayer1 += points;
		}
		else {
			scorePlayer2 += points;
		}
	}


	/**
	 * Retrieves the winner of the game.
	 *
	 * @return The player object representing the winner, or null if the game is not over.
	 */
	public Player getWinner(){
		if (gameOver){
			if (getScorePlayer1() > getScorePlayer2()){
				return player1;
			}
			else{
				return player2;
			}
		}
		return null;
	}
}
