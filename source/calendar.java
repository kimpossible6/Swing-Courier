import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.JComboBox;
import java.util.Date;
import javax.swing.table.AbstractTableModel;
import javax.swing.JTable;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class calendar {
  //combox for years and months
	private JComboBox comboBox1, comboBox2;
  //call create calendar
	private CreateCalendar model;
  //create a JTable
	private JTable t1;
  //create a JPanel 
 	private JPanel p1;

  public calendar() {
  	//set up JPanel 
  	p1 = new JPanel();
  	p1.setLayout(new BorderLayout());
    String[] years = { "2018", "2019","2020","2021","2022","2023","2024" };
    String[] months = { "January", "February", "March", "April", "May", "June", "July", "August",
      "September", "October", "November", "December" };
    model = new CreateCalendar();
    t1 = new JTable(model);
    //Select years 
    comboBox1 = new JComboBox(years);
    comboBox1.setBounds(10, 10, 80, 40);
    comboBox1.setSelectedIndex(0);
    comboBox1.addItemListener(new SelectCombo());

    //select months 
    comboBox2 = new JComboBox(months);
    comboBox2.setSelectedIndex(8);
    comboBox2.setBounds(10, 10, 120, 40);
    comboBox1.addItemListener(new SelectCombo());

    JPanel selectYearMonth = new JPanel();
    selectYearMonth.add(comboBox1);
    selectYearMonth.add(comboBox2);

    t1.setBounds(10, 150, 550, 200);
    model.setMonth(comboBox1.getSelectedIndex() + 1998, comboBox2.getSelectedIndex());

    p1.add(selectYearMonth, BorderLayout.NORTH);

    t1.setGridColor(Color.BLUE);
    t1.setShowGrid(true);
    p1.add(t1,BorderLayout.CENTER);
    p1.setSize(500, 500);
    p1.setVisible(true);
  }
  //Return JPanel when calling from other classes
  public JPanel getCalendar() {
  	return p1;
  }
  //Enable year, month selections
  public class SelectCombo implements ItemListener {
    public void itemStateChanged(ItemEvent e) {
      model.setMonth(comboBox1.getSelectedIndex() + 1998, comboBox2.getSelectedIndex());
      t1.repaint();
    }
  }
}

