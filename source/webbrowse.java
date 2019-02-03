/* WebBrowser
 * Ziwei Miao 
*/

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.event.*;
import static javax.swing.ScrollPaneConstants.*;
import java.util.*; 
public class webbrowse extends JFrame implements ActionListener, HyperlinkListener{
	//url text editor
	private JTextField urlfield;
	//editor pane for url page
	private JEditorPane ep1;
	//buttons
	private JButton b1Go;
	private JButton b2Back;
	private JButton b3Next;
	//button panel
	private JPanel addresseditor;
	private JPanel buttonP;
	//scrollable pane for ep1
	private JScrollPane scroll;
	//url address
	private String url;
	//array to store urls
	private ArrayList<String> navigation = new ArrayList<String>();
	public webbrowse(String url) {
		this.url = url;
		webLayout();
	}
	public void webLayout() {
		buttonP = new JPanel();
		addresseditor = new JPanel();
		JLabel urlLabel= new JLabel("URL:");
		b1Go = new JButton("Search");
		b1Go.addActionListener(this);    
		b1Go.setFocusable(false);
		b2Back = new JButton("<- Back");
		b3Next = new JButton("Next ->");
		urlfield = new JTextField(20);
		urlfield.setText(url);
		urlfield.addActionListener(this);

		b2Back.setEnabled(false);
		b2Back.setFocusable(false);
		b2Back.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				back();
			}
		});

		b3Next.setEnabled(false);
		b3Next.setFocusable(false);
		b3Next.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				next();
			}
		});

		buttonP.add(b2Back);
		buttonP.add(b3Next);
		buttonP.add(urlLabel);
		buttonP.add(urlfield);
		buttonP.add(b1Go);
		
		// getContentPane().add(buttonP,BorderLayout.NORTH);
		try {
			ep1 = new JEditorPane(url);
			navigation.add(url);
			ep1.setEditable(false);
			ep1.setFocusable(false);
			addresseditor.add(ep1, BorderLayout.CENTER);
			ep1.addHyperlinkListener(this);
			scroll = new JScrollPane(ep1);
			scroll.setFocusable(false);
			scroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
			// getContentPane().add(scrollhtml, BorderLayout.CENTER);
		} catch (IOException o) {
			showerror("Pane built failed");
		}
		addresseditor.setLayout(new BorderLayout());
		addresseditor.add(buttonP,BorderLayout.NORTH);
		addresseditor.add(scroll, BorderLayout.CENTER);
		addresseditor.setFocusable(false);
	}

	public JPanel editHTML() {
		return addresseditor;
	}
	public void updateButton() {
		if(navigation.size() < 2) {
			b2Back.setEnabled(false);
			b3Next.setEnabled(false);
		} else {
			URL current = ep1.getPage();
			int index = navigation.indexOf(current.toString());
			if(index > 0) {
				if (index == 1) {
					b2Back.setEnabled(false);
					b3Next.setEnabled(false);
				} else {
					b3Next.setEnabled(true);
				}
			}
			if (index < (navigation.size() - 1)) {
				if (index == navigation.size() - 2) {
					b3Next.setEnabled(false);
				} else {
					b2Back.setEnabled(true);
				}
			} else {
				b3Next.setEnabled(false);
			}
		}
	}
	public void next() {
		URL current = ep1.getPage();
		int index = navigation.indexOf(current.toString());
		try {
			showPage(new URL((String)navigation.get(index + 1)));
		} catch(Exception o) {
			showerror("Can't load the page");
		}
	}

	public void back() {
		URL current = ep1.getPage();
		int index = navigation.indexOf(current.toString());
		try {
			showPage(new URL((String)navigation.get(index-1)));
		} catch (Exception o) {
			showerror("Can't load the page");
		}
	}
	public void showPage(URL url) {
		try {
			URL current = ep1.getPage();
			ep1.setPage(url);
			URL newUrl = ep1.getPage();
			urlfield.setText(url.toString());
			int navigationSize = navigation.size();
			// if (navigationSize > 0) {
			// 	int index = navigation.indexOf(current.toString());
			// 	if (index < navigationSize - 1) {
   //                      for (int i = navigationSize - 1; i > index; i--) {
   //                          navigation.remove(i);
   //                      }
   //              }
			// }
			// System.out.println(navigation.size());
			navigation.add(newUrl.toString());
		} catch(MalformedURLException o) {
			showerror("Can't open url");
		} catch (IOException o) {
			o.printStackTrace();
		}
		updateButton();
	}
	public void actionPerformed(ActionEvent e){
		String url2;
		if (e.getSource() == urlfield || e.getSource() == b1Go) {
			url2 = urlfield.getText();
		} else {
			url2 = url;
		}
		try {
			// ep1.setPage(new URL(url2));
			navigation.add(url2);
			URL url = new URL(urlfield.getText());
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					// b2Back.setEnabled(false);
					showPage(url);		
				}
			});

		} catch (Exception o) {
			showerror("Link: "+ o + " is invalid!");
		}
	}

	public void hyperlinkUpdate(HyperlinkEvent e) {
	    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
	    	try {
		        ep1.setPage(e.getURL());
		        urlfield.setText(e.getURL().toExternalForm());
	    	} catch(IOException o) {
	        	showerror("Can't update hyperlink " + e.getURL().toExternalForm() + ": " + o);
	    	}
	    }
  	}

  	private void showerror(String message) {
    	JOptionPane.showMessageDialog(this, message, "Error", 
                                  JOptionPane.ERROR_MESSAGE);
	}
	
}