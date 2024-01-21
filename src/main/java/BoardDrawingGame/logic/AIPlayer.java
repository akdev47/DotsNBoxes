package BoardDrawingGame.logic;

import BoardDrawingGame.Entities.Board;
import BoardDrawingGame.Entities.Player;
import BoardDrawingGame.logic.Utility;
import BoardDrawingGame.view.Line;
import java.util.Random;
import javafx.scene.paint.Color;

public class AIPlayer extends Player {
	private final Board board;
	private final Utility utility;

	private final Color playerColor;

	public AIPlayer (Board board, Utility utility, Color playerColor)
	{
		this.board = board;
		this.utility = utility;
		this.playerColor = playerColor;
	}	

	public Color getPlayerColor() {
		return playerColor;
	}
	public Line getNextMove()
	{
		// First try to make the move for point
		Line moveResult=getNextMoveForPoint();
		if(moveResult != null)
		{
			return moveResult;
		}
		// if move for points is not possible then first select a random line which does not form 3 sides
		else 
		{
			
			moveResult = getNextMoveRandomNotThreeSides();
			if(moveResult != null)
			{
				return moveResult;
			}
			else
			{
				moveResult = getNextMoveRandom();   // select random from empty lines
			}
		}
		return moveResult;
	}
	
	Line getNextMoveForPoint()
	{
		Line moveResult=null;
		for(int i=0;i<6;i++)
			for(int j=0;j<5;j++)
			{
				if (utility.calculatePoint("H", i, j) == 4)
				{
					moveResult = new Line("H",i,j);
				    return moveResult;
				}
			}
		
		for(int i=0;i<5;i++)
			for(int j=0;j<6;j++)
			{
				if (utility.calculatePoint("V", i, j) == 4)
				{
					moveResult = new Line("V",i,j);
				    return moveResult;
				}			
			}
		
		return moveResult;
	}
	
	Line getNextMoveRandomNotThreeSides()
	{
		Line moveResult=null;
		
		
		
		while(!utility.isOnlyThreePointLinesLeft())
		{
			Random rand = new Random(); 
			int i = rand.nextInt(6);
			int j = rand.nextInt(5);
			int hOrV = rand.nextInt(2);
		
			if(hOrV == 0)  // H
			{
				if(utility.calculatePoint("H", i, j) == 1  || utility.calculatePoint("H", i, j) == 2)
				{
					moveResult = new Line();
					moveResult.setHorizontalOrVertical("H");
					moveResult.setHorizontalIndex(i);
					moveResult.setVerticalIndex(j);
					return moveResult;
				}
			}
			else  // V
			{
				if(utility.calculatePoint("V", j, i) == 1 || utility.calculatePoint("V", j, i) == 2  )
				{
					moveResult = new Line();
					moveResult.setHorizontalOrVertical("V");
					moveResult.setHorizontalIndex(j);
					moveResult.setVerticalIndex(i);
					return moveResult;
				}
			}
		}
		
		return moveResult;
	}

	Line getNextMoveRandom()
	{
		Line moveResult=null;
		
		
		
		while(utility.isGameFinished() != true)
		{
			Random rand = new Random(); 
			int i = rand.nextInt(6);
			int j = rand.nextInt(5);
			int hOrV = rand.nextInt(2);
		
			if(hOrV == 0)  // H
			{
				if(!board.getHorizontalLines()[i][j])
				{
					moveResult = new Line();
					moveResult.setHorizontalOrVertical("H");
					moveResult.setHorizontalIndex(i);
					moveResult.setVerticalIndex(j);
					return moveResult;
				}
			}
			else  // V
			{
				if(!board.getVerticalLines()[j][i])
				{
					moveResult = new Line();
					moveResult.setHorizontalOrVertical("V");
					moveResult.setHorizontalIndex(j);
					moveResult.setVerticalIndex(i);
					return moveResult;
				}
			}
		}
		
		return null;
	}

}
