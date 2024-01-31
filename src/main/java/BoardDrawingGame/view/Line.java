package BoardDrawingGame.view;

/**
 * Represents a line on the game board.
 */
public class Line {

	private String horizontalOrVertical;
	private int horizontalIndex;
	private int verticalIndex;

	/**
	 * Default constructor for Line.
	 */
	public Line()
	{
	}

	/**
	 * Constructs a Line with specified attributes.
	 *
	 * @param horizontalOrVertical The orientation of the line, either "H" (horizontal) or "V" (vertical).
	 * @param horizontalIndex      The horizontal index of the line.
	 * @param verticalIndex        The vertical index of the line.
	 */
	public Line(String horizontalOrVertical,int horizontalIndex, int verticalIndex)
	{
		this.setHorizontalOrVertical(horizontalOrVertical);
		this.setHorizontalIndex(horizontalIndex);
		this.setVerticalIndex(verticalIndex);
	}

	/**
	 * Gets the orientation of the line.
	 *
	 * @return The orientation, either "H" (horizontal) or "V" (vertical).
	 */
	public String getHorizontalOrVertical() {
		return horizontalOrVertical;
	}

	/**
	 * Sets the orientation of the line.
	 *
	 * @param horizontalOrVertical The orientation to set, either "H" (horizontal) or "V" (vertical).
	 */
	public void setHorizontalOrVertical(String horizontalOrVertical) {
		this.horizontalOrVertical = horizontalOrVertical;
	}

	/**
	 * Gets the horizontal index of the line.
	 *
	 * @return The horizontal index.
	 */
	public int getHorizontalIndex() {
		return horizontalIndex;
	}

	/**
	 * Sets the horizontal index of the line.
	 *
	 * @param horizontalIndex The horizontal index to set.
	 */
	public void setHorizontalIndex(int horizontalIndex) {
		this.horizontalIndex = horizontalIndex;
	}

	/**
	 * Gets the vertical index of the line.
	 *
	 * @return The vertical index.
	 */
	public int getVerticalIndex() {
		return verticalIndex;
	}

	/**
	 * Sets the vertical index of the line.
	 *
	 * @param verticalIndex The vertical index to set.
	 */
	public void setVerticalIndex(int verticalIndex) {
		this.verticalIndex = verticalIndex;
	}

	/**
	 * Gets the move index for server communication.
	 *
	 * @return The move index for server.
	 */
	public int getMoveIndexForServer() {
		if(getHorizontalOrVertical().equals("H")) {
			return (getHorizontalIndex() * 10) + getVerticalIndex() + getHorizontalIndex();
		}
		else {
			return (getHorizontalIndex()*11) + getVerticalIndex() + 5;
		}
	}


	
	
}
