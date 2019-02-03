import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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



public class InkBook extends JPanel implements ChangeListener{

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
	public InkBook() {
		init();
	}
	
	public JPanel getInkBook() {
		return mainPane;
	}
	public void init() {
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
				previous();
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
			for (InkPageContainer page : inkPageList) {
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
			for (InkPageContainer page : inkPageList) {
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
		String gesture = inkPageList.get(currentPage).getGesture();
		
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
			previous();
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
}