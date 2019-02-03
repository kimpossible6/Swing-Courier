
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.event.*;

/////////////////////extends Jcomponent ///////////////////////////////
public class InkPageContainer extends JComponent implements ChangeListener, ActionListener {
	/////////call functions
	InkBookFunctions cf1;
	int page_number;
	int width = 0, height = 0;
	private ActionListener listener;
	private InkPage pageContainer1;


	BufferedImage frontPage = null;
	BufferedImage backPage = null;
	BufferedImage frontPage2 = null;
	BufferedImage backPage2 = null;
	boolean pageForward = false;

	private boolean forwardrendering = false;
	private boolean backwardrendering = false;
	boolean pageBackward = false;


	boolean grabEnable = false;
	boolean grabEnable2 = false;
	BufferedImage frontPage3 = null;
	BufferedImage backPage3 = null;
	BufferedImage frontPage4 = null;
	BufferedImage backPage4 = null;

	int grabX = 0;
	int grabY = 0;


	int binderPos;
	public int turn_rate;
	public boolean swipeBack = false;
	public boolean swipeForward = false;
	//New change
	Timer timer = new Timer(1, this);
	public static int rectX = 0;
	public int rectY = 0;
	public static int rectX1 = 0;
	public int rectY1 = 0;
	public int velX = 2;
	public int velY = 2;
	public int pageturnvalue = 20;

	public InkPageContainer() {

		setModel(new InkBookFunctions());
		updateUI();
		this.setFocusable(true);

	}
	public void setModel(InkBookFunctions cf) {
		if (cf1 != null)  {
			cf1.removeChangeListener(this);
		}
		cf1 = cf;
		cf1.addChangeListener(this);
	}
	public InkBookFunctions getModel() {
		return cf1;
	}
	public void setUI(InkPage pageContainer1) {
		this.pageContainer1 = pageContainer1;
		super.setUI(pageContainer1);
	}

	public InkPage getUI() {
		return pageContainer1;
	}

	public void updateUI() {
		setUI((InkPage) new InkPage());
		invalidate();
	}

	public void setBackRender(boolean b) {
		this.backwardrendering = b;
		if (b != backwardrendering) {
			notify();
		}
	}
	public void setNextRender(boolean b) {
		this.forwardrendering = b;
		if (b != backwardrendering) {
			notify();
		}
	}
	public boolean getBackRender() {
		return backwardrendering;
	}
	public boolean getNextRender() {
		return forwardrendering;
	}


	public void addActionListener(ActionListener listener) {
		this.listener = listener;
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		if (InkBook.moveForward && !InkBook.moveBackward) {
			if (frontPage2 != null && backPage2 != null) {

				frontPage2 = frontPage2.getSubimage(0, 0, this.width, this.getHeight());
				backPage2 = backPage2.getSubimage(0, 0, Math.max(this.width - rectX1, 1), this.getHeight());
				g2.drawImage(frontPage2, 0 , rectY, this);
				g2.drawImage(backPage2, rectX1 , rectY, this);
				g2.setColor(Color.RED);
				g2.fillRect( rectX1 + 1, rectY,20, getHeight());
			}

			timer.start();
		}
		else if (InkBook.moveBackward && !InkBook.moveForward) {
			if (frontPage != null && backPage != null) {

				frontPage = frontPage.getSubimage( 0, 0, this.width,  this.getHeight());
				backPage = backPage.getSubimage(0, 0, Math.max(this.width - rectX, 1), this.getHeight());
				g2.drawImage(frontPage, 0,  rectY, this);
				g2.drawImage(backPage, rectX, rectY, this);
				g2.setColor(Color.BLUE);
				g2.fillRect( rectX + 1 , rectY,20, getHeight());
			}

			timer.start();
		}





	}

	@Override
	public void stateChanged(ChangeEvent e) {

		if(this.getWidth() > width) {
			width = this.getWidth();
		}
		if(this.getHeight() > height) {
			height = this.getHeight();
		}

		this.setMaximumSize(new Dimension(1000,1000));
		this.setMinimumSize(new Dimension(width,height));
		this.setPreferredSize(new Dimension(width,height));

		if (pageBackward && !pageForward) {
			System.out.println("this" + InkBook.backPage.page_number);
			System.out.println("previous" + InkBook.frontPage.page_number);
			InkBook.backPage.setVisible(false);
			InkBook.backPage.setEnabled(false);
			InkBook.frontPage.setVisible(true);
			InkBook.frontPage.setEnabled(true);
			InkBook.moveBackward = false;
			rectX = 0;
			frontPage = null;
			backPage = null;
			repaint();
			revalidate();
		}

		if (pageForward && !pageBackward) {
			System.out.println("this" + InkBook.backPage1.page_number);
			System.out.println("next" + InkBook.frontPage1.page_number);
			InkBook.backPage1.setVisible(false);
			InkBook.backPage1.setEnabled(false);
			InkBook.frontPage1.setVisible(true);
			InkBook.frontPage1.setEnabled(true);
			InkBook.moveForward = false;
			frontPage2 = null;
			backPage2 = null;
			rectX1 = 0;
			repaint();
			revalidate();
		}

		repaint();
	}


	///////////////////corresponding changes
	public static void setRadioChoice(String text) {
		InkBookFunctions.setRadioChoice(text);
	}
	public static void setTextChoice(String text) {
		InkBookFunctions.setTextChoice(text);
	}

	public int getMaxWidth() {
		return width;
	}

	public int getMaxHeight() {
		return height;
	}

	public void resized(int width, int height) {
		if(width == 0) {
			width = width;
		}
		if(height == 0) {
			height = height;
		}

		stateChanged(new ChangeEvent(this));
	}

	public void setColor(Color newColor) {
		cf1.setColor(newColor);
	}
	// public Color getColor() {
	// 	return cf1.getColor();
	// }

	public String getGesture() {
		return cf1.knowGesture();
	}


	//aborted red and yellow
	public void fireGestureEvent() {
		String gesture = getGesture();
		if(gesture.equals("Delete")) {
			cf1.deleteDrawing();
		} else if(gesture.equals("Select")) {
			cf1.getStuffFromCorp();
			cf1.setIsSelect(true);
		} else if (gesture.equals("Red")) {
			cf1.setColor(Color.RED);
		} else if (gesture.equals("Yellow")) {
			cf1.setColor(Color.YELLOW);
		}
		listener.actionPerformed(new ActionEvent(this,1, gesture));
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if (InkBook.moveForward && !InkBook.moveBackward) {

			if (rectX1 < this.width - 2 && rectX1 >= 0) {
				rectX1 += velX;
				repaint();
			} else {
				rectX1 = 0;
				System.out.println("stop" + rectX1);

				timer.stop();
				pageForward = false;
				pageBackward = false;
				if (!timer.isRunning()) {
					InkBook.moveForward= false;
					stateChanged(new ChangeEvent(this));
					System.out.println("stop" + rectX + ":" + this.width);
				}
				repaint();

			}
		}
//		System.out.println(pageBackward + "pagebackword");
		if(InkBook.moveBackward && !InkBook.moveForward) {
			if (rectX < this.width) {
//				System.out.println("decrease");
				rectX += velX;

//				if (Math.max(this.width - rectX + 1, 22) + 1 > this.width) {
//					System.out.println("Outside raster");
//					System.out.println("start" + (this.width - rectX));
//					System.out.println("width" + this.rectX);
//					System.out.println ("draw image from" + (this.width - rectX));
//				}
//
//
//				System.out.println("increase" + this.width);
//				System.out.println("rectx"+Math.max(this.width - rectX, 1));
				repaint();
			} else {
//				System.out.println("increase" + this.width);
				rectX = 0;
				System.out.println("stop" + rectX + ":" + this.width);
				timer.stop();
				pageBackward = false;
				pageForward = false;
				if (!timer.isRunning()) {
					InkBook.moveBackward = false;
					backwardrendering = true;
					System.out.println("stop" + rectX + ":" + this.width);
					stateChanged(new ChangeEvent(this));
				}
				repaint();

			}
		}


	}


}