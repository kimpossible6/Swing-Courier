import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.event.*;



public class InkBook extends JPanel implements ChangeListener {

	private InkPageContainer inkPage;
	private JPanel controlButtons, controlDrawing, controlPane;
	private JPanel mainPane;
	private JButton backwardPage, newPage, deletePage, forwardPage, selectColor;
	private JLabel clickstatus = new JLabel("Welcome!");
	private JRadioButton freeFromInk, rectangle, oval, text1;
	private ButtonGroup bg1, bg2, bg3;
	private int pageIndex;
	private int currentPage;
	private List<InkPageContainer> inkPageList;
	private Color newColor = Color.BLUE;

	/////New change
	public BufferedImage topPage, bottomPage;
	public static boolean moveForward = false;
	public static boolean moveBackward = false;
	public static  InkPageContainer frontPage, backPage;
	public static  InkPageContainer frontPage1, backPage1;



	public static boolean grabLeft = false;
	public static boolean grabRight = false;
	public static InkPageContainer frontPage3, backPage3;
	public static InkPageContainer frontPage4, backPage4;



	public InkBook() {
		init();
	}

	public JPanel getInkBook() {
		return mainPane;
	}
	public void init() {
		//new change
		moveForward = false;
		//
		this.setLayout(new BorderLayout());

		mainPane = new JPanel();
		controlPane = new JPanel();
		mainPane.setLayout(new BorderLayout());


		inkPage = new InkPageContainer();
		inkPage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				performGesture(e);
			}
		});


		inkPageList = new ArrayList<InkPageContainer>();
		inkPageList.add(inkPage);
		inkPage.page_number = currentPage;


		newPage    = new JButton("New Page");
		deletePage = new JButton("Delete Page");
		backwardPage = new JButton("Previous Page");
		forwardPage   = new JButton("Next Page");
		selectColor = new JButton("Color");

		backwardPage.setEnabled(false);
		deletePage.setEnabled(false);
		forwardPage.setEnabled(false);

		FlowLayout l1 = new FlowLayout();
		l1.setHgap(20);
		GridLayout g1 = new GridLayout(2,1);
		g1.setVgap(5);

		controlButtons = new JPanel();
		controlButtons.setLayout(l1);
		controlButtons.add(newPage);
		controlButtons.add(deletePage);
		controlButtons.add(forwardPage);
		controlButtons.add(backwardPage);
		controlButtons.add(selectColor);

		newPage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newP();
				backwardPage.setEnabled(true);
			}});
		deletePage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				delete();
			}});
		forwardPage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				next();
				backwardPage.setEnabled(true);
			}});

		backwardPage.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("start Prev");
				previous();
				System.out.println("end Prev");
				forwardPage.setEnabled(true);
			}
		});

		selectColor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				newColor = JColorChooser.showDialog(
						InkBook.this,
						"Choose Background Color",
						newColor);
				for(int i = 0; i <inkPageList.size(); i++) {
					inkPageList.get(i).setColor(newColor);
				}
				selectColor.setBackground(newColor);

			}

		});

		controlDrawing = new JPanel();
		controlDrawing.setLayout(l1);

		freeFromInk  = new JRadioButton("Ink");
		rectangle = new JRadioButton("Rectangle");
		oval = new JRadioButton("Oval");
		text1 = new JRadioButton("Text");

		bg1 = new ButtonGroup();
		bg1.add(freeFromInk);
		bg1.add(rectangle);
		bg1.add(oval);
		bg1.add(text1);


		controlDrawing.add(freeFromInk);
		controlDrawing.add(rectangle);
		controlDrawing.add(oval);
		controlDrawing.add(text1);



		freeFromInk.setSelected(true);
		freeFromInk.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setButtonStatus("Ink");
				changeTyping("");
				changeStrokeType(freeFromInk.getText());
			}
		});

		rectangle.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setButtonStatus("Rectangle");
				changeTyping("");
				changeStrokeType(rectangle.getText());
			}
		});

		oval.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setButtonStatus("Oval");
				changeTyping("");
				changeStrokeType(oval.getText());
			}
		});

		text1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setButtonStatus("Text");
				changeTyping("Text");
			}
		});

		JScrollPane scroll = new JScrollPane(mainPane);
		this.add(scroll, BorderLayout.CENTER);

		controlPane.setLayout(g1);
		controlPane.add(controlButtons);
		controlPane.add(controlDrawing);

		mainPane.add(inkPage, BorderLayout.CENTER);
		mainPane.add(controlPane, BorderLayout.SOUTH);


	}

	//check stroke: for rectangle, oval and ink
	public void changeStrokeType(String text) {
		InkPageContainer.setRadioChoice(text);
	}
	//check typing: for text
	public void changeTyping(String text) {
		InkPageContainer.setTextChoice(text);
	}

	// for resize;
	public void resizePages() {
		int width = 0, height = 0;
		for(int i = 0; i < inkPageList.size(); i++) {
			if(width < inkPageList.get(i).getMaxWidth()) {
				width = inkPageList.get(i).getMaxWidth();
			}
			if(height < inkPageList.get(i).getMaxHeight()) {
				height = inkPageList.get(i).getMaxHeight();
			}
		}
		Dimension d = new Dimension(width,height);
		for(int j = 0; j < inkPageList.size(); j++) {
			inkPageList.get(j).setMinimumSize(new Dimension(d));
			inkPageList.get(j).setPreferredSize(new Dimension(d));
			inkPageList.get(j).setMaximumSize(new Dimension(1000,1000));
			inkPageList.get(j).setSize(new Dimension(d));
			inkPageList.get(j).resized(mainPane.getWidth(),mainPane.getHeight());
			inkPageList.get(j).repaint();
		}


	}

	public void newP() {
		setButtonStatus("New Page");
		forwardPage.setEnabled(false);
		inkPage = new InkPageContainer();
		inkPage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				performGesture(e);
			}
		});

		inkPage.setColor(newColor);
		inkPage.setLayout(new BorderLayout());

		for (InkPageContainer page : inkPageList) {
			page.setVisible(false);
			page.setEnabled(false);
		}
		inkPageList.add(inkPage);
		inkPage.setFocusable(true);
		inkPage.setVisible(true);
		inkPage.setEnabled(true);
		mainPane.add(inkPage, BorderLayout.CENTER);

		pageIndex = inkPageList.indexOf(inkPage);
		currentPage = pageIndex + 1;
		inkPage.page_number = currentPage;
		System.out.println("New Page" + pageIndex);
		System.out.println("New Page1" + currentPage);
		System.out.println("New Page2" + inkPage.page_number);
		if (inkPageList.size() > 1) {
			deletePage.setEnabled(true);
		}

	}
	public void next() {
		revalidate();
		backPage = null;
		frontPage = null;
		setButtonStatus("Next Page");
		if (inkPageList.size() == 1) {
			deletePage.setEnabled(false);
		}
		if (this.pageIndex < inkPageList.size() - 1) {
			for (InkPageContainer page : inkPageList) {
				if (page.isVisible() && page.isEnabled()) {
					//two pages
					backPage1 = page;
					frontPage1 = inkPageList.get(inkPageList.indexOf(page) + 1);
					System.out.println("checkNext" + (inkPageList.indexOf(page) + 1));
					moveForward = true;
					moveBackward = false;
					page.pageForward = true;
					page.pageBackward = false;
					page.rectX = 0;
					//for pages
					page.frontPage2 = makeOffscreenImage(frontPage1);
					page.backPage2 = makeOffscreenImage(backPage1);
					//System.out.println("transformed2!");
//					repaint();


				}
//				page.pageForward = false;
			}



			this.pageIndex++;
			currentPage = inkPageList.get(this.pageIndex).page_number;
			System.out.println("after call next:" + currentPage);
			if (this.pageIndex >= inkPageList.size() - 1) {
				forwardPage.setEnabled(false);
			}
			System.out.println("Next Page" + pageIndex);
			System.out.println("Next Page1" + currentPage);
			System.out.println("Next Page2" + inkPage.page_number);
		}



	}


	public void previous() {
		revalidate();
		backPage1 = null;
		frontPage1 = null;
		setButtonStatus("Previous Page");
		if (inkPageList.size() == 1) {
			deletePage.setEnabled(false);
		}
		if (this.pageIndex > 0) {
			for (InkPageContainer page : inkPageList) {

				if (page.isVisible() && page.isEnabled()) {
					backPage = page;
					moveBackward = true;
					moveForward = false;
					frontPage = inkPageList.get(inkPageList.indexOf(page) - 1);
					System.out.println("checkprevious: " + (inkPageList.indexOf(page) - 1));
					page.rectX = 0;
					page.pageForward = false;
					page.pageBackward = true;
					page.frontPage = makeOffscreenImage(frontPage);
					page.backPage = makeOffscreenImage(backPage);
//
					System.out.println("here" + moveBackward);
//					this.pageIndex--;
//					currentPage = inkPageList.get(this.pageIndex).page_number;

				}
			}


			this.pageIndex--;
			currentPage = inkPageList.get(this.pageIndex).page_number;
//			currentPage = inkPageList.get(this.pageIndex).page_number;
			System.out.println("after call previous:" + currentPage);
			if (this.pageIndex < 1 || inkPageList.size() ==1) {
				backwardPage.setEnabled(false);
			}

			System.out.println("Prev Page" + pageIndex);
			System.out.println("Prev Page1" + currentPage);
			System.out.println("Prev Page2" + inkPage.page_number);
		}


//		moveForward = false;
//		setButtonStatus("Previous Page");
//		if (inkPageList.size() == 1) {
//			deletePage.setEnabled(false);
//		}
//
//		if (this.pageIndex > 0) {
//			InkPageContainer currPage = inkPageList.get(this.pageIndex);
//			backPage = currPage;
//			moveBackward = true;
//			frontPage = inkPageList.get(this.pageIndex - 1);
//
//			currPage.pageForward = false;
//			currPage.pageBackward = true;
//			currPage.frontPage = makeOffscreenImage(frontPage);
//			currPage.backPage = makeOffscreenImage(backPage);
//
//			System.out.println("here" + moveBackward);
//			this.pageIndex--;
//			currentPage = inkPageList.get(this.pageIndex).page_number;
//			repaint();
//
//		}
//
//		if (this.pageIndex > 0) {
//			for (InkPageContainer page : inkPageList) {
//				if (page.isVisible() && page.isEnabled()) {
//					page.pageBackward = true;
//					page.pageForward = false;
//					backPage = inkPageList.get(this.pageIndex);
//					frontPage = inkPageList.get(this.pageIndex - 1);
//
//					page.frontPage = makeOffscreenImage(frontPage);
//					page.backPage = makeOffscreenImage(backPage);
////					inkPageList.get(pageIndex).setVisible(false);
////					inkPageList.get(pageIndex).setEnabled(false);
////					if (!page.timer.isRunning()) {
////						System.out.println("transformed previous");
////
////					}
//
////					repaint();
////
////
//				}
//			}

//			inkPageList.get(this.pageIndex - 1).setVisible(true);
//			inkPageList.get(this.pageIndex - 1).setEnabled(true);

//			this.pageIndex--;
//			currentPage = inkPageList.get(this.pageIndex).page_number;
//			if (this.pageIndex < 1 || inkPageList.size() ==1) {
//				backwardPage.setEnabled(false);
//			}
//		}
	}





		public void delete() {
		setButtonStatus("deletePage");
		if (inkPageList.size() > 1) {
			for(InkPageContainer page : inkPageList) {
				if (page.page_number == currentPage) {
					this.pageIndex = inkPageList.indexOf(page);
					page.setVisible(false);
					page.setEnabled(false);
					inkPageList.remove(page);
					if (this.pageIndex == 0) {
						next();
					} else {
						previous();

					}
				}
			}
		}

	}

	//change gesture
	@Override
	public void stateChanged(ChangeEvent e) {

		System.out.println("state changed");
		String gesture = inkPageList.get(currentPage).getGesture();

//		if (inkPageList.get(this.pageIndex).getBackRender()) {
//			inkPageList.get(this.pageIndex).setVisible(false);
//			inkPageList.get(this.pageIndex).setEnabled(false);
//			inkPageList.get(this.pageIndex + 1).setEnabled(true);
//			inkPageList.get(this.pageIndex + 1).setVisible(true);
//			System.out.println("rendering happened");
//		}

	}

	//return button status at bottom
	public JLabel getButtonStatus() {
		return clickstatus;
	}
	//set button status
	public void setButtonStatus(String status) {
		clickstatus.setText(status);
	}


	//perform gesture: each command corresponds to the gestures in Recognizer
	private void performGesture(ActionEvent e) {
		if(e.getActionCommand().equals("Next")) {
			next();
		} else if (e.getActionCommand().equals("Back")) {
			System.out.println("start Prev");

			previous();
			System.out.println("end Prev");
		} else if(e.getActionCommand().equals("Delete")){
			setButtonStatus("Deleting");

		} else if(e.getActionCommand().equals("Select")){
			setButtonStatus("Selecting");

		} else if (e.getActionCommand().equals("None")) {
			setButtonStatus("Unknown Gesture");
		} else if (e.getActionCommand().equals("Red")) {
			setButtonStatus("Rectangle");
			freeFromInk.setSelected(false);
			rectangle.setSelected(true);

		} else if (e.getActionCommand().equals("Yellow")) {
			setButtonStatus("Oval");
			freeFromInk.setSelected(false);
			oval.setSelected(true);
		} else if (e.getActionCommand().equals("Text")) {
			setButtonStatus("Text");
			freeFromInk.setSelected(false);
			text1.setSelected(true);
		} else if (e.getActionCommand().equals("color")) {
			selectColor.doClick();
		}
	}



	public BufferedImage makeOffscreenImage (JComponent source) {
		GraphicsConfiguration gfxConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		BufferedImage offscreenImage = gfxConfig.createCompatibleImage(source.getWidth(), source.getHeight());
		Graphics2D offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();

		source.paint(offscreenGraphics);
		return offscreenImage;
	}


}