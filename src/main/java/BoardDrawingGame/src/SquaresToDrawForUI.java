package BoardDrawingGame.src;

import javafx.scene.paint.Color;

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

	Line[] getFirstSquare() {
		return firstSquare;
	}

	void setFirstSquare(Line[] firstSquare) {
		this.firstSquare = firstSquare;
	}

	Line[] getSecondSquare() {
		return secondSquare;
	}

	void setSecondSquare(Line[] secondSquare) {
		this.secondSquare = secondSquare;
	}

	int getPlayer1Score() {
		return player1Score;
	}

	void setPlayer1Score(int player1Score) {
		this.player1Score = player1Score;
	}

	int getPlayer2Score() {
		return player2Score;
	}

	void setPlayer2Score(int player2Score) {
		this.player2Score = player2Score;
	}

	boolean isSquare() {
		return isSquare;
	}

	void setSquare(boolean isSquare) {
		this.isSquare = isSquare;
	}

	Line getLineToDraw() {
		return lineToDraw;
	}

	void setLineToDraw(Line lineToDraw) {
		this.lineToDraw = lineToDraw;
	}

	int getCurrentMovePoint() {
		return currentMovePoint;
	}

	void setCurrentMovePoint(int currentMovePoint) {
		this.currentMovePoint = currentMovePoint;
	}
	
	
}
