import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.applet.*;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
public class photo extends JApplet {
	//Create a JPanel
	private JPanel p1;
	//Create Labels 
	private JLabel l1,l2,l3,l4;
	public photo() {
		p1 = new JPanel(new GridLayout(2,2));
		l1 = new JLabel();
		l2 = new JLabel();
		l3 = new JLabel();
		l4 = new JLabel();

		try {
			String path1 = "https://i.imgur.com/1fHpRu1.jpg";
			String path2 = "https://i.imgur.com/ET6Qw6p.jpg";
			String path3 = "https://i.imgur.com/6RJmSPS.jpg";
			String path4 = "https://i.imgur.com/rgGCqi8.jpg";

			URL url1 = new URL(path1);
			URL url2 = new URL(path2);
			URL url3 = new URL(path3);
			URL url4 = new URL(path4);

			BufferedImage image1 = ImageIO.read(url1);
			BufferedImage image2 = ImageIO.read(url2);
			BufferedImage image3 = ImageIO.read(url3);
			BufferedImage image4 = ImageIO.read(url4);

			l1.setIcon(new ImageIcon(new ImageIcon(image1).getImage().getScaledInstance(250, 280, Image.SCALE_DEFAULT)));
            l2.setIcon(new ImageIcon(new ImageIcon(image2).getImage().getScaledInstance(250, 280, Image.SCALE_DEFAULT)));
            l3.setIcon(new ImageIcon(new ImageIcon(image3).getImage().getScaledInstance(250, 280, Image.SCALE_DEFAULT)));
            l4.setIcon(new ImageIcon(new ImageIcon(image4).getImage().getScaledInstance(250, 280, Image.SCALE_DEFAULT)));

            l1.setHorizontalAlignment(JLabel.CENTER);
            l2.setHorizontalAlignment(JLabel.CENTER);
            l3.setHorizontalAlignment(JLabel.CENTER);
            l4.setHorizontalAlignment(JLabel.CENTER);
		} catch (Exception e) {
            e.printStackTrace();
        }
        p1.add(l1);
        p1.add(l2);
        p1.add(l3);
        p1.add(l4);
        p1.setBackground(Color.GRAY);
        p1.setFocusable(false);
	}
	//Return JPanel when calling from other class
	public JPanel getphoto() {
		return p1;
	}
}