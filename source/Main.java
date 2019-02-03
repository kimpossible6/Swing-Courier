/* Main Class
 * Ziwei Miao 
 * CS 4470
*/
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame{
    private JFrame f1;
    private JSplitPane s1;
    private JPanel p1;
    private JPanel p2;
    private JPanel p3;
    private JPanel p4;
    private JPanel p5;
    private JButton b1;
    private JLabel l1;
    private JScrollPane jsp1;
    // private JLabel status;
    private JTabbedPane t1;
    private JEditorPane e1;
    private JPanel statusbar;
    inkbook inkP;
    public Main() {
       guiFrame();
    }
    public void guiFrame() {
        f1 = new JFrame("Microsoft Courier");
        f1.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        f1.setLayout(new BorderLayout());
        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // f1.setSize(1280,800);
        f1.setMinimumSize(new Dimension(1280, 800));
        // jsp1 = new JScrollPane();
        // p1 = new JPanel();
        // p3 = new JPanel();
        // p4 = new JPanel();
        p5 = new JPanel();
        e1 = new JEditorPane();
        t1 = new JTabbedPane();
        statusbar = new JPanel();
        statusbar.setFocusable(false);
        inkP = new inkbook();
        jsp1 = new JScrollPane(inkP.getinkbook());

        s1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        s1.setLeftComponent(t1);
        s1.setRightComponent(jsp1);
        s1.setResizeWeight(0.5f);
        s1.setFocusable(false);
        f1.add(s1);
        f1.add(statusbar, BorderLayout.SOUTH);
        // p2 = new JPanel();
        // call web browser 
        webbrowse wp1 =  new webbrowse("https://www.google.com/");
        JPanel browser = wp1.editHTML();


        // call addressbook 
        addressbook adbook = new addressbook();
        p2 = adbook.getAddressBook();
        p2.setFocusable(false);
        browser.setFocusable(false);
        // call inkbook
        
        JLabel status = inkP.getButtonStatus();
        statusbar.add(status);

        // call calendar
        calendar c1 = new calendar();
        p3 = c1.getCalendar();

        // call photo 
        photo photo1 = new photo();
        p4 = photo1.getphoto();

        b1 = new JButton("Test");
        l1 = new JLabel("This is test label1");
        t1.add("Web Browser", browser);
        t1.add("Address Book", p2);
        t1.add("Calendar", p3);
        t1.add("Photo Galleries", p4);
        t1.add("Map Viewer", p5);
        t1.setMinimumSize(new Dimension(650,600));
        f1.pack();
        f1.setVisible(true);
    }

    public static void main(String[] args){
        new Main();
    }
}
