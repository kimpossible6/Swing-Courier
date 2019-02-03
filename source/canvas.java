import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.font.GlyphVector;
import java.awt.font.FontRenderContext;
public class canvas extends JPanel{
	private Image background, newBackground; 
	private Image image;
	private Dimension panelDimension;
	private Graphics2D g2;
	private Graphics g3;
	Font font1;
	private FontMetrics fontM;
	//For Ink 
	boolean mouseEnable;
	private int inkX, inkY, oldInkX, oldInkY;
	private ArrayList<Point> pointList = new ArrayList<>();

	//For Rectangle
	boolean mouseDrag;
	private int x, y, x1, y1;
	private ArrayList<Rectangle> rect = new ArrayList<>();
	private Rectangle current;
	
	//For Oval 
	boolean mouseOval;
	private int ox, oy, ox1, oy1;
	private ArrayList<Rectangle> oval = new ArrayList<>();
	private Rectangle currentOval;

	//For Text
	boolean mouseText;
	private int mx, my, mx1, my1;
	private ArrayList<textObj> sticky = new ArrayList<>();
	private textObj currentP;
	

	//For PostIt
	private Rectangle textbox;
	private ArrayList<Rectangle> textboxes;
	private Point textpoint;
	public canvas() {
		setFocusable(true);
		try {
			URL background_image = this.getClass().getResource("background.png");
			background  = ImageIO.read(background_image);
		}
		catch (Exception e) {
	        e.printStackTrace();
	    }
	    mouseEnable = true;
	    mouseDrag = true;
	    mouseOval = true;
	    mouseText = true;
	}

	@Override
	  protected void paintComponent(Graphics g) {
	  	super.paintComponent(g);
	  	panelDimension = getSize();
	  	if(image == null) {
	  		image = background;
	    	g2 = (Graphics2D) image.getGraphics();
	   		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	  	} 
	    g.drawImage(image, 0, 0, panelDimension.width, panelDimension.height, this);
	    if (mouseDrag && !mouseEnable && !mouseOval && !mouseText) {
	    	drawPerfectRect(g, x, y, x1, y1);
		}
		if (!mouseDrag && !mouseEnable && mouseOval && !mouseText) {
		    	drawPerfectOval(g, ox, oy, ox1, oy1);
		}
	    for (Rectangle r: rect) {
	        g.drawRect((int)r.getX(),(int)r.getY(), (int)r.getWidth(), (int)r.getHeight()); // attention!! g not g2 
	    }
	    for (Rectangle o : oval) {
	       	g.drawOval((int)o.getX(),(int)o.getY(), (int)o.getWidth(), (int)o.getHeight());
	    }
    	font1 = new Font("SansSerif",Font.BOLD,14);
    	fontM = g.getFontMetrics(font1);
    	int height = 0;

    	g.setFont(font1);
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
			g.setColor(Color.YELLOW);
			g.fillRect(rx, ry, rw, rh);
			g.setColor(t.color);
    		if (textTodraw.length() < 16) {
    			System.out.println(textTodraw);
    			g.drawString(textTodraw, x, y);
    			System.out.println(fontM.stringWidth(textTodraw));
    		} else {
    			Rectangle2D bounds = fontM.getStringBounds("Hello World this is fantastic", g);
    			for (int i = 0; i < textTodraw.length()/16; i++) {

    				String currentLine = textTodraw.substring(16 * i, 16 * (i + 1)) + '-';
    				g.drawString(currentLine, x, y);
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
						g.fillRect(rx2, start, rw2, (int)(t.getRectangle()).getHeight() + 3);
						System.out.println("new rec");
    				}
    			} 
    			
    	// 		if (fontM.stringWidth(textTodraw) >= 140) {
					// String part1=currentLine.substring(0,(currentLine.length()/2))+'-';// start +'-'
					// String part2=currentLine.substring((currentLine.length()/2) + 1, currentLine.length());// remainder
    	// 			currentLine = part1;
					// g.drawString(currentLine, x, y);
     //            	y += fontM.getHeight();
     //            	currentLine = part2;
     //            	// maybe need to print part2
     //            	g.drawString(currentLine, x, y);
    	// 		}
    	// 		for(int i = 1; i < words.length; i++) {
    				
		   //        	if(fontM.stringWidth(currentLine+words[i]) < 140) {
		   //              currentLine += " "+words[i];
		   //          } else {
		   //              g.drawString(currentLine, x, y);
		   //              y += fontM.getHeight();
		   //              currentLine = words[i];
		   //          }
			  //   }
			
			  //  if (currentLine.trim().length() > 0) {
			  //      g.drawString(currentLine, x, y);
			  //  }
    		}


    		// while(nIndex < w1.length) {
    		// 	String line = w1[nIndex++];
    		// 	while ((nIndex < w1.length)  && (fontM.stringWidth(line + " \n" + w1[nIndex]) < 100)) {
    		// 		line = line + "\n" + w1[nIndex];
    		// 		nIndex++;
    		// 	}	
    		// 	g.drawString(line, x, y);
    		// 	y = y + height;
    		// }
    	}
	}

	// public void drawString(Graphics g, String text, int x, int y) {
 //    for (String line : text.split("\n"))
 //        g.drawString(line, x, y += g.getFontMetrics().getHeight());
	// }	
	private Rectangle getStringBounds(Graphics2D g2, String str,
                                      float x, float y)
    {
        FontRenderContext f1 = g2.getFontRenderContext();
        GlyphVector gv = g2.getFont().createGlyphVector(f1, str);
        return gv.getPixelBounds(null, x, y);
    }

	public void setStartPoint(int x, int y) {
            this.x = x;
            this.y = y;
    }

    public void setEndPoint(int x, int y) {
            x1 = (x);
            y1 = (y);
    }
    public void setOvalStart(int ox, int oy) {
    	this.ox = ox;
    	this.oy = oy;
    }
    public void setOvalEnd(int ox, int oy) {
    	ox1 = (ox);
    	oy1 = (oy);
    }

    public void drawTextRect(Graphics g, int width, int height, int x, int y) {
		textbox = new Rectangle(x, y, width, height);
		// g.drawRect(textbox);
		g.setColor(Color.YELLOW);
		g.fillRect(x, y, width, height);
	} 

    public void createNote() {
    	setFocusable(true);
    	addMouseListener(new MouseAdapter () {
		    public void mouseClicked(MouseEvent e) {
		    	if (!mouseDrag && !mouseEnable && !mouseOval && mouseText) {
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

		    }    
		});
		addKeyListener(new KeyListener() {
			@Override
	    	public void keyTyped(KeyEvent e) {
	    	if (!mouseDrag && !mouseEnable && !mouseOval && mouseText) {
	    		if(e.getKeyChar() == '\u0008'){
	    			
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
			
			}
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});

	}

	public void textFont(Font font){
		g2.setFont(font);
	}


    public void drawOval() {
    	ox = oy = ox1 = oy1 = 0;
    	setFocusable(true);
    	addMouseListener(new MouseAdapter (){
		    public void mousePressed(MouseEvent e) {
		    	if (!mouseDrag && !mouseEnable && mouseOval && !mouseText ) {
		    		setOvalStart(e.getX(), e.getY());
		    	}
		    }
		    public void mouseReleased(MouseEvent e) {
		    	if (!mouseDrag && !mouseEnable && mouseOval && !mouseText) {
		    		setOvalEnd(e.getX(), e.getY());
	                int px = Math.min(ox, ox1);
	                int py = Math.min(oy, oy1);
	                int pw=Math.abs(ox - ox1);
	                int ph=Math.abs(oy - oy1);
	                currentOval = new Rectangle(px, py, pw, ph);
	                oval.add(currentOval);
	                repaint();
		    	}
		    }
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (!mouseDrag && !mouseEnable && mouseOval  && !mouseText) {
					setOvalEnd(e.getX(), e.getY());
					repaint();
				} 
	        }
		});
	}

	 public void drawPerfectOval(Graphics g2, int ox, int oy, int ox1, int oy1) {
            int px = Math.min(ox, ox1);
            int py = Math.min(oy, oy1);
            int pw = Math.abs(ox - ox1);
            int ph = Math.abs(oy - oy1);
            g2.drawOval(px, py, pw, ph);
    }

    ////////////////Rect////////////////
	public void drawRect() {
		x = y = x1 = y1 = 0;
		setFocusable(true);
		addMouseListener(new MouseAdapter (){
		    public void mousePressed(MouseEvent e) {
		    	if (mouseDrag && !mouseEnable && !mouseOval  && !mouseText) {
		    		setStartPoint(e.getX(), e.getY());
		    	}
		    }
		    public void mouseReleased(MouseEvent e) {
		    	if (mouseDrag && !mouseEnable && !mouseOval  && !mouseText) {
		    		setEndPoint(e.getX(), e.getY());
	                int px = Math.min(x,x1);
	                int py = Math.min(y,y1);
	                int pw=Math.abs(x - x1);
	                int ph=Math.abs(y - y1);
	                current = new Rectangle(px, py, pw, ph);
	                rect.add(current);
	                repaint();
		    	}
		    }
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (mouseDrag && !mouseEnable && !mouseOval  && !mouseText) {
					setEndPoint(e.getX(), e.getY());
					repaint();
				} 
	        }
		});
	}
	 public void drawPerfectRect(Graphics g2, int x, int y, int x1, int y1) {
            int px = Math.min(x,x1);
            int py = Math.min(y,y1);
            int pw=Math.abs(x-x1);
            int ph=Math.abs(y-y1);
            g2.drawRect(px, py, pw, ph);


    }



	//Draw the stroke/////////////////////////////////////////////
	public void stroke() {
		setFocusable(true);
		setDoubleBuffered(false);
		addMouseListener(new MouseAdapter (){
			public void mousePressed(MouseEvent e) {
				if (mouseEnable && !mouseDrag && !mouseOval  && !mouseText) {
					oldInkX = e.getX();
					oldInkY = e.getY();
				}
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (mouseEnable && !mouseDrag && !mouseOval  && !mouseText) {
					inkX = e.getX();
					inkY = e.getY();
					if (g2 != null) {
						g2.drawLine(oldInkX, oldInkY, inkX, inkY);
						//Refresh
						repaint();
						oldInkX = inkX;
						oldInkY = inkY;
					}
				}
			}

		});

	}
	public void clear() {
		// panelDimension = getSize();
		// g2.drawImage(background, 0, 0, panelDimension.width, panelDimension.height, this);
	    // g2.setPaint(Color.white);
	    // g2.fillRect(0, 0, getSize().width, getSize().height);
	    // g2.setPaint(Color.black);

	    
	}
	public void strokeColor(Color color) {
		g2.setPaint(color);
	}

	public void strokeSize(int size) {
		g2.setStroke(new BasicStroke(size));
	}


	
}