import java.awt.Dimension;
import java.awt.FontMetrics;

import javax.swing.JButton;

public class LessonButton extends JButton {

	
	public LessonButton(){}
	
	public LessonButton(String text)
	{
		setText(text);
	}
	
	@Override
	public void setText(String text)
	{
		super.setText(text);
		
		FontMetrics metrics = getFontMetrics(getFont());
		int width = metrics.stringWidth(getText());
		int height = metrics.getHeight();
		Dimension dim = new Dimension(width + 40, height);
		this.setPreferredSize(dim);
	}
}
