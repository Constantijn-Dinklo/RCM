import java.awt.Dimension;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
	
	JPanel currentPanel;

	public MainFrame () {
		
		this.setTitle("Planner");
		this.pack();
		this.setSize(new Dimension(1300,800));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	
	public void display(){
		
		this.setSize(currentPanel.getPreferredSize());
		
		this.setLocationRelativeTo(null);
		this.setResizable(true); //for easier UI design, not modifying the elements when resizing yet.
		this.setVisible(true);
	}
	
	public void close()
	{
		currentPanel.setVisible(false);
		this.remove(currentPanel);
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	
}
