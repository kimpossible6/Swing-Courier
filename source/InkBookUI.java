import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class InkBookUI extends JPanel {
    private static int panelWidth = 400;
    private static int panelHeight = 600;
    public JButton newPage, deletePage;
    public static JButton forwardPage, backwardPage, overview;
    private JLabel clickstatus = new JLabel("Welcome!");
    private JRadioButton freeFormInk, rect, oval, text;
    private JPanel mainPane, controlPane, controlButtons, controlDrawing, statusBar, currPanel;
    private JLabel statusLabel;
    private JTextArea statusText;
    private ButtonGroup bg1;
    private InkBook container;


    public InkBookUI() {
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        container = new InkBook();
        addMouseListener(container);
        this.add(container);
        mainPane = new JPanel();
        controlPane = new JPanel();
        controlButtons = new JPanel();
        controlDrawing = new JPanel();
        mainPane.setLayout(new BorderLayout());
        controlPane.setLayout(new BorderLayout());
        controlButtons.setLayout(new FlowLayout());
        controlDrawing.setLayout(new FlowLayout());

        newPage = new JButton("New Page");
        deletePage = new JButton("Delete Page");
        forwardPage = new JButton("Next Page");
        backwardPage = new JButton("Previous Page");
        overview = new JButton("Overview");

        freeFormInk = new JRadioButton("Ink");
        rect = new JRadioButton("Rectangle");
        oval = new JRadioButton("Oval");
        text = new JRadioButton("Text");

        controlButtons.add(newPage);
        controlButtons.add(deletePage);
        controlButtons.add(backwardPage);
        controlButtons.add(forwardPage);
        controlButtons.add(overview);

        bg1 = new ButtonGroup();
        bg1.add(freeFormInk);
        bg1.add(rect);
        bg1.add(oval);
        bg1.add(text);

        controlDrawing.add(freeFormInk);
        controlDrawing.add(rect);
        controlDrawing.add(oval);
        controlDrawing.add(text);
        controlPane.add(controlButtons, BorderLayout.NORTH);
        controlPane.add(controlDrawing, BorderLayout.SOUTH);
        mainPane.add(controlPane, BorderLayout.SOUTH);
        this.add(mainPane, BorderLayout.SOUTH);

        deletePage.setEnabled(false);
		    forwardPage.setEnabled(false);
		    backwardPage.setEnabled(false);

		    deletePage.setFocusable(false);
		    forwardPage.setFocusable(false);
		    backwardPage.setFocusable(false);

    }


    public void repaintAfterAction() {
        container.repaintAfterAction();
    }

    public void selections() {
        newPage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setButtonStatus("New Page");
                if (!container.isOverview()) {
                    container.createNewCanvasP();
                    backwardPage.setEnabled(true);
                    bg1.clearSelection();
                }

            }
        });

        deletePage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setButtonStatus("Delete Page");
                if (!container.isOverview()) {
                    deletePage.setEnabled(true);
                    container.deletePage();
                    bg1.clearSelection();

                }

            }
        });
        forwardPage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setButtonStatus("Next Page");
                if (!container.isOverview()) {
                  container.goNextPage();
                  backwardPage.setEnabled(true);
                  System.out.println("hahaha");
                  bg1.clearSelection();
                  // container.nextgesturePage();
                }

                    // container.nextgesturePage();
            }
        });

        backwardPage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setButtonStatus("Previous Page");
                if (!container.isOverview()) {
                    container.goPreviousPage();
                    forwardPage.setEnabled(true);
                    bg1.clearSelection();
                }

            }
        });

        overview.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setButtonStatus("Overview");
                if (!container.isOverview()) {
                  container.overview();
                  bg1.clearSelection();
                }

            }
        });

        rect.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.SELECTED) {
                    setButtonStatus("Rectangle");
                    container.isInRect(true);
                } else {
                    container.isInRect(false);
                }
            }
        });

        oval.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.SELECTED) {
                    setButtonStatus("Oval");
                    container.isInOval(true);
                } else {
                    container.isInOval(false);
                }
            }
        });

        text.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                    // container.isCreatingNote();
                    setButtonStatus("Text");
            }
        });

        freeFormInk.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    setButtonStatus("Ink");
                    container.isInInk(true);
                } else {
                    container.isInInk(false);
                }
            }
        });

    }


    public JLabel getButtonStatus() {
		    return clickstatus;
	  }
	  public void setButtonStatus(String status) {
		    clickstatus.setText(status);
	  }
}
