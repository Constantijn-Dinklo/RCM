import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
	
	JPanel currentPanel;

	public MainFrame () {
		
		this.setTitle("Planner");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	
	public void display(){
		
		this.pack();
		this.setSize(new Dimension(500, 700));
		this.setLocationRelativeTo(null);
		this.setResizable(false); //for easier UI design for now!
		this.setVisible(true);
	}
	
}
