import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.event.*;

/////////////////////extends Jcomponent ///////////////////////////////
public class InkPageContainer extends JComponent implements ChangeListener {
	/////////call functions
    InkBookFunctions cf1;
    int page_number;
    int width = 0, height = 0;
    private ActionListener listener;
    private InkPage pageContainer1;
    public InkPageContainer() {
    	
        setModel(new InkBookFunctions());
        updateUI();
        this.setFocusable(true);
        
    }
    public void setModel(InkBookFunctions cf) {
        if (cf1 != null)  {
           cf1.removeChangeListener(this);
        }
        cf1 = cf;
        cf1.addChangeListener(this);
    }
    public InkBookFunctions getModel() {
        return cf1;
    }
    public void setUI(InkPage pageContainer1) { 
    	this.pageContainer1 = pageContainer1;
    	super.setUI(pageContainer1); 
    }
    
    public InkPage getUI() {
    	return pageContainer1;
    }
    
    public void updateUI() {
        setUI((InkPage) new InkPage());
        invalidate();
    }
    
	public void addActionListener(ActionListener listener) {
		this.listener = listener;
	}
    
    @Override
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	
    }
    
	@Override
	public void stateChanged(ChangeEvent e) {
		
		if(this.getWidth() > width) {
			width = this.getWidth();
		}
		if(this.getHeight() > height) {
			height = this.getHeight();
		}
		this.setMaximumSize(new Dimension(1000,1000));
		this.setMinimumSize(new Dimension(width,height));
		this.setPreferredSize(new Dimension(width,height));

		repaint();
	}
	

	///////////////////corresponding changes 
	public static void setRadioChoice(String text) {
		InkBookFunctions.setRadioChoice(text);
	}
	public static void setTextChoice(String text) {
		InkBookFunctions.setTextChoice(text);
	}
	
	public int getMaxWidth() {
		return width;
	}
	
	public int getMaxHeight() {
		return height;
	}
	
	public void resized(int width, int height) {
		if(width == 0) {
			width = width;
		}
		if(height == 0) {
			height = height;
		}
		stateChanged(new ChangeEvent(this));
	}

	public void setColor(Color newColor) {
		cf1.setColor(newColor);
	}
	// public Color getColor() {
	// 	return cf1.getColor();
	// }
	
	public String getGesture() {
		return cf1.knowGesture();
	}
	

	//aborted red and yellow 
	public void fireGestureEvent() {
		String gesture = getGesture();
		if(gesture.equals("Delete")) {
			cf1.deleteDrawing();
		} else if(gesture.equals("Select")) {
			cf1.getStuffFromCorp();
			cf1.setIsSelect(true);
		} else if (gesture.equals("Red")) {
			cf1.setColor(Color.RED);
		} else if (gesture.equals("Yellow")) {
			cf1.setColor(Color.YELLOW);
		}
		listener.actionPerformed(new ActionEvent(this,1,gesture));
	}
	
	
}