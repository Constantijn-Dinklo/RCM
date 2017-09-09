import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

	public static void main(String args[]) {
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(ClassNotFoundException e) {}
		catch(InstantiationException e) {}
		catch(IllegalAccessException e) {}
		catch(UnsupportedLookAndFeelException e) {}
		Model m = new Model();
	}
}
