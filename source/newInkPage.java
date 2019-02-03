import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.*;
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.net.URL;
import java.awt.geom.Rectangle2D;
import java.awt.font.GlyphVector;
import java.awt.font.FontRenderContext;
public class newInkPage extends JComponent implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
    private boolean isRect, isOval, isInk, isText;
    private boolean drawRect, drawOval, drawfreeInk;
    private int page_Num;
    private int xStart, yStart;
    private int xPrevious, yPrevious;
    private int xCurrent, yCurrent;
    private int xPos, yPos, width, height;
    private int dTop, dLeft, dR, dBottom;
    private FontMetrics fontM;

    private int turnStartAt;
    private int turnStopAt;

    private ArrayList<Integer> pX2, pY2;
    private ArrayList<Stroke> stoke;
    private ArrayList<shapeOval> ovals;
    private ArrayList<Rectangle> rects;

    private ArrayList<Rectangle> MRects;
    private ArrayList<shapeOval> MOvals;
    private ArrayList<Stroke> MStrokes;

    boolean mouseText;
    private int mx, my, mx1, my1;
    private ArrayList<textObj> sticky = new ArrayList<>();
    private textObj currentP;

    private Rectangle textbox;
    private ArrayList<Rectangle> textboxes;
    private Point textpoint;


    private BufferedImage topPage, bottomPage;
    private BufferedImage leftPage, rightPage;

    private BufferedImage subTopPage, subBottomPage, turnPage;
    private boolean isRightClick;
    private boolean isDelete;
    private boolean isMove;
    private boolean isInMoving;
    private boolean flipped;
    private boolean forward;
    private boolean isPageFlipping;
    private boolean overview;


    private boolean goForward;
    private boolean goBackward;

    private Recognizer gestureMatch;
    /////GESTURE

    private Timer timer;
    private static int pageDivider = 15;
    private static int flipping_speed  = 55;

    private String gestures;
    private Timer timerG;

    public newInkPage(int page_Num) {
        setPreferredSize(new Dimension(400, 600));
        this.page_Num = page_Num;
        pX2 = new ArrayList();
        pY2 = new ArrayList();

        /////////general/////////////////////////////////////////
        stoke = new ArrayList();
        rects = new ArrayList();
        ovals = new ArrayList();

        ////////////when moving//////////////////////////////////////
        MRects = new ArrayList();
        MOvals = new ArrayList();
        MStrokes = new ArrayList();

        goForward = false;
        goBackward = false;
        gestures = "";
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (flipped) {
            if (forward) {
                subTopPage = topPage.getSubimage(0, 0, turnStartAt - pageDivider, this.getHeight());
                  if (isPageFlipping) {
                      subBottomPage = rightPage.getSubimage((turnStartAt + pageDivider), 0, this.getWidth() - (turnStartAt + pageDivider), this.getHeight());
                  } else {
                      subBottomPage = bottomPage.getSubimage((turnStartAt + pageDivider), 0, this.getWidth() - (turnStartAt + pageDivider), this.getHeight());
                  }

                g2.drawImage(subTopPage, 0, 0, this);
                g2.drawImage(turnPage, turnStartAt - pageDivider, 0, 2 * pageDivider, this.getHeight(), this);
                g2.drawImage(subBottomPage, turnStartAt + pageDivider, 0, this);
            } else {
                subTopPage = topPage.getSubimage((turnStartAt + pageDivider), 0, this.getWidth() - (turnStartAt + pageDivider), this.getHeight());
                if (isPageFlipping) {
                    subBottomPage = leftPage.getSubimage(0, 0, turnStartAt - pageDivider, this.getHeight());
                } else {
                    subBottomPage = bottomPage.getSubimage(0, 0, turnStartAt - pageDivider, this.getHeight());
                }
                g2.drawImage(subBottomPage, 0, 0, this);
                g2.drawImage(turnPage, turnStartAt - pageDivider, 0, 2 * pageDivider, this.getHeight(), this);
                g2.drawImage(subTopPage, turnStartAt + pageDivider, 0, this);
            }
        } else {
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for(int hoz = 0; hoz < this.getHeight(); hoz += 40) {
                g2.setColor(Color.BLUE);
                g2.drawLine(0, hoz, this.getWidth(), hoz);
            }

            Font font1 = new Font("SansSerif",Font.BOLD,14);
            g2.setFont(font1);
            g2.drawString("" + (page_Num), this.getWidth() - 30, 20);

            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.BLACK);

            //all the lines
            for (Stroke stroke: stoke) {
                g2.drawPolyline(stroke.getpX(), stroke.getpY(), stroke.getSize());
            }

            for (Rectangle r: rects) {
                g2.drawRect((int)r.getX(), (int)r.getY(), (int)r.getWidth(), (int)r.getHeight());
            }

            for (shapeOval o: ovals) {
                g2.drawOval(o.getX(), o.getY(), o.getWidth(), o.getHeight());
            }

            g2.setColor(Color.RED);

            for (Stroke stroke: MStrokes) {
                g2.drawPolyline(stroke.getpX(), stroke.getpY(), stroke.getSize());
            }

            for (Rectangle r: MRects) {
                g2.drawRect((int)r.getX(), (int)r.getY(), (int)r.getWidth(), (int)r.getHeight());
            }
            //all the moving ovals
            for (shapeOval o: MOvals) {
                g2.drawOval(o.getX(), o.getY(), o.getWidth(), o.getHeight());
            }


            for(textObj t: sticky){
    		height = fontM.getAscent() + fontM.getDescent() + fontM.getLeading();
    		int x = (int)t.x;
    		int y = (int)t.y;
    		int width=0;
    		int nIndex = 0;
    		String textTodraw = t.getText();
    		if ((t.getRectangle()).getHeight() < height - 1) {
				int rx = (int)t.getRectangle().getX();
				int ry = (int)t.getRectangle().getY();
				int rw = (int)t.getRectangle().getWidth();
				Rectangle newbox = new Rectangle(rx, ry, rw, (int)(t.getRectangle()).getHeight() + height);
				t.setRectangle(newbox);
    		}

			int rx = (int)t.getRectangle().getX();
			int ry = (int)t.getRectangle().getY();
			int rw = (int)t.getRectangle().getWidth();
			int rh = (int)t.getRectangle().getHeight();
			g2.setColor(Color.YELLOW);
			g2.fillRect(rx, ry, rw, rh);
			g2.setColor(t.color);
    		if (textTodraw.length() < 16) {
    			System.out.println(textTodraw);
    			g2.drawString(textTodraw, x, y);
    			System.out.println(fontM.stringWidth(textTodraw));
    		} else {
    			Rectangle2D bounds = fontM.getStringBounds("Hello World this is fantastic", g2);
    			for (int i = 0; i < textTodraw.length()/16; i++) {

    				String currentLine = textTodraw.substring(16 * i, 16 * (i + 1)) + '-';
    				g2.drawString(currentLine, x, y);
    				y += fontM.getHeight();
    				System.out.println("new rec" + (t.getRectangle()).getHeight());
    				System.out.println("new rec" + (int)bounds.getHeight() * i);
    				if ((t.getRectangle()).getHeight() < (int)bounds.getHeight() * i + 40) {
    					int rx2 = (int)t.getRectangle().getX();
						int ry2 = (int)t.getRectangle().getY();
						int rw2 = (int)t.getRectangle().getWidth();
						int start = (int)t.getRectangle().getY() + (int)bounds.getHeight();
						Rectangle newbox = new Rectangle(rx2, ry2, rw2, (int)(t.getRectangle()).getHeight() + (int)bounds.getHeight() + 3);
						t.setRectangle(newbox);
						g2.fillRect(rx2, start, rw2, (int)(t.getRectangle()).getHeight() + 3);
						System.out.println("new rec");
    				}
    			}
    		}
      }
            g2.setColor(Color.BLACK);


            int[] pX = new int[pX2.size()];
            int[] pY = new int[pY2.size()];
            for (int i = 0; i < pX2.size(); i++) {
                pX[i] = pX2.get(i);
                pY[i] = pY2.get(i);
            }
            if (isRightClick) {
              g2.setColor(Color.RED);
            }
            g2.drawPolyline(pX, pY, pX2.size());
            g2.setColor(Color.BLACK);

            /////////////////////////////////////////////////////////////////
            if(drawfreeInk) {
                if(!isRightClick) {
                    Stroke tempStroke = new Stroke(dTop, dBottom, dLeft, dR, pX2, pY2);
                    stoke.add(tempStroke);
                }

                if(isRightClick && (isDelete || isMove)) {
                    if (isDelete) {

                        for (int r1 = 0; r1 < rects.size(); r1++) {
                            Rectangle r = rects.get(r1);
                            if (((int)r.getX() > dLeft) &&
                                ((int)r.getX() < dR) &&
                                ((int)r.getX() < dR - (int)r.getWidth()) &&
                                ((int)r.getY() > dTop) &&
                                ((int)r.getY() < dBottom) &&
                                ((int)r.getY() < dBottom - (int)r.getHeight())) {
                                rects.remove(r1);
                                r1--;
                            }
                        }
                        for (int o1 = 0; o1 < ovals.size(); o1++) {
                            shapeOval o = ovals.get(o1);
                            if ((o.getX() > dLeft) &&
                                (o.getX() < dR) &&
                                (o.getX() + o.getWidth() < dR) &&
                                (o.getY() > dTop) &&
                                (o.getY() < dBottom) &&
                                (o.getY() + o.getHeight() < dBottom)) {

                                ovals.remove(o1);
                                o1--;
                            }
                        }
                        for (int x = 0; x < stoke.size(); x++) {
                            Stroke s = stoke.get(x);
                            if ((s.getdLeft() > dLeft) &&
                                (s.getdR() < dR) &&
                                (s.getdTop() > dTop) &&
                                (s.getdBottom() < dBottom)) {

                                stoke.remove(x);
                                x--;
                            }
                        }
                          // System.out.println("DELETED");
                    } else if (isMove) {
                        for (int s1 = 0; s1 < stoke.size(); s1++) {
                            Stroke s = stoke.get(s1);
                            if ((s.getdLeft() > dLeft) &&
                                (s.getdR() < dR) &&
                                (s.getdTop() > dTop) &&
                                (s.getdBottom() < dBottom)) {
                                MStrokes.add(stoke.get(s1));
                                stoke.remove(s1);
                                s1--;
                            }
                        }

                        for (int r1 = 0; r1 < rects.size(); r1++) {
                            Rectangle r = rects.get(r1);
                            if (((int)r.getX() > dLeft) &&
                                ((int)r.getX() < dR) &&
                                ((int)r.getX() < dR - (int)r.getWidth()) &&
                                ((int)r.getY() > dTop) &&
                                ((int)r.getY() < dBottom) &&
                                ((int)r.getY() < dBottom - (int)r.getHeight())) {

                                MRects.add(rects.get(r1));
                                rects.remove(r1);
                                r1--;
                            }
                        }

                        for (int o1 = 0; o1 < ovals.size(); o1++) {
                            shapeOval o = ovals.get(o1);
                            if ((o.getX() > dLeft) &&
                                (o.getX() < dR) &&
                                (o.getX() + o.getWidth() < dR) &&
                                (o.getY() > dTop) &&
                                (o.getY() < dBottom) &&
                                (o.getY() + o.getHeight() < dBottom)) {

                                MOvals.add(ovals.get(o1));
                                ovals.remove(o1);
                                o1--;
                            }
                        }
                        if (MOvals.size() != 0 || MRects.size() != 0 || MStrokes.size() != 0) {
                            isInk = false;
                            isInMoving = true;
                        }
                    }
                }

                ///////////reset//////////////////////////////////////////////

                dLeft = 0;
                dBottom = 0;
                dR = 0;
                dTop = 0;
                gestures = "";
                drawfreeInk = false;
                pX2 = new ArrayList<>();
                pY2 = new ArrayList<>();
            }

             //////////////////////////Rect//////////////////////////////////////
            if (isRect) {
                g2.drawRect(xPos, yPos, width, height);
            }

            if (drawRect) {
                rects.add(new Rectangle(xPos, yPos, width, height));
                drawRect = false;
            }

            //////////////////////////oval//////////////////////////////////////
            if (isOval) {
                g2.drawOval(xPos, yPos, width, height);
            }

            if (drawOval) {
                ovals.add(new shapeOval(xPos, yPos, width, height));
                drawOval = false;
            }

        }
    }
    //mouse listener
    @Override
    public void mouseClicked(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
        xStart = e.getX();
        yStart = e.getY();

        if (isInk || isInMoving) {
            xPrevious = xStart;
            yPrevious = yStart;
        }

        if (isInk) {
            dTop = yStart;
            dBottom = yStart;
            dLeft = xStart;
            dR = xStart;
            if (SwingUtilities.isLeftMouseButton(e))  {
                isRightClick = false;
            } else if (SwingUtilities.isRightMouseButton(e)) {
                isRightClick = true;
            }

            if (isRightClick){
                if ((xStart > this.getWidth() - (2 * pageDivider + 1)) || (xStart < (2 * pageDivider + 1))) {
                    flipped = true;
                    isPageFlipping = true;
                }
            }
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if(isRect) {
            drawRect = true;
            repaint();
        }
        if(isOval) {
            drawOval = true;
            repaint();
        }
        if(isInk) {
            drawfreeInk = true;
            if (isRightClick) {
                gestureMatch = new Recognizer(gestures, false);
                if (gestureMatch.getIsPrevious()) {
                    goForward = true;
                    goBackward = false;
                    InkBookUI.forwardPage.doClick();
                } else if (gestureMatch.getIsNext()) {
                    goBackward = true;
                    goForward = false;
                    InkBookUI.backwardPage.doClick();
                    System.out.println("goBackward");
                } else if (gestureMatch.getIsOverview()) {
                    InkBookUI.overview.doClick();
                }
                isDelete = gestureMatch.getIsDelete();
                isMove = gestureMatch.getIsMove();
            }
            repaint();
        }

        if (isPageFlipping) {
            int stopat = e.getX();
            if (stopat >= this.getWidth()/2) {
                turnStopAt = this.getWidth() - 1;
                forward = false;
            } else {
                turnStopAt = 1;
                forward = true;
            }

            timerG = new Timer(flipping_speed , this);
            timerG.start();
            repaint();
        }

        if(isInMoving) {
            for (Rectangle r: MRects) {
                rects.add(r);
            }
            for (shapeOval o: MOvals) {
                ovals.add(o);
            }
            for (Stroke s: MStrokes) {
                stoke.add(s);
            }
            isInk = true;
            isInMoving = false;
            MRects = new ArrayList();
            MOvals = new ArrayList();
            MStrokes = new ArrayList();
            repaint();
        }
    }

    //mouse motion listener
    @Override
    public void mouseDragged(MouseEvent e) {

        if(isRect || isOval) {
            if(xStart > e.getX()) {
                xPos = e.getX();
                width = xStart - e.getX();
            } else {
                xPos = xStart;
                width = e.getX() - xStart;
            }

            if(yStart > e.getY()) {
                yPos = e.getY();
                height = yStart - e.getY();
            } else {
                yPos = yStart;
                height = e.getY() - yStart;
            }
            repaint();
        }

        if(isInk) {
            xCurrent = e.getX();
            yCurrent = e.getY();
            if(isRightClick) {
                int diffX = xPrevious - xCurrent;
                int diffY = yPrevious - yCurrent;
                if ((diffX == 0) && (diffY > 0)) { gestures += "N"; }
                if ((diffX == 0) && (diffY < 0)) { gestures += "S"; }
                if ((diffX < 0) && (diffY == 0)) { gestures += "E"; }
                if ((diffX > 0) && (diffY == 0)) { gestures += "W"; }
                if ((diffX > 0) && (diffY > 0)) { gestures += "A"; }
                if ((diffX < 0) && (diffY > 0)) { gestures += "B"; }
                if ((diffX < 0) && (diffY < 0)) { gestures += "C"; }
                if ((diffX > 0) && (diffY < 0)) { gestures += "D"; }
            }

            if (xCurrent < dLeft) {
              dLeft = xCurrent;
            }
            if (xCurrent > dR) {
              dR = xCurrent;
            }
            if (yCurrent > dBottom) {
              dBottom = yCurrent;
            }
            if (yCurrent < dTop) {
              dTop = yCurrent;
            }
            xPrevious = xCurrent;
            yPrevious = yCurrent;
            pX2.add(xPrevious);
            pY2.add(yPrevious);
            repaint();
        }
        if (isPageFlipping) {
            if (xCurrent > (this.getWidth() - (pageDivider + 1))) {
                turnStartAt = this.getWidth() - (pageDivider + 1);
            } else if(xCurrent < (pageDivider + 1)) {
                turnStartAt = (pageDivider + 1);
            } else {
                turnStartAt = xCurrent;
            }
            repaint();
        }

        if (isInMoving) {
            xCurrent = e.getX();
            yCurrent = e.getY();
            int diffX = xPrevious - xCurrent;
            int diffY = yPrevious - yCurrent;
            for (Stroke s: MStrokes) {
                for (int pixel = 0; pixel < s.getSize(); pixel++) {
                    int x = s.getlistPX().get(pixel) - diffX;
                    int y = s.getlistPY().get(pixel) - diffY;

                    s.getDiffX(pixel, x);
                    s.getDiffY(pixel, y);
                }
                s.dispDiff(diffX, diffY);
            }
            for (Rectangle r: MRects) {
                int x = (int)r.getX() - diffX;
                int y = (int)r.getY() - diffY;
                r.setLocation(x, y);
            }
            for (shapeOval o: MOvals) {
                int x = o.getX() - diffX;
                int y = o.getY() - diffY;
                o.setX(x);
                o.setY(y);
            }
            xPrevious = xCurrent;
            yPrevious = yCurrent;
            repaint();
        }
    }
    @Override
    public void mouseMoved(MouseEvent e) {

    }


    @Override
    public void keyPressed(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}


    public void isInInk(boolean isInk) {
        this.isInk = isInk;
    }


    public void isInRect(boolean isRect) {
        this.isRect = isRect;
    }


    public void isInOval(boolean isOval) {
        this.isOval = isOval;
    }

    private Rectangle getStringBounds(Graphics2D g2, String str,
                                      float x, float y)
    {
        FontRenderContext f1 = g2.getFontRenderContext();
        GlyphVector gv = g2.getFont().createGlyphVector(f1, str);
        return gv.getPixelBounds(null, x, y);
    }

    public void setBufferLR(BufferedImage leftPage, BufferedImage rightPage) {
        this.leftPage = leftPage;
        this.rightPage = rightPage;
    }


    public void flipped(BufferedImage topPage, BufferedImage bottomPage, boolean forward) {
        this.topPage = topPage;
        this.bottomPage = bottomPage;
        this.forward = forward;

        if (forward) {
            turnStartAt = this.getWidth() - (pageDivider + 1);
            turnStopAt = 1;
        } else if (forward == false){
            turnStartAt = 1 + pageDivider;
            turnStopAt = this.getWidth() - 1;
        }
        flipped = true;

        timer = new Timer(flipping_speed , this);
        timer.start();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (forward) {
            turnStartAt -= 24;
            if (turnStartAt - pageDivider < turnStopAt) {
                isPageFlipping = false;
                flipped = false;
                timer.stop();
                if (isPageFlipping)
                    timerG.stop();
            }
        } else {
            turnStartAt += 24;
            if (turnStartAt + pageDivider > turnStopAt) {
                isPageFlipping = false;
                flipped = false;
                timer.stop();
                if (isPageFlipping)
                    timerG.stop();
            }
        }
        repaint();
    }

    public Recognizer getGesture() {
        return this.gestureMatch;
    }
    public boolean getFlipDirection() {
        return this.goForward;
    }
    public void drawTextRect(Graphics g2, int width, int height, int x, int y) {
  		textbox = new Rectangle(x, y, width, height);
  		// g2.drawRect(textbox);
  		g2.setColor(Color.YELLOW);
  		g2.fillRect(x, y, width, height);
	  }

    public void createNote() {
    	setFocusable(true);
    	addMouseListener(new MouseAdapter () {
		    public void mouseClicked(MouseEvent e) {

		    		// g2.setColor(Color.YELLOW);
		    		// g2.fillRect(e.getX(), e.getY(), 200, 200);
		    		mx = e.getX() + 15;
		    		my = e.getY() - 15;
		    		currentP = new textObj(mx, my);
		    		currentP.setColor(Color.BLACK);
		    		currentP.setFont(new Font("HelveticaNeue-Italic", Font.PLAIN, 13));
		    		currentP.setRectangle(new Rectangle(mx - 15, my - 30, 200, 200));
		    		sticky.add(currentP);
		    		System.out.println("clicked");


		    }
		});
		addKeyListener(new KeyListener() {
			@Override
	    	public void keyTyped(KeyEvent e) {
	    		if(e.getKeyChar() == '\u0008'){
            System.out.println("typed");
	    			if (sticky.size() > 0) {
	    				String character = sticky.get(sticky.size()-1).getText();
						  sticky.get(sticky.size()-1).text = character.substring(0, character.length()-1);
	    			}

				}
				else{
					 currentP.setText(e.getKeyChar());
				}
				repaint();


			}
			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {

			}
		});

	}


}
