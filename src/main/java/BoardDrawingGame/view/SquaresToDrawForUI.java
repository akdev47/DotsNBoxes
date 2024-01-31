package BoardDrawingGame.view;

/**
 * Represents information about squares to draw on the UI.
 */
public class SquaresToDrawForUI {
	private boolean isSquare;
	private Line lineToDraw;
	private Line[] firstSquare;
	private Line[] secondSquare;
	private int player1Score;
	private int player2Score;
	private int currentMovePoint = 0;

	/**
	 * Constructs a SquaresToDrawForUI object.
	 */
	public SquaresToDrawForUI()
	{
		setFirstSquare(new Line[4]);
		setSecondSquare(new Line[4]);
	}

	/**
	 * Gets the lines forming the first square.
	 *
	 * @return An array of lines forming the first square.
	 */
	public Line[] getFirstSquare() {
		return firstSquare;
	}

	/**
	 * Sets the lines forming the first square.
	 *
	 * @param firstSquare An array of lines forming the first square.
	 */
	public void setFirstSquare(Line[] firstSquare) {
		this.firstSquare = firstSquare;
	}


	/**
	 * Gets the lines forming the second square.
	 *
	 * @return An array of lines forming the second square.
	 */
	public Line[] getSecondSquare() {
		return secondSquare;
	}

	/**
	 * Sets the lines forming the second square.
	 *
	 * @param secondSquare An array of lines forming the second square.
	 */
	public void setSecondSquare(Line[] secondSquare) {
		this.secondSquare = secondSquare;
	}

	/**
	 * Gets the score of player 1.
	 *
	 * @return The score of player 1.
	 */
	int getPlayer1Score() { return player1Score; }

	/**
	 * Sets the score of player 1.
	 *
	 * @param player1Score The score of player 1.
	 */
	public void setPlayer1Score(int player1Score) {
		this.player1Score = player1Score;
	}

	/**
	 * Gets the score of player 2.
	 *
	 * @return The score of player 2.
	 */
	int getPlayer2Score() {
		return player2Score;
	}

	/**
	 * Sets the score of player 2.
	 *
	 * @param player2Score The score of player 2.
	 */
	public void setPlayer2Score(int player2Score) {
		this.player2Score = player2Score;
	}

	/**
	 * Checks if a square is formed.
	 *
	 * @return True if a square is formed, false otherwise.
	 */
	public boolean isSquare() {
		return isSquare;
	}

	/**
	 * Sets whether a square is formed.
	 *
	 * @param isSquare True if a square is formed, false otherwise.
	 */
	public void setSquare(boolean isSquare) {
		this.isSquare = isSquare;
	}

	/**
	 * Gets the line to draw.
	 *
	 * @return The line to draw.
	 */
	public Line getLineToDraw() {
		return lineToDraw;
	}

	/**
	 * Sets the line to draw.
	 *
	 * @param lineToDraw The line to draw.
	 */
	public void setLineToDraw(Line lineToDraw) {
		this.lineToDraw = lineToDraw;
	}

	/**
	 * Gets the points earned by the current move.
	 *
	 * @return The points earned by the current move.
	 */
	public int getCurrentMovePoint() {
		return currentMovePoint;
	}

	/**
	 * Sets the points earned by the current move.
	 *
	 * @param currentMovePoint The points earned by the current move.
	 */
	public void setCurrentMovePoint(int currentMovePoint) {
		this.currentMovePoint = currentMovePoint;
	}
	
	
}
