package BoardDrawingGame.logic;

import BoardDrawingGame.Entities.Board;
import BoardDrawingGame.Entities.Player;
import BoardDrawingGame.view.Line;
import BoardDrawingGame.view.SquaresToDrawForUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Game {

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

//	public static void setUpGame(Board board, Utility utility) {
//		Scanner scanner = new Scanner(System.in);
//
//		boolean contAsking = true;
//
//		while(contAsking) {
//			System.out.println("Please enter game mode from options:");
//			System.out.println("-C: Play against computer");
//			String gameType = scanner.nextLine();
//
//			switch (gameType) {
//				case "-C":
//					player1 = new AIPlayer(board,utility);
//					player2 =  new AIPlayer(board,utility);
//					contAsking = false;
//					break;
//				case "-Q":
//					contAsking = false;
//					break;
//				default:
//					System.out.println("Invalid command.");
//					break;
//
//
//			}
//		}
//
//
//	}

	public void setGameMode(String gameMode) {
		this.gameMode = gameMode;
	}

	public Utility getUtility() {
		return utility;
	}

	public String getGameMode() {
		return gameMode;
	}

	public Player getCurrentPlayer() {
		if(player1Turn) {
			return player1;
		}
		else {
			return player2;
		}
	}

	public void setMoveMadeListener(MoveMadeListener moveMadeListener) {
		this.moveMadeListener = moveMadeListener;
	}

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

	public void startGame() {

		board = new Board(5);
		utility = new Utility(board);

		setUpGameDEMO(board, utility);

		System.out.println("AI Players Start Playing");



		player1Turn = true;


		if(getGameMode().equals("AIvsAI")) {
			timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> processMove()));
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.play();
		}

//		if(getGameMode().equals("PlayerVSAI") && getCurrentPlayer() instanceof AIPlayer) {
//			playerVSAItimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> processMove()));
//			playerVSAItimeline.setCycleCount(Timeline.INDEFINITE);
//			playerVSAItimeline.play();
//		}

	}

	public void callAIMove() {
		if(getGameMode().equals("PlayerVSAI") && getCurrentPlayer() instanceof AIPlayer) {
			playerVSAItimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> processMove()));
			playerVSAItimeline.setCycleCount(Timeline.INDEFINITE);
			playerVSAItimeline.play();
		}
	}


	public void processMove() {
		if (!utility.isGameFinished()) {
			if (player1Turn) // first player's turn
			{
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

				// HERE: Do not forget to send squaresToDrawForUI  to the UI to draw this line and squares and points

			}
		} else {
			gameIsOver();
		}
	}

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

	public boolean isGameOver() {
		return gameOver;
	}

	public void resetGame() {
		System.out.println("Reseted Game");
		timeline = null;
		playerVSAItimeline = null;
		player1 = null;
		player2 = null;
		moveCountPlayer1 = 1;
		moveCountPlayer2 = 1;
		scorePlayer1 = 0;
		scorePlayer2 = 0;
		player1Turn = true;

	}
	
	static void testBoard()
	{

		Board board = new Board(5);
		board.markLine("H", 1, 2);
		board.markLine("V", 1, 2);
		board.markLine("V", 1, 3);
		board.markLine("H", 4, 4);
		board.markLine("V", 4, 4);
		board.markLine("V", 4, 5);
		
		Utility utility = new Utility (board);

		System.out.println("Point for H 1,2: "  + utility.calculatePoint("H", 1, 2) );
		System.out.println("Point for V 1,2: "  + utility.calculatePoint("V", 1, 2));
		System.out.println("Point for V 2,2: "  + utility.calculatePoint("H", 2, 2));
		System.out.println("Point for H 0,4: "  + utility.calculatePoint("H", 0, 4));
		System.out.println("Point for H 5,2: "  + utility.calculatePoint("H", 5, 2));
		System.out.println("Point for V 4,0: "  + utility.calculatePoint("V", 4, 0));
		System.out.println("Point for V 4,5: "  + utility.calculatePoint("V", 4, 5));
		System.out.println("Point for V 1,4: "  + utility.calculatePoint("V", 1, 4));
	}

	public int getScorePlayer1() {
		return scorePlayer1;
	}

	public boolean isPlayer1Turn() {
		return player1Turn;
	}

	public int getScorePlayer2() {
		return scorePlayer2;
	}
	

}
