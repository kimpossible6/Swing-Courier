import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import java.awt.font.GlyphVector;
import java.awt.font.FontRenderContext;

public class InkPage extends NewInkPage implements MouseListener, MouseMotionListener, KeyListener{
    private InkPageContainer pc1;
    // private InkBookFunctions = pc1.getModel();
    private Graphics2D g2;
    private Color prevColor;
    private int prevX=-1, prevY=-1;

    //////////text obj/////////////////
    private int mx, my, mx1, my1;
    private ArrayList<textObj> sticky = new ArrayList<>();
    private textObj currentP;
    

    //For PostIt
    private Rectangle textbox;
    private ArrayList<Rectangle> textboxes;
    private point textpoint;
    Font font1;
    private FontMetrics fontM;



    public static boolean triggerGrabPrevious = false;
    public static boolean triggerGrabNext = false;
    //New change//////////////////////////////////////////////////////////////

    
    public static ComponentUI createUI(JComponent c) {
        return new InkPage();
    }
    /////////////install inkpage container///////////////////////
    public void installUI(JComponent c) {
        pc1 = (InkPageContainer) c;
        c.setLayout(new BorderLayout());
        pc1.addMouseListener(this);
        pc1.addMouseMotionListener(this);
        pc1.addKeyListener(this);
    }
        
    
    public void uninstallUI(JComponent c) {
        
        ((InkPageContainer) c).removeMouseListener(this);
    }
    


   
    public void paint(Graphics g, JComponent c) {
        g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawBg(g2);
        drawStrokes(g2);
        g2.setColor(pc1.getModel().getColor());
        font1 = new Font("SansSerif",Font.BOLD,14);
        fontM = g.getFontMetrics(font1);
        int height = 0;
        stickyNote(g2, font1, fontM, height);
        if(pc1.getModel().getIsSelect()) {
            Rectangle bound = pc1.getModel().getSelectionBounds();
            g2.setColor(Color.RED);
            g2.draw(bound);
        }



        double width = 300;
        double height1 = 400;
        Path2D.Double path = new Path2D.Double();
//        path.moveTo(0.0, 0.0);
//        path.lineTo(width - 8.0, 0.0);
//        path.curveTo(0.0, 0.0, 0, 8.0, 0, 30);
//        path.lineTo(0, 30);
//        path.curveTo(0.0, 0.0, 8.0, 0.0, 8.0, 0.0);
//        path.lineTo(width - 8.0, 0.0);
//        path.curveTo(width, 0.0, width, 8.0, width, 8.0);
//        path.lineTo(width, height1 - 8.0);
//        path.curveTo(width, height1, width - 8.0, height1, width - 8.0, height1);
//        path.lineTo(8.0, height1);
//        path.curveTo(0.0, height1, 0.0, height1 - 8.0, 0, height1 - 8.0);
//        path.closePath();
//        g2.fill(path);

        ///////code changed
        if(pc1.grabEnable) {
            path.moveTo(0, 0.0);
            path.lineTo(pc1.getWidth(), 0.0);
            path.curveTo(pc1.getWidth() -  pc1.getModel().xStart, 0.0, 0, 100, 0,  pc1.getModel().yCurrent + 100);
            path.lineTo(0,  pc1.getModel().yCurrent + 100);
            path.closePath();
            g2.fill(path);
//            g2.fillRect(pc1.getWidth() -  pc1.getModel().xStart,  0, pc1.getModel().xCurrent, pc1.getModel().yCurrent);
        }
        if (pc1.grabEnable2) {
            g2.setColor(Color.WHITE);
            g2.fillRect(pc1.getModel().xCurrent + 2,  0, pc1.getWidth(), pc1.getHeight());
        }



    }



    ////////////draw backgound
    public void drawBg(Graphics2D g2) {
        int width = pc1.getWidth();
        int height = pc1.getHeight();
        int horizontal = 40;
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        g2.setColor(Color.BLUE);
        do {
            g2.drawLine(0, horizontal, width, horizontal);
            horizontal += 40;
        }while(horizontal <= height - 40);
    }
   


    ///Draw rect 
    public void drawPerfectRect(Graphics g2, int x, int y, int x1, int y1) {
        int px = Math.min(x,x1);
        int py = Math.min(y,y1);
        int pw=Math.abs(x-x1);
        int ph=Math.abs(y-y1);
        g2.drawRect(px, py, pw, ph);
    }

    //draw oval
    public void drawPerfectOval(Graphics g2, int ox, int oy, int ox1, int oy1) {
        int px = Math.min(ox, ox1);
        int py = Math.min(oy, oy1);
        int pw = Math.abs(ox - ox1);
        int ph = Math.abs(oy - oy1);
        g2.drawOval(px, py, pw, ph);
    }


    //draw inks 
    public void drawStrokes(Graphics2D g2) {
        List<Stroke> strokes = pc1.getModel().getStrokes();
        int width;
        int height;
        for(int i = 0; i < strokes.size(); i++) {
            Stroke stroke = strokes.get(i);
            g2.setColor(stroke.getColor());
            width = stroke.getX2() - stroke.getX();
            height = stroke.getY2() - stroke.getY();
            if(stroke.getChoice().equals("Ink")) {
                List<point> points = stroke.getPoints();
                for(int j = 0; j < points.size(); j++) {
                    if (j == points.size() - 1) {
                        g2.drawLine(points.get(j).getX(), points.get(j).getY(), stroke.getX2(), stroke.getY2());
                    } else if(j > 1){
                        g2.drawLine(points.get(j-1).getX(), points.get(j-1).getY(), points.get(j).getX(), points.get(j).getY());
                    }
                }
            } else if (stroke.getChoice().equals("Rectangle")) {
                drawPerfectRect(g2, stroke.getX(), stroke.getY(), stroke.getX2(),stroke.getY2()); 
            } else if (stroke.getChoice().equals("Oval")) {
                drawPerfectOval(g2, stroke.getX(), stroke.getY(), stroke.getX2(), stroke.getY2());
            }
        }

        Stroke curStroke = pc1.getModel().getDrawStroke();
        if(curStroke != null) {
            g2.setColor(curStroke.getColor());
            width = curStroke.getX2() - curStroke.getX();
            height = curStroke.getY2() - curStroke.getY();
            if(curStroke.getChoice().equals("Ink")) {
                List<point> points = curStroke.getPoints();
                for(int i = 0; i < points.size(); i++) {
                    if (i == points.size() - 1) {
                        g2.drawLine(points.get(i).getX(), points.get(i).getY(), curStroke.getX2(), curStroke.getY2());
                    } else if(i > 1){
                        g2.drawLine(points.get(i-1).getX(), points.get(i-1).getY(), points.get(i).getX(), points.get(i).getY());
                    }
                }
            } else if (curStroke.getChoice().equals("Rectangle")) {
                drawPerfectRect(g2, curStroke.getX(), curStroke.getY(), curStroke.getX2(), curStroke.getY2());
            } else if (curStroke.getChoice().equals("Oval")) {
                drawPerfectOval(g2, curStroke.getX(), curStroke.getY(), curStroke.getX2(), curStroke.getY2());
            }
        }
    }

    public void drawTextRect(Graphics g, int width, int height, int x, int y) {
        textbox = new Rectangle(x, y, width, height);
        // g.drawRect(textbox);
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, width, height);
    } 

    private Rectangle getStringBounds(Graphics2D g2, String str,
                                      float x, float y)
    {
        FontRenderContext f1 = g2.getFontRenderContext();
        GlyphVector gv = g2.getFont().createGlyphVector(f1, str);
        return gv.getPixelBounds(null, x, y);
    }

    public void stickyNote(Graphics2D g, Font font1, FontMetrics fontM, int height){
        
        g.setFont(font1);
        for(textObj t: sticky){
            height = fontM.getAscent() + fontM.getDescent() + fontM.getLeading();
            int x = (int)t.x;
            int y = (int)t.y; 
            int width = 0;
            int nIndex = 0;
            String textTodraw = t.getText();
            if ((t.getRectangle()).getHeight() < height - 1) {
                int rx = (int)t.getRectangle().getX();
                int ry = (int)t.getRectangle().getY();
                int rw = (int)t.getRectangle().getWidth();
                Rectangle newbox = new Rectangle(rx, ry, rw, (int)(t.getRectangle()).getHeight() + height);
                t.setRectangle(newbox);
            }

            int rx = (int)t.getRectangle().getX();
            int ry = (int)t.getRectangle().getY();
            int rw = (int)t.getRectangle().getWidth();
            int rh = (int)t.getRectangle().getHeight();
            g.setColor(Color.YELLOW);
            g.fillRect(rx, ry, rw, rh);
            g.setColor(t.color);
            if (textTodraw.length() < 16) {
                System.out.println(textTodraw);
                g.drawString(textTodraw, x, y);
                System.out.println(fontM.stringWidth(textTodraw));
            } else {
                Rectangle2D bounds = fontM.getStringBounds("Hello World this is fantastic", g);
                for (int i = 0; i < textTodraw.length()/16; i++) {

                    String currentLine = textTodraw.substring(16 * i, 16 * (i + 1)) + '-';
                    g.drawString(currentLine, x, y);
                    y += fontM.getHeight();
                    System.out.println("new rec" + (t.getRectangle()).getHeight());
                    System.out.println("new rec" + (int)bounds.getHeight() * i);
                    if ((t.getRectangle()).getHeight() < (int)bounds.getHeight() * i + 40) {
                        int rx2 = (int)t.getRectangle().getX();
                        int ry2 = (int)t.getRectangle().getY();
                        int rw2 = (int)t.getRectangle().getWidth();
                        int start = (int)t.getRectangle().getY() + (int)bounds.getHeight();
                        Rectangle newbox = new Rectangle(rx2, ry2, rw2, (int)(t.getRectangle()).getHeight() + (int)bounds.getHeight() + 3);
                        t.setRectangle(newbox);
                        g.fillRect(rx2, start, rw2, (int)(t.getRectangle()).getHeight() + 3);
                        System.out.println("new rec");
                    }
                } 
                
        
            }
        }

    }

    
    @Override
    public void mouseClicked(MouseEvent e) {
        String curStroke = pc1.getModel().getTextChoice();
        System.out.println(curStroke);
//        System.out.println("clicked1");

        if (curStroke.equals("Text")) {
                mx = e.getX() + 15;
                my = e.getY() - 15;
                currentP = new textObj(mx, my);
                currentP.setColor(Color.BLUE);
                currentP.setFont(new Font("HelveticaNeue-Italic", Font.PLAIN, 13));
                currentP.setRectangle(new Rectangle(mx - 15, my - 30, 200, 200));
                sticky.add(currentP);
                System.out.println("clicked");
        }
       
        pc1.requestFocusInWindow();
    }
    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            if(pc1.getModel().getIsSelect()) {
                pc1.getModel().setIsGesture(false);
                prevX = e.getX();
                prevY = e.getY();
                if(!pc1.getModel().getSelectionBounds().contains(e.getX(),e.getY())) {
                    pc1.getModel().setIsSelect(false);
                    pc1.getModel().deselect();
                }
            }
            else{
                pc1.getModel().setXStart(e.getX());
                pc1.getModel().setYStart(e.getY());
                pc1.getModel().setXCurrent(e.getX());
                pc1.getModel().setYCurrent(e.getY());
                pc1.getModel().getInkState();
                pc1.getModel().setIsGesture(false);
            }
        } else if(e.getButton() == MouseEvent.BUTTON3) {
//            Color newcolor2 = pc1.getModel().getColor();
//            System.out.println("new color" + newcolor2);
            if(pc1.getModel().getColor() != Color.RED) {
                prevColor = pc1.getModel().getColor();
                pc1.getModel().setColor(Color.RED);
            } else {
                pc1.getModel().setColor(Color.BLUE);
            }
            pc1.getModel().setXStart(e.getX());
            pc1.getModel().setYStart(e.getY());
            pc1.getModel().setXCurrent(e.getX());
            pc1.getModel().setYCurrent(e.getY());
            if(pc1.getModel().getInkState()) {
                pc1.getModel().setIsGesture(true);
                
            } else {
                pc1.getModel().setIsGesture(false);
                pc1.getModel().setColor(Color.BLUE);
            }
            
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            System.out.println("Button2");
            System.out.println();
//            Color newcolor2 = pc1.getModel().getColor();
            pc1.getModel().setColor(Color.BLUE);
            pc1.getModel().setIsGesture(false);
            pc1.getModel().setIsSelect(false);
            pc1.getModel().deselect();
            pc1.getModel().resetStroke();
        } else {
            pc1.getModel().setIsGesture(false);
            pc1.getModel().setIsSelect(false);
            pc1.getModel().setColor(Color.BLUE);
            pc1.getModel().deselect();
            pc1.getModel().resetStroke();
        }
        
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if(pc1.getModel().getIsSelect()) {
            return;
        }
        if(pc1.getModel().getIsGesture()) {
            if(pc1.getModel().getGesture()) {
                pc1.fireGestureEvent();
            }
            pc1.getModel().isDrawing(false);
            pc1.getModel().resetStroke();
            pc1.getModel().setColor(prevColor);
        } else{

            pc1.getModel().setStroke(e.getX(),e.getY());
            pc1.getModel().isDrawing(false);
        }


        
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        
    }
    @Override
    public void mouseExited(MouseEvent e) {
        
    }
   
    @Override
    public void mouseDragged(MouseEvent e) {
        if(pc1.getModel().getIsSelect()) {
            pc1.getModel().moveCorp(e.getX() - prevX, e.getY() -prevY);
            prevX = e.getX();
            prevY = e.getY();
        } else {
            pc1.getModel().setXCurrent(e.getX());
            pc1.getModel().setYCurrent(e.getY());
            pc1.getModel().getInkState();
            pc1.getModel().isDrawing(true);
            if (pc1.getModel().xStart > 500 && pc1.getModel().yStart < 100) {
                pc1.grabEnable = true;
               if (pc1.getModel().yCurrent > pc1.getHeight()/2) {
                   pc1.grabEnable = false;
               }
                return;
            }
            pc1.grabEnable = false;
            if (pc1.getModel().xStart > 500 && pc1.getModel().yStart > 150 && pc1.getModel().yStart < 500) {
                pc1.grabEnable2 = true;
//                System.out.println("grab is true"+ pc1.getWidth() + pc1.getModel().xStart +  pc1.getModel().yStart);
                if (pc1.getModel().xCurrent <= pc1.getWidth()/2) {
                    System.out.println("time to call previous" + pc1.swipeBack);
                    pc1.grabEnable2 = false;
                    pc1.swipeBack = true;
                    triggerGrabPrevious = true;
                }
            }

        }
        
                
    }


    
    @Override
    public void mouseMoved(MouseEvent e) {
        
    }
    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar() == '\u0008'){
            if (sticky.size() > 0) {
                String character = sticky.get(sticky.size()-1).getText();
                sticky.get(sticky.size()-1).text = character.substring(0, character.length()-1);
            }
                    
        }
        else {
            currentP.setText(e.getKeyChar());
        }

        
    }
    @Override
    public void keyPressed(KeyEvent e) {
        
    }
    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    
}