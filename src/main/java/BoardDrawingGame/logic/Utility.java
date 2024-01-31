package BoardDrawingGame.logic;

import BoardDrawingGame.Entities.Board;
import BoardDrawingGame.view.Dot;
import BoardDrawingGame.view.Line;
import BoardDrawingGame.view.SquaresToDrawForUI;
/**
 * The Utility class provides utility methods as an helper class for calculating points,
 * checking game completion,and determining squares to draw on the UI in a Dots and Boxes game.
 * This class is responsible for various game related calculations and checks the gameplay logic.
 *
 * It includes methods for calculating points, checking game completion conditions, and determining
 * the squares to draw on the UI based on the moves made during the game.
 *
 * @author Ali Kaan Uysal
 * @author Bas Van Dijk
 */
public class Utility {
	private Board board;

	/**
	 * Constructs a Utility object with the specified game board.
	 *
	 * @param board The game board.
	 */
	public Utility (Board board)
	{
		this.board = board;
	}

	/**
	 * Calculates the points for a specific move based on its position and orientation.
	 * The points are determined by the lines surrounding the move on the game board.
	 *
	 * @param horizontalOrVertical The orientation of the move ("H" for horizontal, "V" for vertical).
	 * @param horizontalIndex      The horizontal index of the move.
	 * @param verticalIndex        The vertical index of the move.
	 * @return The calculated points for the move.
	 */
	public int calculatePoint (String horizontalOrVertical,int horizontalIndex, int verticalIndex)

	{
		int point= 0;
		if(horizontalOrVertical.equalsIgnoreCase("H"))
		{
			if (horizontalIndex == 0)  // top horizontal lines
			{
				return calculatePointForTopHorizontalLines(horizontalIndex,verticalIndex);
			}
			else if (horizontalIndex == 5) // bottom horizontal lines
			{
				return calculatePointForBottomHorizontalLines(horizontalIndex,verticalIndex);
			}
			else  // middle horrizontal lines
			{
				return calculatePointForMiddleHorizontalLines(horizontalIndex, verticalIndex);
			}
		}
		else  if(horizontalOrVertical.equalsIgnoreCase("V"))
		{
			if (verticalIndex == 0)  // left vertical lines
			{
				return calculatePointForLeftVerticalLines(horizontalIndex,verticalIndex);
			}
			else if (verticalIndex == 5) // right vertical lines
			{
				return calculatePointForRightVerticalLines(horizontalIndex,verticalIndex);
			}
			else  // middle vertical lines
			{
				return calculatePointForMiddleVerticalLines(horizontalIndex, verticalIndex);
			}
		}
			
		return point;	
	}

	/**
	 * Calculates points for a move based on its server index.
	 *
	 * @param serverIndex The server index of the move.
	 * @return The calculated points for the move.
	 */
	public int calculatePointWithServerIndex(int serverIndex) {
		//This method calls the previous calculatePoints method but calls it by converting the server
		// index into its appropriate format. (With a string and two integers.)
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
		}
		else {
			horizontalIndex = serverIndex/ 11;
			verticalIndex =  (serverIndex % 11) - 5;
		}
		return calculatePoint(hOrV,horizontalIndex,verticalIndex);
	}
	


	/**
	 * This method calculates points for only top horizontal lines whose horizontal index is 0.
	 *
	 * @param horizontalIndex The horizontalIndex of the line.
	 * @param verticalIndex The horizontalIndex of the line.
	 * @return int The calculated points for the move.
	 */
	public int calculatePointForTopHorizontalLines (int horizontalIndex, int verticalIndex)
	{
		int point= 1;
		if(board.getHorizontalLines()[horizontalIndex][verticalIndex] == true)
		{
			return 0;
		}
		else  // the line at the board is empty calculate the point
		{
			if(board.getHorizontalLines()[horizontalIndex + 1][verticalIndex] == true)
			{
				point++;
			}
			if(board.getVerticalLines()[horizontalIndex][verticalIndex] == true)
			{
				point++;
			}
			if(board.getVerticalLines()[horizontalIndex][verticalIndex + 1] == true)
			{
				point++;
			}

		}

		return point;
		
	}


	/**
	 * This method calculates points for only bottom horizontal lines whose horizontal index is 0.
	 *
	 * @param horizontalIndex The horizontalIndex of the line.
	 * @param verticalIndex The horizontalIndex of the line.
	 * @return int The calculated points for the move.
	 */
	public int calculatePointForBottomHorizontalLines (int horizontalIndex, int verticalIndex)
	{
		int point= 1;
		if(board.getHorizontalLines()[horizontalIndex][verticalIndex] == true)
		{
			return 0;
		}
		else  // the line at the board is empty so that we calculate the point for it
		{
			if(board.getHorizontalLines()[horizontalIndex - 1][verticalIndex] == true)
			{
				point++;
			}
			if(board.getVerticalLines()[horizontalIndex - 1][verticalIndex] == true)
			{
				point++;
			}
			if(board.getVerticalLines()[horizontalIndex - 1][verticalIndex + 1] == true)
			{
				point++;
			}

		}

		return point;
		
	}
	

	/**
	 * This method calculates points for only left vertical lines whose vertical index is 0.
	 *
	 * @param horizontalIndex The horizontalIndex of the line.
	 * @param verticalIndex The horizontalIndex of the line.
	 * @return int The calculated points for the move.
	 */
	public int calculatePointForLeftVerticalLines (int horizontalIndex, int verticalIndex)
	{
		int point= 1;
		if(board.getVerticalLines()[horizontalIndex][verticalIndex] == true)
		{
			return 0;
		}
		else  // the line at the board is empty calculate the point
		{
			if(board.getVerticalLines()[horizontalIndex][verticalIndex + 1] == true)
			{
				point++;
			}
			if(board.getHorizontalLines()[horizontalIndex][verticalIndex] == true)
			{
				point++;
			}
			if(board.getHorizontalLines()[horizontalIndex +1][verticalIndex] == true)
			{
				point++;
			}

		}

		return point;
		
	}


	/**
	 * This method calculates points for only right vertical lines whose vertical index is 5.
	 *
	 * @param horizontalIndex The horizontalIndex of the line.
	 * @param verticalIndex The horizontalIndex of the line.
	 * @return int The calculated points for the move.
	 */
	public int calculatePointForRightVerticalLines (int horizontalIndex, int verticalIndex)
		{
			int point= 1;
			if(board.getVerticalLines()[horizontalIndex][verticalIndex] == true)
			{
				return 0;
			}
			else  // the line at the board is empty calculate the point
			{
				if(board.getVerticalLines()[horizontalIndex][verticalIndex - 1] == true)
				{
					point++;
				}
				if(board.getHorizontalLines()[horizontalIndex][verticalIndex-1] == true)
				{
					point++;
				}
				if(board.getHorizontalLines()[horizontalIndex +1][verticalIndex-1] == true)
				{
					point++;
				}

			}

			return point;
			
		}
		
		//This method calculates points for only top horizontal lines whose horizontal index is 0
		public int calculatePointForMiddleHorizontalLines (int horizontalIndex, int verticalIndex)
		{
		
			if(board.getHorizontalLines()[horizontalIndex][verticalIndex] == true)
			{
				return 0;
			}
			else  // the line at the board is empty calculate the point
			{
				int upperSquarePoint = calculatePointForBottomHorizontalLines(horizontalIndex, verticalIndex);
				int lowerSquarePoint = calculatePointForTopHorizontalLines(horizontalIndex, verticalIndex);
				if (upperSquarePoint > lowerSquarePoint)
				   return upperSquarePoint;
				else
				   return lowerSquarePoint;	

			}
			
		}
		
		//This method calculates points for only top horizontal lines whose horizontal index is 0
	/**
	 * This method calculates points for MiddleVerticalLines.
	 *
	 * @param horizontalIndex The horizontalIndex of the line.
	 * @param verticalIndex The horizontalIndex of the line.
	 * @return int The calculated points for the move.
	 */
		public int calculatePointForMiddleVerticalLines (int horizontalIndex, int verticalIndex)
		{
		
			if(board.getVerticalLines()[horizontalIndex][verticalIndex] == true)
			{
				return 0;
			}
			else  // the line at the board is empty calculate the point
			{
				int rightSquarePoint = calculatePointForLeftVerticalLines(horizontalIndex, verticalIndex);
				int leftSquarePoint = calculatePointForRightVerticalLines(horizontalIndex, verticalIndex);
				if (rightSquarePoint > leftSquarePoint)
				   return rightSquarePoint;
				else
				   return leftSquarePoint;	

			}

	
			
		}

	/**
	 * Checks if the game is finished by examining the state of horizontal and vertical lines on the board.
	 *
	 * @return true if the game is finished, false otherwise.
	 */
	public boolean isGameFinished()
		{
			
			for(int i=0;i<6;i++)
				for(int j=0;j<5;j++)
				{
					if (!board.getHorizontalLines()[i][j])
					{
						return false;
					}
				}
			
			for(int i=0;i<5;i++)
				for(int j=0;j<6;j++)
				{
					if (!board.getVerticalLines()[i][j])
					{
						return false;
					}			
			
				}
			return true;
			
		}



	/**
	 * Checks if there are only three-point lines left on the board.
	 *
	 * @return true if there are only three-point lines left, false otherwise.
	 */
	public boolean isOnlyThreePointLinesLeft()
		{
			
			for(int i=0;i<6;i++)
				for(int j=0;j<5;j++)
				{
					if (!board.getHorizontalLines()[i][j]  && calculatePoint("H", i, j) != 3)
					{
						return false;
					}
				}
			
			for(int i=0;i<5;i++)
				for(int j=0;j<6;j++)
				{
					if (!board.getVerticalLines()[i][j] && calculatePoint("V", i, j) != 3)
					{
						return false;
					}			
			
				}
			return true;
			
		}

	/**
	 * Gets the squares to draw for the GUI based on the move made.
	 *
	 * @param move The move made on the board.
	 * @return SquaresToDrawForUI object containing information about squares to draw and points earned.
	 */
	public SquaresToDrawForUI getSquaresToDrawForUI(Line move)
		{

			// the purpose of this method is to give the GUI information on where the lines that
			// form a square are located.
			SquaresToDrawForUI squaresToDrawForUI = new SquaresToDrawForUI();
			int point = calculatePoint(move.getHorizontalOrVertical(), move.getHorizontalIndex(), move.getVerticalIndex());
			if (point == 4) // we should create square to draw for the UI
			{
				squaresToDrawForUI.setSquare(true);
				squaresToDrawForUI.setLineToDraw(move);
				if(move.getHorizontalOrVertical().equalsIgnoreCase("H"))
				{
					if (move.getHorizontalIndex() == 0 )  // move is on the top horizontal line
					{
						squaresToDrawForUI.setCurrentMovePoint(1);
						Line[] lines = new Line[4];
						lines[0] = move;
						
						Line secondLine = new Line();
						secondLine.setHorizontalOrVertical("V");
						secondLine.setHorizontalIndex(move.getHorizontalIndex());
						secondLine.setVerticalIndex(move.getVerticalIndex()+1);
						lines[1] = secondLine;
						
						Line thirdLine = new Line();
						thirdLine.setHorizontalOrVertical("H");
						thirdLine.setHorizontalIndex(move.getHorizontalIndex()+1);
						thirdLine.setVerticalIndex(move.getVerticalIndex());
						lines[2] = thirdLine;
						
						Line fourthLine = new Line();
						fourthLine.setHorizontalOrVertical("V");
						fourthLine.setHorizontalIndex(move.getHorizontalIndex());
						fourthLine.setVerticalIndex(move.getVerticalIndex());
						lines[3] = fourthLine;
						
						squaresToDrawForUI.setFirstSquare(lines);
					}
					else if (move.getHorizontalIndex() == 5 )  // move is on the bottom horizontal line
					{
						squaresToDrawForUI.setCurrentMovePoint(1);
						Line[] lines = new Line[4];
						
						Line firstLine = new Line();
						firstLine.setHorizontalOrVertical("H");
						firstLine.setHorizontalIndex(move.getHorizontalIndex()-1);
						firstLine.setVerticalIndex(move.getVerticalIndex());
						lines[0] = firstLine;
						
						Line secondLine = new Line();
						secondLine.setHorizontalOrVertical("V");
						secondLine.setHorizontalIndex(move.getHorizontalIndex()-1);
						secondLine.setVerticalIndex(move.getVerticalIndex()+1);
						lines[1] = secondLine;
						
						Line thirdLine = new Line();
						lines[2] = move;
						
						Line fourthLine = new Line();
						fourthLine.setHorizontalOrVertical("V");
						fourthLine.setHorizontalIndex(move.getHorizontalIndex()-1);
						fourthLine.setVerticalIndex(move.getVerticalIndex());
						lines[3] = fourthLine;
						
						squaresToDrawForUI.setFirstSquare(lines);
					} // end of bottom line
					else // middle horizontal
					{
						int upperSquarePoint = calculatePointForBottomHorizontalLines(move.getHorizontalIndex(), move.getVerticalIndex());
						int lowerSquarePoint = calculatePointForTopHorizontalLines(move.getHorizontalIndex(), move.getVerticalIndex());
						if (upperSquarePoint == 4)
						{
							squaresToDrawForUI.setCurrentMovePoint(1);
							Line[] lines = new Line[4];
							
							Line firstLine = new Line();
							firstLine.setHorizontalOrVertical("H");
							firstLine.setHorizontalIndex(move.getHorizontalIndex()-1);
							firstLine.setVerticalIndex(move.getVerticalIndex());
							lines[0] = firstLine;
							
							Line secondLine = new Line();
							secondLine.setHorizontalOrVertical("V");
							secondLine.setHorizontalIndex(move.getHorizontalIndex()-1);
							secondLine.setVerticalIndex(move.getVerticalIndex()+1);
							lines[1] = secondLine;
							
							Line thirdLine = new Line();
							lines[2] = move;
							
							Line fourthLine = new Line();
							fourthLine.setHorizontalOrVertical("V");
							fourthLine.setHorizontalIndex(move.getHorizontalIndex()-1);
							fourthLine.setVerticalIndex(move.getVerticalIndex());
							lines[3] = fourthLine;
							
							squaresToDrawForUI.setFirstSquare(lines);
						}
						if (lowerSquarePoint == 4)
						{
							if (upperSquarePoint == 4)
							{
							   squaresToDrawForUI.setCurrentMovePoint(2);  // this move is 2 points
							}
							else
							{
								squaresToDrawForUI.setCurrentMovePoint(1);
							}
							
							Line[] lines = new Line[4];
							lines[0] = move;
							
							Line secondLine = new Line();
							secondLine.setHorizontalOrVertical("V");
							secondLine.setHorizontalIndex(move.getHorizontalIndex());
							secondLine.setVerticalIndex(move.getVerticalIndex()+1);
							lines[1] = secondLine;
							
							Line thirdLine = new Line();
							thirdLine.setHorizontalOrVertical("H");
							thirdLine.setHorizontalIndex(move.getHorizontalIndex()+1);
							thirdLine.setVerticalIndex(move.getVerticalIndex());
							lines[2] = thirdLine;
							
							Line fourthLine = new Line();
							fourthLine.setHorizontalOrVertical("V");
							fourthLine.setHorizontalIndex(move.getHorizontalIndex());
							fourthLine.setVerticalIndex(move.getVerticalIndex());
							lines[3] = fourthLine;

							//improtnat i changed this to setSecondSqaure
							squaresToDrawForUI.setSecondSquare(lines);

						}
						
						
					}
						
						
						
				} // end of H check
				else if(move.getHorizontalOrVertical().equalsIgnoreCase("V"))
				{
					if (move.getVerticalIndex() == 0 )  // move is on the left Vertical line
					{
						squaresToDrawForUI.setCurrentMovePoint(1);
						Line[] lines = new Line[4];
						
						Line firstLine = new Line();
						firstLine.setHorizontalOrVertical("H");
						firstLine.setHorizontalIndex(move.getHorizontalIndex());
						firstLine.setVerticalIndex(move.getVerticalIndex());
						lines[0] = firstLine;
												
						Line secondLine = new Line();
						secondLine.setHorizontalOrVertical("V");
						secondLine.setHorizontalIndex(move.getHorizontalIndex());
						secondLine.setVerticalIndex(move.getVerticalIndex()+1);
						lines[1] = secondLine;
						
						Line thirdLine = new Line();
						thirdLine.setHorizontalOrVertical("H");
						thirdLine.setHorizontalIndex(move.getHorizontalIndex()+1);
						thirdLine.setVerticalIndex(move.getVerticalIndex());
						lines[2] = thirdLine;
						
						lines[3] = move;
						
						squaresToDrawForUI.setFirstSquare(lines);
					}
					else if (move.getVerticalIndex() == 5 )  // move is on the right vertical line
					{
						squaresToDrawForUI.setCurrentMovePoint(1);
						Line[] lines = new Line[4];
						
						Line firstLine = new Line();
						firstLine.setHorizontalOrVertical("H");
						firstLine.setHorizontalIndex(move.getHorizontalIndex());
						firstLine.setVerticalIndex(move.getVerticalIndex()-1);
						lines[0] = firstLine;
						
						lines[1] = move;
						
						Line thirdLine = new Line();
						thirdLine.setHorizontalOrVertical("H");
						thirdLine.setHorizontalIndex(move.getHorizontalIndex()+1);
						thirdLine.setVerticalIndex(move.getVerticalIndex()-1);
						lines[2] = thirdLine;
						
						Line fourthLine = new Line();
						fourthLine.setHorizontalOrVertical("V");
						fourthLine.setHorizontalIndex(move.getHorizontalIndex());
						fourthLine.setVerticalIndex(move.getVerticalIndex()-1);
						lines[3] = fourthLine;
						
						squaresToDrawForUI.setFirstSquare(lines);
					} // end of right vertical
					else // middle vertical
					{
						int rightSquarePoint = calculatePointForLeftVerticalLines(move.getHorizontalIndex(), move.getVerticalIndex());
						int leftSquarePoint = calculatePointForRightVerticalLines(move.getHorizontalIndex(), move.getVerticalIndex());
		
						if (rightSquarePoint == 4)
						{
							squaresToDrawForUI.setCurrentMovePoint(1);
							Line[] lines = new Line[4];
							
							Line firstLine = new Line();
							firstLine.setHorizontalOrVertical("H");
							firstLine.setHorizontalIndex(move.getHorizontalIndex());
							firstLine.setVerticalIndex(move.getVerticalIndex());
							lines[0] = firstLine;
													
							Line secondLine = new Line();
							secondLine.setHorizontalOrVertical("V");
							secondLine.setHorizontalIndex(move.getHorizontalIndex());
							secondLine.setVerticalIndex(move.getVerticalIndex()+1);
							lines[1] = secondLine;
							
							Line thirdLine = new Line();
							thirdLine.setHorizontalOrVertical("H");
							thirdLine.setHorizontalIndex(move.getHorizontalIndex()+1);
							thirdLine.setVerticalIndex(move.getVerticalIndex());
							lines[2] = thirdLine;
							
							lines[3] = move;
							
							squaresToDrawForUI.setFirstSquare(lines);
						}
						if (leftSquarePoint == 4)
						{
							if (rightSquarePoint == 4)
							{
								squaresToDrawForUI.setCurrentMovePoint(2);	
							}
							else
							{
								squaresToDrawForUI.setCurrentMovePoint(1);	
							}
							Line[] lines = new Line[4];
							
							Line firstLine = new Line();
							firstLine.setHorizontalOrVertical("H");
							firstLine.setHorizontalIndex(move.getHorizontalIndex());
							firstLine.setVerticalIndex(move.getVerticalIndex()-1);
							lines[0] = firstLine;
							
							lines[1] = move;
							
							Line thirdLine = new Line();
							thirdLine.setHorizontalOrVertical("H");
							thirdLine.setHorizontalIndex(move.getHorizontalIndex()+1);
							thirdLine.setVerticalIndex(move.getVerticalIndex()-1);
							lines[2] = thirdLine;
							
							Line fourthLine = new Line();
							fourthLine.setHorizontalOrVertical("V");
							fourthLine.setHorizontalIndex(move.getHorizontalIndex());
							fourthLine.setVerticalIndex(move.getVerticalIndex()-1);
							lines[3] = fourthLine;

							//i changed this
							squaresToDrawForUI.setSecondSquare(lines);

						}
					}
				} // end of V check
			}  // endif point==4
			else  // only line no square and points
			{
				squaresToDrawForUI.setSquare(false);
				squaresToDrawForUI.setLineToDraw(move);
				
			}
			
			return squaresToDrawForUI;
		}

	/**
	 * Checks if two dots are adjacent to each other.
	 *
	 * @param startDot The starting dot.
	 * @param endDot   The ending dot.
	 * @return true if the dots are adjacent, false otherwise.
	 */
	public boolean areAdjacentDots(Dot startDot, Dot endDot) {
		int row1 = startDot.getRow();
		int col1 = startDot.getCol();
		int row2 = endDot.getRow();
		int col2 = endDot.getCol();

		return (Math.abs(row1 - row2) == 1 && col1 == col2) || (Math.abs(col1 - col2) == 1 && row1 == row2);
	}


	/**
	 * Converts a server index to a Line object representing the corresponding line on the game board.
	 *
	 * @param serverIndex The server index to be converted.
	 * @return A Line object representing the line corresponding to the server index.
	 */
	public Line getServerIndexToLine(int serverIndex) {
		Line line = new Line();
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

		line.setHorizontalOrVertical(hOrV);
		if(hOrV.equals("H")) {
			horizontalIndex = serverIndex/ 10;
			verticalIndex = (serverIndex % 10) - horizontalIndex;
		}
		else {
			horizontalIndex = serverIndex/ 11;
			verticalIndex =  (serverIndex % 11) - 5;
		}
		line.setHorizontalIndex(horizontalIndex);
		line.setVerticalIndex(verticalIndex);

		return line;

	}

	/**
	 * Gets the current game board.
	 *
	 * @return The current game board.
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * Creates a deep copy of the given game board.
	 *
	 * @param board The original game board to be copied.
	 * @return A new Board object representing a deep copy of the original board.
	 */
	public Board copyBoard(Board board)
	{
		Board copiedBoard = new Board(5);
		for(int i=0;i<6;i++)
			for(int j=0;j<5;j++)
			{
				copiedBoard.getHorizontalLines()[i][j] = board.getHorizontalLines()[i][j];
			}

		for(int i=0;i<5;i++)
			for(int j=0;j<6;j++)
			{
				copiedBoard.getVerticalLines()[i][j] = board.getVerticalLines()[i][j];
			}


		return copiedBoard;
	}


		
}