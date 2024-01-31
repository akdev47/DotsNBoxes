package BoardDrawingGame.Entities;

import BoardDrawingGame.view.Line;

/**
 * The Board class represents the game board for a Dots and Boxes game.
 * It stores information about the horizontal and vertical lines on the board.
 *
 *
 *
 * @author Ali Kaan Uysal
 * @author Bas Van Dijk
 */
public class Board {


	private boolean[][] horizontalLines;
	private boolean[][] verticalLines;


	/**
	 * Constructs a new Board with the specified grid size.
	 *
	 * @param gridsize The size of the grid for the game board.
	 */
	public Board(int gridsize) {
		setHorizontalLines(new boolean [gridsize+1][gridsize]);
		setVerticalLines(new boolean [gridsize][gridsize+1]);
	}

	/**
	 * Gets the  two-dimensional array representing the horizontal lines on the board.
	 *
	 * @return The 2D array of horizontal lines.
	 */
	public boolean[][] getHorizontalLines() {
		return horizontalLines;
	}

	/**
	 * Sets the array representing the horizontal lines on the board.
	 *
	 * @param horizontalLines The array of horizontal lines to set.
	 */
	void setHorizontalLines(boolean[][] horizontalLines) {
		this.horizontalLines = horizontalLines;
	}

	/**
	 * Gets the two-dimensional array representing the vertical lines on the board.
	 *
	 * @return The array of vertical lines.
	 */
	public boolean[][] getVerticalLines() {
		return verticalLines;
	}

	/**
	 * Sets the array representing the vertical lines on the board.
	 *
	 * @param verticalLines The array of vertical lines to set.
	 */
	void setVerticalLines(boolean[][] verticalLines) {
		this.verticalLines = verticalLines;
	}

	/**
	 * Marks a line on the board based on the specified parameters.
	 *
	 * @param horizontalOrVertical The type of line to mark ("H" for horizontal, "V" for vertical).
	 * @param horizontalIndex      The horizontal index of the line.
	 * @param verticalIndex        The vertical index of the line.
	 */
	public void markLine(String horizontalOrVertical, int horizontalIndex, int verticalIndex)
	{
		if(horizontalOrVertical.equalsIgnoreCase("H")){
			horizontalLines[horizontalIndex][verticalIndex]  = true;
		}
		else if(horizontalOrVertical.equalsIgnoreCase("V")){
			verticalLines[horizontalIndex][verticalIndex]  = true;
		}
		
	}

	/**
	 * Marks a line on the board based on the specified server index.
	 *
	 * @param serverIndex The server index to determine the type and position of the line to mark.
	 */
	public void markLine(int serverIndex) {

		//if you are confused about how this works open the "BOARDLINES.png" image.
		//this method has to mark the board based on a server move index.

		String hOrV;
		int horizontalIndex;
		int verticalIndex;

		if(serverIndex>=0 && serverIndex<=4) {
			hOrV = "H";
		} else if (serverIndex>=5 && serverIndex<=10) {
			hOrV = "V";
		} else if (serverIndex>=11 && serverIndex <= 15) {
			hOrV = "H";
		} else if (serverIndex>=16 && serverIndex <= 21) {
			hOrV = "V";
		}else if (serverIndex>=22 && serverIndex <= 26) {
			hOrV = "H";
		} else if (serverIndex>=27 && serverIndex <= 32) {
			hOrV = "V";
		} else if (serverIndex>=33 && serverIndex <= 37) {
			hOrV = "H";
		}else if (serverIndex>=38 && serverIndex <= 43) {
			hOrV = "V";
		}
		else if (serverIndex>=44 && serverIndex <= 48) {
			hOrV = "H";
		}
		else if (serverIndex>=49 && serverIndex <= 54) {
			hOrV = "V";
		}
		else  {
			hOrV = "H";
		}

		if(hOrV.equals("H")) {
			horizontalIndex = serverIndex/ 10;
			verticalIndex = (serverIndex % 10) - horizontalIndex;
			horizontalLines[horizontalIndex][verticalIndex]  = true;
		}
		else {
			horizontalIndex = serverIndex/ 11;
			verticalIndex =  (serverIndex % 11) - 5;
			verticalLines[horizontalIndex][verticalIndex]  = true;
		}
		}


	/**
	 * Checks if the specified Line object represents a valid move.
	 *
	 * @param line The Line object to be checked.
	 * @return true if the move is valid.
	 */
		public boolean isValidMove(Line line) {
			String hOrV = line.getHorizontalOrVertical();
			int horizontalIndex = line.getHorizontalIndex();
			int verticalIndex = line.getVerticalIndex();

			if(hOrV.equals("H")) {
				return !horizontalLines[horizontalIndex][verticalIndex];
			}
			else {
				return !verticalLines[horizontalIndex][verticalIndex];
			}
		}
}


