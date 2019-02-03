import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.event.*;
public class inkbook{
	private JPanel controlButtons, controlDrawing, controlPane;
	private JLayeredPane mainPane;
	private JButton newPage, deletePage, forwardPage, backwardPage;	
	private JRadioButton freeFromInk, rectangle, oval, text1;
	private JLabel clickstatus = new JLabel("Welcome!");
	private ButtonGroup bg1, bg2, bg3;
	private newInkPage inkPage;
	private int pageIndex;
	private int currentPage;
	private JPanel colorPanel;
	private boolean inkselect;
	private JRadioButton black, red, blue, green, yellow, stroke1, stroke2;
	private ArrayList<newInkPage> inkPageList;
	public inkbook() {
		inkselect = false;
		inkPage = new newInkPage();
		mainPane = new JLayeredPane();
		mainPane.setLayout(new BorderLayout());
		FlowLayout l1 = new FlowLayout();
		l1.setHgap(20);
		GridLayout g1 = new GridLayout(2,1);
		g1.setVgap(5);
		controlButtons = new JPanel();
		controlButtons.setLayout(l1);
		controlButtons.setFocusable(true);

		colorPanel = new JPanel();
		colorPanel.setLayout(new GridLayout(6,1));
		colorPanel.setFocusable(false);
		colorPanel.setEnabled(true);
		colorPanel.setVisible(false);
		colorPanel.setBackground(Color.LIGHT_GRAY);

		newPage = new JButton("New Page");
		deletePage = new JButton("Delete Page");
		forwardPage = new JButton("Next Page");
		backwardPage = new JButton("Previous Page");

		deletePage.setEnabled(false);
		forwardPage.setEnabled(false);
		backwardPage.setEnabled(false);

		deletePage.setFocusable(false);
		forwardPage.setFocusable(false);
		backwardPage.setFocusable(false);

		controlButtons.add(newPage);
		controlButtons.add(deletePage);
		controlButtons.add(forwardPage);
		controlButtons.add(backwardPage);

		
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
		backwardPage.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	previous();
                	forwardPage.setEnabled(true);
        }});

		controlDrawing = new JPanel();
		controlDrawing.setLayout(l1);
		controlDrawing.setFocusable(false);

		freeFromInk = new JRadioButton("Ink");
		rectangle = new JRadioButton("Rectangle");
		oval = new JRadioButton("Oval");
		text1 = new JRadioButton("Text");

		freeFromInk.setFocusable(false);
		rectangle.setFocusable(false);
		oval.setFocusable(false);
		text1.setFocusable(false);

		freeFromInk.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	ink();
        }});
		rectangle.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	rect();
        }});
		oval.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	circle();
        }});
		text1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	text();
        }});

		bg1 = new ButtonGroup();
		bg1.add(freeFromInk);
		bg1.add(rectangle);
		bg1.add(oval);
		bg1.add(text1);

		controlDrawing.add(freeFromInk);
		controlDrawing.add(rectangle);
		controlDrawing.add(oval);
		controlDrawing.add(text1);

		controlPane = new JPanel();
		controlPane.setLayout(g1);
		controlPane.add(controlButtons);
		controlPane.add(controlDrawing);
		controlPane.setFocusable(false);
		// JPanel paper = new JPanel();
//		clear = new JRadioButton("Clear");
		black = new JRadioButton("Black");
		black.setForeground(Color.BLACK);
		red = new JRadioButton("Red");
		red.setForeground(Color.RED);
		blue = new JRadioButton("Blue");
		blue.setForeground(Color.BLUE);
		green = new JRadioButton("Green");
		green.setForeground(Color.GREEN);
		yellow = new JRadioButton("Yellow");
		yellow.setForeground(Color.YELLOW);
		stroke1 = new JRadioButton("Stroke1");
		stroke2 = new JRadioButton("Stroke2");
//		clear.setFocusable(false);
		black.setFocusable(false);
		red.setFocusable(false);
		blue.setFocusable(false);
		green.setFocusable(false);
		yellow.setFocusable(false);
		stroke1.setFocusable(false);
		stroke1.setFocusable(false);
////		clear.addActionListener(new ActionListener() {
////                @Override
//                public void actionPerformed(ActionEvent e) {
//                	if (inkselect) {
//                		inkClear();
//                	}
//        }});
		black.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	if (inkselect) {
                		inkColor(Color.BLACK);
                	} 
        }});
		red.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	if (inkselect) {
                		inkColor(Color.RED);
                	}
        }});
		blue.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	if (inkselect) {
                		inkColor(Color.BLUE);
                	} 
        }});
		green.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	if (inkselect) {
                		inkColor(Color.GREEN);
                	} 
        }});
		yellow.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	if (inkselect) {
                		inkColor(Color.YELLOW);
                	} 
        }});
		stroke1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	if (inkselect) {
                		inkStroke(2);
                	} 
        }});
        stroke2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	if (inkselect) {
                		inkStroke(4);
                	} 
        }});
		bg2 = new ButtonGroup();
//		bg2.add(clear);
		bg2.add(black);
		bg2.add(red);
		bg2.add(blue);
		bg2.add(green);
		bg2.add(yellow);
		bg3 = new ButtonGroup();
		bg3.add(stroke1);
		bg3.add(stroke2);

//		colorPanel.add(clear);
		colorPanel.add(black);
		colorPanel.add(red);
		colorPanel.add(blue);
		colorPanel.add(green);
		colorPanel.add(yellow);
		colorPanel.add(stroke1);
		colorPanel.add(stroke2);


		// newP();
		inkPageList = new ArrayList<>();

		inkPage.setLayout(new BorderLayout());
		mainPane.add(inkPage, BorderLayout.CENTER);
		mainPane.add(controlPane, BorderLayout.SOUTH);
		inkPage.add(colorPanel, BorderLayout.EAST);
		inkPageList.add(inkPage);
	}
	public JLayeredPane getinkbook() {
		return mainPane;
	}
	public void newP() {
		setButtonStatus("New Page");
		forwardPage.setEnabled(false);
		inkPage = new newInkPage();
		inkPage.setLayout(new BorderLayout());
		inkPage.add(colorPanel, BorderLayout.EAST);
		for (newInkPage page : inkPageList) {
			page.setVisible(false);
			page.setEnabled(false);
		}
		inkPageList.add(inkPage);
		inkPage.setFocusable(true);
		inkPage.setVisible(true);
		inkPage.setEnabled(true);
		mainPane.add(inkPage, BorderLayout.CENTER);
		System.out.println("New Page");
		pageIndex = inkPageList.indexOf(inkPage);
		currentPage = pageIndex + 1;
		inkPage.page_number = currentPage;
		if (inkPageList.size() > 1) {
			deletePage.setEnabled(true);
		}

	}
	public void next() {
		setButtonStatus("Next Page");
		if (inkPageList.size() == 1) {
			deletePage.setEnabled(false);
		}
		if (this.pageIndex < inkPageList.size() - 1) {
			for (newInkPage page : inkPageList) {
				if (page.isVisible() && page.isEnabled()) {
					page.setVisible(false);
					page.setEnabled(false);
				}
			}
			inkPageList.get(this.pageIndex + 1).setVisible(true);
			inkPageList.get(this.pageIndex + 1).setVisible(true);
			// inkPageList.get(this.pageIndex + 1).

			this.pageIndex++;
			currentPage = inkPageList.get(this.pageIndex).page_number;
		}

		if (this.pageIndex >= inkPageList.size() - 1) {
			forwardPage.setEnabled(false);
		}

	}
	public void previous() {
		setButtonStatus("Previous Page");
		if (inkPageList.size() == 1) {
			deletePage.setEnabled(false);
		}
		if (this.pageIndex > 0) {
			for (newInkPage page : inkPageList) {
				if (page.isVisible() && page.isEnabled()) {
					page.setVisible(false);
					page.setEnabled(false);
				}
			}
			inkPageList.get(this.pageIndex - 1).setVisible(true);
			inkPageList.get(this.pageIndex - 1).setEnabled(true);

			this.pageIndex--;
			currentPage = inkPageList.get(this.pageIndex).page_number;
			if (this.pageIndex < 1 || inkPageList.size() ==1) {
				backwardPage.setEnabled(false);
			}
		}
	}
	public void delete() {
		setButtonStatus("deletePage");
		if (inkPageList.size() > 1) {
			for(newInkPage page : inkPageList) {
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
	public void inkClear() {
		setButtonStatus("Clear");
		inkPage.clear();
	}
	public void inkColor(Color color) {
		inkPage.stroke();
		inkPage.strokeColor(color);
	}
	public void inkStroke(int size) {
		inkPage.strokeSize(size);
	}
	public void ink(){
		setButtonStatus("Ink");
		inkselect = true;
		inkPage.mouseEnable = true;
		inkPage.mouseDrag = false;
		inkPage.mouseOval = false;
		inkPage.mouseText = false;
		inkColor(Color.BLACK);
		inkStroke(1);
		bg2.clearSelection();
		bg3.clearSelection();
		colorPanel.setVisible(true);
	}
	public void rect() {
		inkselect = false;
		inkPage.mouseDrag = true;
		inkPage.mouseEnable = false;
		inkPage.mouseOval = false;
		inkPage.mouseText = false;
		setButtonStatus("Rectangle");
		bg2.clearSelection();
		bg3.clearSelection();
		// inkPage.mouseRelease = true; 
		inkPage.drawRect();
		// inkPage.rubRect();
		colorPanel.setEnabled(false);
		colorPanel.setVisible(false);

	}
	public void circle() {
		inkselect = false;
		inkPage.mouseEnable = false;
		inkPage.mouseDrag = false;
		inkPage.mouseOval = true;
		inkPage.mouseText = false;
		setButtonStatus("Oval");
		bg2.clearSelection();
		bg3.clearSelection();
		inkPage.drawOval();
		colorPanel.setEnabled(false);
		colorPanel.setVisible(false);

	}
	public void text() {
		inkselect = false;
		inkPage.mouseEnable = false;
		inkPage.mouseDrag = false;
		inkPage.mouseOval = false;
		inkPage.mouseText = true;
		setButtonStatus("Text");
		bg2.clearSelection();
		bg3.clearSelection();
		// inkPage.textCreator();
		inkPage.createNote();
		inkPage.requestFocus();
		// inkPage.createText(inkPage.textpoint);
		colorPanel.setEnabled(false);
		colorPanel.setVisible(false);


	}
	public JLabel getButtonStatus() {
		return clickstatus;
	}
	public void setButtonStatus(String status) {
		clickstatus.setText(status);
	}
}