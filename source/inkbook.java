import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.BufferedImage;
import javax.swing.Timer;

public class InkBook extends JComponent implements ActionListener, MouseListener, MouseMotionListener {
    private static final int panelWidth = 400;
    private static final int panelHeight = 600;
    private static final int overviewRate = 15;
    private static final int overviewRateMax = 28;
    private int pageNum, currentPage, LastIndex;
    private ArrayList<newInkPage> pages;
    private boolean newPage, deletePage;
    ///////////////////Gestures////////////////////////////////////////////
    private String gestures;
    private Recognizer gestureMatch;
    private boolean getForward;
    private boolean getBackward;
	  /////////////#######motion#######/////////////////////////////////////
    //DimensionS of grid
    private int dmGrid, motionTime;
    private double changeWidth, changeHeight;
    private boolean isInOverview;
    private ArrayList<overviewLocation> overviewLocationGrid;
	  private ArrayList<inkOverview> motionChange;

    private boolean isGrid;
    private boolean isZoomIn, isZoomOut;


    private newInkPage canvas;
    private boolean isRightClick;
    ////////////////Timer///////////////////////////////////////////////////
    private Timer timer;
    ///////////////Animation////////////////////////////////////////////////
	  private BufferedImage topPage, bottomPage;
    private BufferedImage leftPage, rightPage;
    private ArrayList<Point> movepoint = new ArrayList<>();




    public InkBook() {
    		setMaximumSize(new Dimension(panelWidth, panelHeight));
    		setLayout(new BorderLayout());
        setBackground(Color.BLUE);
    		pages = new ArrayList<>();
    		motionChange = new ArrayList<>();
    		overviewLocationGrid = new ArrayList<>();

    		canvas = new newInkPage(1);
    		pages.add(canvas);
    		pageNum++;
    		LastIndex++;
    		motionTime = 1;
        topPage = null;
        bottomPage = null;
        gestures = "";
        addKeyListener(canvas);
        addMouseListener(canvas);
        addMouseMotionListener(canvas);

        this.add(canvas, BorderLayout.CENTER);
        // magnify = new Magnifier();
    }

	@Override
    public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (isInOverview) {
			g2.setColor(Color.BLUE);
			g2.fillRect(0,0,this.getWidth(), this.getHeight());
			g2.setColor(Color.BLACK);
			for(int x = 0; x < pages.size(); x++) {
				if (x != currentPage) {
					Graphics2D g2d = (Graphics2D)g2.create();
          System.out.println("paintpage" + x);
					inkOverview inkOverviewMovement = motionChange.get(x);
					double currentXMotion = motionTime * inkOverviewMovement.getXMotion();
					double currentYMotion = motionTime * inkOverviewMovement.getYMotion();


          double currChangeWidth = Math.pow(changeWidth, motionTime);
					double currChangeHeight = Math.pow(changeHeight, motionTime);
					g2d.translate(currentXMotion, currentYMotion);
					g2d.scale(currChangeWidth, currChangeHeight);
					pages.get(x).paint(g2d);
				}

      // magnify.setVisible(true);
			}

			Graphics2D g2d = (Graphics2D)g2.create();

			inkOverview inkOverviewMovement = motionChange.get(currentPage);
			double currentXMotion = motionTime * inkOverviewMovement.getXMotion();
			double currentYMotion = motionTime * inkOverviewMovement.getYMotion();
			double currChangeWidth = Math.pow(changeWidth, motionTime);
			double currChangeHeight = Math.pow(changeHeight, motionTime);

			g2d.translate(currentXMotion, currentYMotion);
			g2d.scale(currChangeWidth, currChangeHeight);
			pages.get(currentPage).paint(g2d);

      if (isRightClick) {
        System.out.println("rotated");
        g2d.rotate(Math.toRadians(180));
      }
		}

	}

	@Override
	public void paintChildren(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (!isInOverview) {
			super.paintChildren(g);
		}
	}

    ////////////////////////////repaint page///////////////////////////////////
    public void repaintAfterAction() {

        //////////////////delete the page ////////////////
        if (deletePage) {
        	removeMouseListener(canvas);
        	removeMouseMotionListener(canvas);
        	removeKeyListener(canvas);
        	this.remove(canvas);
        }


        if (pages.size() > 0) {
        	if (!deletePage) {
	        	removeMouseListener(canvas);
	        	removeMouseMotionListener(canvas);
	        	removeKeyListener(canvas);
        	}
	        canvas = pages.get(currentPage);
	        if(newPage)
	        	this.add(canvas);
	         } else {
	    	     canvas = new newInkPage(1);
        		 pages.add(canvas);
        		 this.add(canvas, BorderLayout.CENTER);
        		 pageNum = 1;
        		 LastIndex = 1;
        		 currentPage = 0;
	        }
        /////////displaying certain pages////////////////////////////////////
        newPage = false;
        deletePage = false;
        dispCanvas(canvas);
        this.revalidate();
        this.repaint();
    }

    /////////////////////Page animation///////////////////////////////////////
    public void pageFlipping(boolean forward) {
        ///front page
        topPage = makeOffscreenImage(canvas);
        canvas = pages.get(currentPage);
        ///current page will be at the back
        bottomPage = makeOffscreenImage(canvas);
        if (pageNum > 1) {
          newInkPage currPage = canvas;
          if (currentPage == 0) {
              leftPage = makeOffscreenImage(currPage);
              currPage = pages.get(currentPage + 1);
              rightPage = makeOffscreenImage(currPage);
              canvas.setBufferLR(leftPage, rightPage);
          } else if (currentPage == pageNum - 1) {
              rightPage = makeOffscreenImage(currPage);
              currPage = pages.get(currentPage - 1);
              leftPage = makeOffscreenImage(currPage);
              canvas.setBufferLR(leftPage, rightPage);
          } else {
              currPage = pages.get(currentPage - 1);
              leftPage = makeOffscreenImage(canvas);
              currPage = pages.get(currentPage + 1);
              rightPage = makeOffscreenImage(canvas);
              canvas.setBufferLR(leftPage, rightPage);
          }
        }
        canvas.flipped(topPage, bottomPage, forward);
        canvas.setBufferLR(leftPage, rightPage);
        repaintAfterAction();
    }

    ////////////////////////buffer image////////////////////////////////////////
    public BufferedImage makeOffscreenImage (JComponent source) {
        GraphicsConfiguration gfxConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage offscreenImage = gfxConfig.createCompatibleImage(source.getWidth(), source.getHeight());
        Graphics2D offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
        source.paint(offscreenGraphics);
        return offscreenImage;
    }


    ///////create new page
    public void createNewCanvasP() {
    		pageNum++;
    		LastIndex++;
    		currentPage = pageNum - 1;
    		newPage = true;
        newInkPage temp = new newInkPage(LastIndex);
  		  pages.add(temp);
  		  repaintAfterAction();
    }


    public void deletePage() {
    	pages.remove(currentPage);
      if (currentPage == 0 && pageNum == 1) {
        LastIndex= 0;
    		pageNum = 0;
    	}
    	if (currentPage == pageNum - 1) {
    		LastIndex--;
    	}
    	if (currentPage > 0) {
    		currentPage--;
    	}
    	pageNum--;
    	deletePage = true;
    	repaintAfterAction();
    }

    public void nextgesturePage() {
      System.out.println("next gesture page called"+pages.get(currentPage).getFlipDirection());
      if (pages.get(currentPage).getFlipDirection()) {
          if (currentPage < pageNum - 1) {
            currentPage++;
            System.out.println("next gesture page called2");
            pageFlipping(true);
        	}
      }
    }

    /////////////////////////NEXT PAGE///////////////////////////////////////
    public void goNextPage() {
    	if (currentPage < pageNum - 1) {
        currentPage++;
        pageFlipping(true);
    	}
    }

    //////////////////////////PREVIOUS PAGE//////////////////////////////////
    public void goPreviousPage() {
    	if (currentPage > 0) {
    		currentPage--;
    		pageFlipping(false);
    	}
    }

    //////////OVERVIEW////////////////////////////////////////////////////////
  	public void overview() {
  		isInOverview = true;
  		isZoomIn = true;
  		motionTime = 1;
  		locatePages();
  		MotionMovement(overviewLocationGrid);
      for (newInkPage page: pages) {
          page.setVisible(true);
          newInkPage temp = new newInkPage(pages.size() + 3);
          dispCanvas(temp);
       }
  		timer = new Timer(overviewRate, this);
  		timer.start();
  		repaint();
  	}

    /////////////////////////drawing Options///////////////////////////////////
    public void isInInk(boolean isInk) {
    	canvas.isInInk(isInk);
    }
  	public void isInRect(boolean isRect) {
  		canvas.isInRect(isRect);
  	}

    public void isInOval(boolean isOval) {
    	canvas.isInOval(isOval);
    }

    public void isCreatingNote() {
      canvas.createNote();
    }
    public boolean isOverview() {
      return this.isInOverview;
    }


	  //////////////display canvas////////////////////////////////////////////////
    public void dispCanvas(newInkPage currentPage) {
        for (newInkPage page: pages) {
        	if (!page.equals(currentPage)) {
        		removeKeyListener(page);
        		removeMouseListener(page);
        		removeMouseMotionListener(page);
        		page.setVisible(false);
        	} else {
		        addMouseListener(page);
		        addMouseMotionListener(page);
		        addKeyListener(page);
        		page.setVisible(true);
        	}
        }
    }


	   public void locatePages() {
		     int pageNum = pages.size();
         dmGrid = 1;
         for(int x = 1; x < 3000; x++) {
           int lB = (int) Math.pow(x - 1, 2);
           int uB = (int) Math.pow(x, 2);
			     if (pageNum > lB && pageNum <= uB) {
             dmGrid = x;
             break;
           }
      }
		int marginBTW = 12 * (dmGrid + 1);
		int marginLeft = this.getWidth() - marginBTW;
		int marginLeftHeight = this.getHeight() - marginBTW;
		int thumbnailWidth = marginLeft / dmGrid;
		int thumbnailHeight = marginLeftHeight / dmGrid;
    int totalWidth = 12 + thumbnailWidth;
    int totalHeight = 12 + thumbnailHeight;

		for (int y = 12; y < this.getHeight() - thumbnailHeight; y+= totalHeight) {
      for (int x = 12; x < this.getWidth() - thumbnailWidth; x+= totalWidth) {
        if (overviewLocationGrid.size() < pages.size()) {
          overviewLocationGrid.add(new overviewLocation(x, y, thumbnailWidth, thumbnailHeight));
				}
			}
		}
	}

	public void MotionMovement(ArrayList<overviewLocation> gridPlace) {
		for (overviewLocation gdLoc: gridPlace) {
			double xMotion = gdLoc.getX()/(overviewRateMax * 1.0);
			double yMotion = gdLoc.getY()/(overviewRateMax * 1.0);
      double w = (double) this.getWidth();
      double h = (double) this.getHeight();
			double resizeW = (gdLoc.getWidth() / w);
			double resizeH = (gdLoc.getHeight() / h);
			changeWidth = Math.pow(resizeW, (1.0/overviewRateMax));
			changeHeight = Math.pow(resizeW, (1.0/overviewRateMax));
			motionChange.add(new inkOverview(0, 0, this.getWidth(), this.getHeight(), xMotion, yMotion));
		}

	}

	/////////////////////////////////match to page index//////////////////////////
	public int matchPageIndex(int x, int y) {
  	for (int page = 0; page < overviewLocationGrid.size(); page++) {
      /////from left to right
  		double leftX = overviewLocationGrid.get(page).getX();
  		double rightX = overviewLocationGrid.get(page).getWidth() + leftX;
      /////////from subTopPage to subBottomPage
      double topY = overviewLocationGrid.get(page).getY();
  		double bottomY = overviewLocationGrid.get(page).getHeight() + topY;
      System.out.println("is Matching!");
  		if (x >= leftX && x <= rightX && y >= topY && y <= bottomY) {
  	      return page;
  		}
  	}
  	return -1;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
    if (isZoomOut) {
			motionTime--;
			if (motionTime < 1) {
				isInOverview = false;
				isZoomOut = false;
				isGrid = false;
				dispCanvas(pages.get(currentPage));
				overviewLocationGrid = new ArrayList();
				motionChange = new ArrayList();
				timer.stop();
			}
		} else if (isZoomIn) {
			motionTime++;
			if (motionTime > overviewRateMax) {
				isGrid = true;
				timer.stop();
			}
		}
		repaint();
	}

    @Override
    public void mouseClicked(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }
    @Override
    public void mousePressed(MouseEvent e) {
      //////gesture//////////////////////////////
      if (SwingUtilities.isRightMouseButton(e)) {
         System.out.println("Right Mouse Clicked!");
         if (isGrid) {
       		int page = matchPageIndex(e.getX(), e.getY());
       		if (page != -1 && page != 0)
       			currentPage = page - 1;
            Collections.swap(pages, page, page - 1);
       		isZoomIn = true;
       		isZoomOut = false;
       		timer = new Timer(overviewRate, this);
       		timer.start();
       	}
     } else if (SwingUtilities.isLeftMouseButton(e)) {
       if (isGrid) {
     		int page = matchPageIndex(e.getX(), e.getY());
     		if (page != -1)
     			currentPage = page;
     		isZoomIn = false;
     		isZoomOut = true;
     		timer = new Timer(overviewRate, this);
     		timer.start();
     	}
     }
    }
    @Override
    public void mouseDragged(MouseEvent e) {
      // movepoint.add(e.getPoint());
      // System.out.println(e.getX());
      // System.out.println(e.getY());
    }
    @Override
    public void mouseMoved(MouseEvent e) {

    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }

}
