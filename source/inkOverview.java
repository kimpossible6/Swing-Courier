public class inkOverview {
	private int x;
	private int y;
	private int width;
	private int height;
	private double xMotion;
	private double yMotion;
	public inkOverview(int x, int y, int width,
									int height, double xMotion, double yMotion) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.xMotion = xMotion;
		this.yMotion = yMotion;
	}
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}
	public double getXMotion() {
		return this.xMotion;
	}
	public double getYMotion() {
		return this.yMotion;
	}
}
