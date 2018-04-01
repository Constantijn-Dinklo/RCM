import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PopupFrame extends JFrame {

	PopupPanel panel;
	
	
	public PopupFrame (PopupPanel panel)
	{
		this.panel = panel;
		panel.setFrame(this);
		
		this.setTitle(panel.getTitle());
		
		this.getContentPane().add(panel, BorderLayout.CENTER);
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void display(){
		
		this.setSize(panel.getPreferredSize());
		this.setLocationRelativeTo(null);
		this.setResizable(true); //for easier UI design, not modifying the elements when resizing yet.
		this.setVisible(true);
	}
	
	public void close()
	{
		panel.setVisible(false);
		this.remove(panel);
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
}
