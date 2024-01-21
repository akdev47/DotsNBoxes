package BoardDrawingGame.view;

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
	
	public String getHorizontalOrVertical() {
		return horizontalOrVertical;
	}
	public void setHorizontalOrVertical(String horizontalOrVertical) {
		this.horizontalOrVertical = horizontalOrVertical;
	}
	public int getHorizontalIndex() {
		return horizontalIndex;
	}
	public void setHorizontalIndex(int horizontalIndex) {
		this.horizontalIndex = horizontalIndex;
	}
	public int getVerticalIndex() {
		return verticalIndex;
	}
	public void setVerticalIndex(int verticalIndex) {
		this.verticalIndex = verticalIndex;
	}
	
	
}
