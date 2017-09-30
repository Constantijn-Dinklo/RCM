import javax.swing.JComboBox;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;


public class CustomComboBox<E> extends JComboBox<E> {

	public CustomComboBox ()
	{
		//The hope is to make this feel better, but not very important right now, so just keep it this way.
		AutoCompleteDecorator.decorate(this);
	}
}
