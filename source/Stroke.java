import java.awt.*;
import java.util.ArrayList;
import java.util.List;


//stroke object used for all inks 
public class Stroke {

	private int x, y, x2, y2;
	private Color color;
	private String choice;
	private List<point> p1;
	private boolean isSelect;
	public Stroke(int x, int y, int x2, int y2, String choice, Color color) {
		this.x = x;
		this.y = y;
		this.x2 = x2;
		this.y2 = y2;
		this.choice = choice;
		this.color = color;
		p1 = new ArrayList<point>();
		isSelect = false;
		
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

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}

	public String getChoice() {
		return choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
	}

	public Color getColor() {
		return color;
	}
	
	public void addPoint(point p) {
		p1.add(p);
	}
	
	public List<point> getPoints() {
		return p1;
	}
	
	public boolean getSelected() {
		return isSelect;
	}

	public void setSelected(boolean s) {
		isSelect = s;
	}
	
}