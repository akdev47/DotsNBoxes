package BoardDrawingGame.src;

public class Board {
	private boolean[][] horizontalLines;
	private boolean[][] verticalLines;
	
	public Board(){
		setHorizontalLines(new boolean [6][5]);
		setVerticalLines(new boolean [5][6]);
	}

	boolean[][] getHorizontalLines() {
		return horizontalLines;
	}

	void setHorizontalLines(boolean[][] horizontalLines) {
		this.horizontalLines = horizontalLines;
	}

	boolean[][] getVerticalLines() {
		return verticalLines;
	}

	void setVerticalLines(boolean[][] verticalLines) {
		this.verticalLines = verticalLines;
	}
	
	void markLine(String horizontalOrVertical,int horizontalIndex, int verticalIndex)
	{
		if(horizontalOrVertical.equalsIgnoreCase("H")){
			horizontalLines[horizontalIndex][verticalIndex]  = true;
		}
		else if(horizontalOrVertical.equalsIgnoreCase("V")){
			verticalLines[horizontalIndex][verticalIndex]  = true;
		}
		
	}

}
