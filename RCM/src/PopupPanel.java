import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PopupPanel extends JPanel {
	
	private String title;
	
	PopupFrame frame;
	
	public PopupPanel (String title)
	{
		this.title = title;
	}
	
	public void setFrame(PopupFrame frame)
	{
		this.frame = frame;
	}
	
	public PopupFrame getFrame()
	{
		return this.frame;
	}
	
	public String getTitle()
	{
		return title;
	}
}
