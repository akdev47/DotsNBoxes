package BoardDrawingGame.logic;

import BoardDrawingGame.Entities.Board;
import BoardDrawingGame.view.Dot;
import BoardDrawingGame.view.Line;
import BoardDrawingGame.view.SquaresToDrawForUI;

public class Utility {
	private Board board;
	
	public Utility (Board board)
	{
		this.board = board;
	}
	
	int calculatePoint (String horizontalOrVertical,int horizontalIndex, int verticalIndex)

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
	
	//This method calculates points for only top horizontal lines whose horizontal index is 0
	int calculatePointForTopHorizontalLines (int horizontalIndex, int verticalIndex)
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

	//This method calculates points for only top horizontal lines whose horizontal index is 0
	int calculatePointForBottomHorizontalLines (int horizontalIndex, int verticalIndex)
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
	
	//This method calculates points for only left vertical lines whose vertical index is 0
	int calculatePointForLeftVerticalLines (int horizontalIndex, int verticalIndex)
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

	//This method calculates points for only right vertical lines whose vertical index is 5
		int calculatePointForRightVerticalLines (int horizontalIndex, int verticalIndex)
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
		int calculatePointForMiddleHorizontalLines (int horizontalIndex, int verticalIndex)
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
		int calculatePointForMiddleVerticalLines (int horizontalIndex, int verticalIndex)
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
		
		boolean isGameFinished()
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

		boolean isOnlyThreePointLinesLeft()
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
		
		SquaresToDrawForUI getSquaresToDrawForUI(Line move)
		{
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

	public boolean areAdjacentDots(Dot startDot, Dot endDot) {
		int row1 = startDot.getRow();
		int col1 = startDot.getCol();
		int row2 = endDot.getRow();
		int col2 = endDot.getCol();

		return (Math.abs(row1 - row2) == 1 && col1 == col2) || (Math.abs(col1 - col2) == 1 && row1 == row2);
	}



		
}