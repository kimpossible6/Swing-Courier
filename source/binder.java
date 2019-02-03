import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class binder extends JPanel {
    private int xCoord;
    private int yCoord;
    private int height;

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }
    public void setHeight(int Height) {
        this.height = Height;
    }

    public binder(int x, int y) {
        this.xCoord = x;
        this.yCoord = y;
    }

    public void moveBinder(int xUnits, int yUnits) {
        xCoord += xUnits;
        yCoord += yUnits;
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE);
        g2d.fill(new Rectangle2D.Double(xCoord, yCoord, 20, height));
    }
}


