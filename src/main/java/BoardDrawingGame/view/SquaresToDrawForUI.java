package BoardDrawingGame.view;

public class SquaresToDrawForUI {
	private boolean isSquare;
	private Line lineToDraw;
	private Line[] firstSquare;
	private Line[] secondSquare;
	private int player1Score;
	private int player2Score;
	private int currentMovePoint;

	
	public SquaresToDrawForUI()
	{
		setFirstSquare(new Line[4]);
		setSecondSquare(new Line[4]);
	}

	public Line[] getFirstSquare() {
		return firstSquare;
	}

	public void setFirstSquare(Line[] firstSquare) {
		this.firstSquare = firstSquare;
	}

	public Line[] getSecondSquare() {
		return secondSquare;
	}

	public void setSecondSquare(Line[] secondSquare) {
		this.secondSquare = secondSquare;
	}

	int getPlayer1Score() { return player1Score; }

	public void setPlayer1Score(int player1Score) {
		this.player1Score = player1Score;
	}

	int getPlayer2Score() {
		return player2Score;
	}

	public void setPlayer2Score(int player2Score) {
		this.player2Score = player2Score;
	}

	public boolean isSquare() {
		return isSquare;
	}

	public void setSquare(boolean isSquare) {
		this.isSquare = isSquare;
	}

	public Line getLineToDraw() {
		return lineToDraw;
	}

	public void setLineToDraw(Line lineToDraw) {
		this.lineToDraw = lineToDraw;
	}

	public int getCurrentMovePoint() {
		return currentMovePoint;
	}

	public void setCurrentMovePoint(int currentMovePoint) {
		this.currentMovePoint = currentMovePoint;
	}
	
	
}
