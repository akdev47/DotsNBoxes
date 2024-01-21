package BoardDrawingGame.Entities;

public class Board {

	private int GRIDSIZE;
	private boolean[][] horizontalLines;
	private boolean[][] verticalLines;
	
	public Board(int gridsize) {
		this.GRIDSIZE = gridsize;
		setHorizontalLines(new boolean [gridsize+1][gridsize]);
		setVerticalLines(new boolean [gridsize][gridsize+1]);
	}

	public boolean[][] getHorizontalLines() {
		return horizontalLines;
	}

	void setHorizontalLines(boolean[][] horizontalLines) {
		this.horizontalLines = horizontalLines;
	}

	public boolean[][] getVerticalLines() {
		return verticalLines;
	}

	void setVerticalLines(boolean[][] verticalLines) {
		this.verticalLines = verticalLines;
	}
	
	public void markLine(String horizontalOrVertical, int horizontalIndex, int verticalIndex)
	{
		if(horizontalOrVertical.equalsIgnoreCase("H")){
			horizontalLines[horizontalIndex][verticalIndex]  = true;
		}
		else if(horizontalOrVertical.equalsIgnoreCase("V")){
			verticalLines[horizontalIndex][verticalIndex]  = true;
		}
		
	}

}
