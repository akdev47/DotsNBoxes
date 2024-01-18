package BoardDrawingGame.src;

public class Line {

	private String horizontalOrVertical;
	private int horizontalIndex;
	private int verticalIndex;
	
	public Line()
	{
	}
	
	public Line(String horizontalOrVertical,int horizontalIndex, int verticalIndex)
	{
		this.setHorizontalOrVertical(horizontalOrVertical);
		this.setHorizontalIndex(horizontalIndex);
		this.setVerticalIndex(verticalIndex);
	}
	
	String getHorizontalOrVertical() {
		return horizontalOrVertical;
	}
	void setHorizontalOrVertical(String horizontalOrVertical) {
		this.horizontalOrVertical = horizontalOrVertical;
	}
	int getHorizontalIndex() {
		return horizontalIndex;
	}
	void setHorizontalIndex(int horizontalIndex) {
		this.horizontalIndex = horizontalIndex;
	}
	int getVerticalIndex() {
		return verticalIndex;
	}
	void setVerticalIndex(int verticalIndex) {
		this.verticalIndex = verticalIndex;
	}
	
	
}
