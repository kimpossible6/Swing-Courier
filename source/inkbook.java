import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.event.*;
public class inkbook implements ActionListener {
	private JPanel controlButtons, controlDrawing, mainPane, controlPane;
	private JButton newPage, deletePage, forwardPage, backwardPage;	
	private JRadioButton freeFromInk, rectangle, oval, text1;
	private JLabel clickstatus = new JLabel("Welcome!");
	private ButtonGroup bg1;

	public inkbook() {
		mainPane = new JPanel();
		mainPane.setLayout(new BorderLayout());
		FlowLayout l1 = new FlowLayout();
		l1.setHgap(20);
		GridLayout g1 = new GridLayout(2,1);
		g1.setVgap(5);
		controlButtons = new JPanel();
		controlButtons.setLayout(l1);
		controlButtons.setFocusable(false);

		newPage = new JButton("New Page");
		deletePage = new JButton("Delete Page");
		forwardPage = new JButton("Next Page");
		backwardPage = new JButton("Previous Page");

		newPage.setFocusable(false);
		deletePage.setFocusable(false);
		forwardPage.setFocusable(false);
		backwardPage.setFocusable(false);

		controlButtons.add(newPage);
		controlButtons.add(deletePage);
		controlButtons.add(forwardPage);
		controlButtons.add(backwardPage);

		newPage.addActionListener(this);
		deletePage.addActionListener(this);
		forwardPage.addActionListener(this);
		backwardPage.addActionListener(this);

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

		freeFromInk.addActionListener(this);
		rectangle.addActionListener(this);
		oval.addActionListener(this);
		text1.addActionListener(this);

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
		JPanel paper = new JPanel();
		mainPane.add(paper, BorderLayout.CENTER);
		mainPane.add(controlPane, BorderLayout.SOUTH);

	}
	public JPanel getinkbook() {
		return mainPane;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == newPage) {
			setButtonStatus("New Page");
		} else if (e.getSource() == deletePage) {
			setButtonStatus("deletePage");
		} else if (e.getSource() == forwardPage) {
			setButtonStatus("Next Page");
		} else if (e.getSource() == backwardPage) {
			setButtonStatus("Previous Page");
		} else if (e.getSource() == freeFromInk) {
			setButtonStatus("Ink");
		} else if (e.getSource() == rectangle) {
			setButtonStatus("Rectangle");
		} else if (e.getSource() == oval) {
			setButtonStatus("Oval");
		} else if (e.getSource() == text1) {
			setButtonStatus("Text");
		}
	}
	public JLabel getButtonStatus() {
		return clickstatus;
	}
	public void setButtonStatus(String status) {
		clickstatus.setText(status);
	}
}