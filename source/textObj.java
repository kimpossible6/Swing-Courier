import java.awt.*;
import javax.swing.*;
public class textObj {
	// point p1; 
	double x;
	double y;
	String text;
	Color color;
	Font font;
	Rectangle rect;
	boolean selected;

	public textObj(double x, double y) {
		this.x = x;
		this.y = y;
		this.text = "";
	}
	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	public void setText(char text) {
		this.text += text;
	} 
	public String getText() {
		return this.text;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	public void setRectangle(Rectangle rect) {
		this.rect = rect;
	}
	public Rectangle getRectangle() {
		return rect;
	}

	public void setSelected(boolean b) {
		selected  = b;
	}
	
	public boolean getSelected() {
		return selected;
	}

}
