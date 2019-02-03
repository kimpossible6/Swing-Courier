import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.*;
public class addressbook{
	private JPanel book;
	private JTable t1;
	private JScrollPane jsp1;
	private JTextArea text1;
	private int index;
	private boolean DEBUG = false;
	String[] columnNames = {"First Name",
         				"Last Name",
                        "Phone Number",
                        "Email Address",
                        "Comments"};
	   	Object[][] data = {
	    {"Catherine", "Smith",
	     "4042223333", "Csmith@gmail.com", "CS 4470 Classmate"},
	    {"John", "Doe",
	     "4785221212", "JDoe@gmail.com", "Roommate"},
	    {"Jasmine", "Luke",
	     "4041982675", "JasmineL@gmail.com", "Auntie"},
	    {"Jane", "Lee",
	     "4049231537","Jane96@yahoo.com" , "AT&T Engineer"},
	    {"Naomi", "Satomi",
	     "4042356933", "SatomiN@yahoo.com", "Japanese Teacher"},
	    {"Nicole", "Chen",
	     "4041352933", "nicolechen9@yahoo.com", "Roommate"},
	    {"Blair", "O'Conner",
	     "4042328938", "QueenBConner@outlook.com", "Leasing Office Lady"}
	 	};
 	public addressbook() {
 		t1 = new JTable(data, columnNames);
 		book = new JPanel();
 		book.setLayout(new BorderLayout());
 		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
	    renderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

 		t1.setPreferredScrollableViewportSize(new Dimension(500, 100));
        t1.setFillsViewportHeight(true);
    
        	t1.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                	index = t1.getSelectedRow();
                    printDebugData(t1, index);
                }
            });
        
        jsp1 = new JScrollPane(t1);
        text1 = new JTextArea();
        text1.setEditable(false);
        book.add(jsp1, BorderLayout.NORTH);
        book.add(text1, BorderLayout.CENTER);

 	}
 	public void printDebugData(JTable table, int index) {
 		int row = t1.getRowCount();
 		int col = t1.getColumnCount();
 		String detail = "Contact Detail \n";
 		for (int i = 0; i < row; i++) {
 			if (i == index) {
 				for (int j=0; j < col; j++) {
                detail += columnNames[j] +" : "+String.valueOf(data[i][j]) + "\n";
            	}
 			}
 		}
 		text1.setText(detail);
 	}
 	public JPanel getAddressBook() {
 		return book;
 	}


}
