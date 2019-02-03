public class point{
	private int x;
	private int y;

	public point(int x, int y) {
		this.x = x; 
		this.y = y;
	}

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
			return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	public String directionVector(point prev) {
		StringBuffer direction = new StringBuffer();
		if(x == prev.x && y < prev.y) {
			direction.append("N");
		} else if(x > prev.getX() && y < prev.y) {
			 direction.append("B");
		} else if(x > prev.getX() && y == prev.y) {
			direction.append("E");
		} else if(x > prev.getX() && y > prev.y) {
			direction.append("C");
		} else if(x == prev.getX() && y > prev.y) {
			direction.append("S");
		} else if(x < prev.getX() && y > prev.y) {
			direction.append("D");
		} else if(x < prev.getX() && y == prev.y) {
			direction.append("W");
		} else if(x < prev.getX() && y < prev.y) {
			direction.append("A");
		}
		return direction.toString();
	}
}