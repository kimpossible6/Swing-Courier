import javax.swing.event.*;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;




public class InkBookFunctions {
	private int xStart, yStart;
	private int xCurrent, yCurrent;
	///////Stroke///////////////////////////////////////////
	private List<Stroke> strokes = new ArrayList<Stroke>();
	private Stroke s1;
	private boolean isDraw;
	public static String choice = "Ink";
	///////text////////////////////////////////////////////
	private List<textObj> texts = new ArrayList<textObj>();
	private textObj text1;
	public static String isTyping = "";
	private boolean isType;

	///////////recognizer/////////////////
	private Recognizer r1 = new Recognizer();
	private boolean isGesture;
	private String g1;
	///////////selection box/////////////////
	private Rectangle corp;
	private boolean isSelect;

	private ChangeListener listener;
	private Color color1 = Color.BLUE;
	
	public void setXCurrent(int x) {
		xCurrent = x;
		listener.stateChanged(new ChangeEvent(strokes));
	}

	public void setYCurrent(int y) {
		yCurrent = y;
		listener.stateChanged(new ChangeEvent(strokes));
	}


	public void setXStart(int x) {
		xStart = x;
	}


	public void setYStart(int y) {
		yStart = y;
	}

	////////////////Stroke////////////////////////////////////
	public void setStroke(int x2, int y2) {
		if(choice.equals("Ink") && s1 != null) {
			s1.setX2(x2);
			s1.setY2(y2);
			strokes.add(s1);
			s1 = null;
		} else {
			strokes.add(new Stroke(xStart, yStart, x2, y2, choice,color1));
		}
		
		listener.stateChanged(new ChangeEvent(strokes));
	}

	public boolean getInkState() {
		if (choice.equals("Ink")) {
			if(s1 != null) {
				addPoint(s1.getX2(), s1.getY2());
				s1.setX2(xCurrent);
				s1.setY2(yCurrent);
			} else {
				s1 = new Stroke(xStart, yStart, xCurrent, yCurrent, choice, color1);
			}
			
			return true;
		}
		return false;
	}

	private void addPoint(int xCurrent2, int yCurrent2) {
		s1.addPoint(new point(xCurrent2, yCurrent2));
		
	}

	public List<Stroke> getStrokes() {
		return strokes;
	}

	public Stroke getDrawStroke() {
		Stroke s1current = new Stroke(xStart, yStart, xCurrent, yCurrent, choice, color1);
		if (isDraw) {
			if(choice.equals("Ink")) {
				return s1;
			} else {
				return s1current;
			}
			
		} else {
			return null;
		}

	}
	/////////////////////////for radio button 
	public static void setRadioChoice(String text) {
		choice = text;
	}
	public static void setTextChoice(String text) {
		isTyping = text;
	}

	public String getTextChoice() {
		return isTyping;
	}
	public void isDrawing(boolean draw) {
		isDraw = draw;
	}

	public void isType(boolean type) {
		isType = type;
	}

	public List<textObj> getTextList() {
		return texts;
	}

	////////////set Color
	public void setColor(Color color) {
		color1 = color;
	}
	
	public Color getColor() {
		return color1;
	}


	public void setIsGesture(boolean b) {
		isGesture = b;
		
	}


	public boolean getIsGesture() {

		return isGesture;
	}

	public boolean getGesture() {
	
		g1 = r1.getGesture(s1.getPoints());
		if(g1 == null) {
			return false;
		}
		if(g1.equals("Next")) {
			listener.stateChanged(new ChangeEvent(g1));
		} else if (g1.equals("Red")) {
			listener.stateChanged(new ChangeEvent(g1));
		} else if (g1.equals("Yellow")) {
			listener.stateChanged(new ChangeEvent(g1));
		}
		return true;
	}

	/////////////////////////stop ///////////////////
	public void resetStroke() {
		s1 = null;
		
	}
	
	//get string gestures 
	public String knowGesture() {
		return g1;
	}

	///corp the objects
	private Rectangle corpBoundary(Stroke ink1) {
		int minX = 1000, maxX = 0, minY = 1000, maxY = 0;
		List<point> points = ink1.getPoints();
		for(int i = 0; i < points.size(); i++) {
			if(points.get(i).getX() < minX) {
				minX = points.get(i).getX();
			}
			if(points.get(i).getX() > maxX) {
				maxX = points.get(i).getX();
			}
			if(points.get(i).getY() < minY) {
				minY = points.get(i).getY();
			}
			if(points.get(i).getY() > maxY) {
				maxY = points.get(i).getY();
			}
		}
		return new Rectangle(minX,minY,(maxX-minX),(maxY-minY));
	}

	//corp objects want to remove ---> for shapes 
	private void corpRemovingShapes(Rectangle rect) {
		List<Stroke> strokeDel = new ArrayList<Stroke>();
		List<textObj> textDel = new ArrayList<textObj>();
		for(int i = 0; i < strokes.size(); i++) {
			Stroke stroke2 = strokes.get(i);
			if(stroke2.getChoice().equals("Ink")) {
				Rectangle corpBox = corpBoundary(stroke2);
				if(rect.contains(corpBox)) {
					strokeDel.add(stroke2);
				}
			} else {

				int bX, bY, bHeight, bWidth;
				if(stroke2.getX() < stroke2.getX2()) {
					bX = stroke2.getX();
				} else {
					bX = stroke2.getX2();
				}
				if(stroke2.getY() < stroke2.getY2()) {
					bY = stroke2.getY();
				} else {
					bY = stroke2.getY2();
				}
				bWidth = Math.abs(stroke2.getX() - stroke2.getX2());
				bHeight = Math.abs(stroke2.getY() - stroke2.getY2());
				Rectangle box1 = new Rectangle(bX,bY,bWidth,bHeight);
				if(rect.contains(box1)) {
					strokeDel.add(stroke2);
				}
			}
		}
		
		for(int i = 0; i < texts.size(); i++) {
			textObj text = texts.get(i);
			int textWidth = (int) text.getRectangle().getWidth();
			int textHeight = (int) text.getRectangle().getHeight();
			int startX = (int) text.getRectangle().getX();
			int startY = (int) text.getRectangle().getY();
			Rectangle box2 = new Rectangle(startX, startY, textWidth,textHeight);
			if(rect.contains(box2)) {
				textDel.add(texts.get(i));
			}
		}
		
		for(int i = 0; i < strokeDel.size(); i++) {
			strokes.remove(strokeDel.get(i));
		}
		
		for(int i = 0; i < textDel.size(); i++) {
			while(texts.remove(textDel.get(i)));
		}
	}
	
	
	public void deleteDrawing() {
		Rectangle r1 = corpBoundary(s1);
		corpRemovingShapes(r1);
		
	}

	//check if selected 
	public void setIsSelect(boolean b) {
		isSelect = b;
		
	}

	//get if selected 
	public boolean getIsSelect() {
		
		return isSelect;
	}



	//get boundary 
	public Rectangle getSelectionBounds() {
		return corp;
	}

	
	//selected all 
	private void SelectAllFromCorp(Rectangle rect) {
		corp = rect;
		for(int i = 0; i < strokes.size(); i++) {
			Stroke stroke2 = strokes.get(i);
			if(stroke2.getChoice().equals("Ink")) {
				Rectangle corpBox = corpBoundary(stroke2);
				if(rect.contains(corpBox)) {
					// g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
					// Color myColour = new Color(255, value,value );
            		// stroke2.setColor(myColour);
					stroke2.setSelected(true);
				}
			} else {
				int bX, bY, bHeight, bWidth;
				if(stroke2.getX() < stroke2.getX2()) {
					bX = stroke2.getX();
				} else {
					bX = stroke2.getX2();
				}
				if(stroke2.getY() < stroke2.getY2()) {
					bY = stroke2.getY();
				} else {
					bY = stroke2.getY2();
				}
				bWidth = Math.abs(stroke2.getX() - stroke2.getX2());
				bHeight = Math.abs(stroke2.getY() - stroke2.getY2());
				Rectangle box1 = new Rectangle(bX,bY,bWidth,bHeight);
				if(rect.contains(box1)) {
					stroke2.setSelected(true);
				}
			}
		}
		
		for(int j = 0; j < texts.size(); j++) {
			textObj text = texts.get(j);
			int textWidth = (int) text.getRectangle().getWidth();
			int textHeight = (int) text.getRectangle().getHeight();
			int startX = (int) text.getX();
			int startY = (int) text.getY();
			Rectangle box2 = new Rectangle(startX, startY, textWidth,textHeight);
			if(rect.contains(box2)) {
				text.setSelected(true);
			}
		}
	}

	//move the corpBox
	public void moveCorp(int x, int y) {
		for(int i = 0; i < strokes.size(); i++) {
			Stroke stroke2 = strokes.get(i);
			if(stroke2.getSelected()) {
				if(stroke2.getChoice().equals("Ink")) {
					for(int j = 0; j < stroke2.getPoints().size(); j++) {
						point p = stroke2.getPoints().get(j);
						p.setX(p.getX() + x);
						p.setY(p.getY() + y);
					}
					stroke2.setX2(stroke2.getX2() + x);
					stroke2.setY2(stroke2.getY2() + y);
				} else {
					stroke2.setX(stroke2.getX() + x);
					stroke2.setY(stroke2.getY() + y);
					stroke2.setX2(stroke2.getX2() + x);
					stroke2.setY2(stroke2.getY2() + y);
				}
			}
		}
		for(int j = 0; j < texts.size(); j++) {
			textObj text1 = texts.get(j);
			if(text1.getSelected()) {
				text1.setX(text1.getX() + x * 1.0);
				text1.setY(text1.getY() + y * 1.0);
			}
		}
		corp.x = corp.x + x;
		corp.y = corp.y + y;
	}

	// get everything from corped rectangle
	public void getStuffFromCorp() {
		Rectangle r1 = corpBoundary(s1);
		SelectAllFromCorp(r1);
		
	}

	//deselect the box 
	public void deselect() {
		for(int i = 0; i < strokes.size(); i++) {
			strokes.get(i).setSelected(false);
		}
		for(int j = 0; j < texts.size(); j++) {
			texts.get(j).setSelected(false);
		}
	}

	public void removeChangeListener(ChangeListener listener) {
		this.listener = null;
	}

	public void addChangeListener(ChangeListener listener) {
		this.listener = listener;
	}


}