import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class SelectPersonPanel extends PopupPanel {
	
	Model model;
	SpringLayout layout;
	
	JLabel personL;
	JButton varientB, cancelB;
	JComboBox<Person> selectPerson;
	
	String varient;
	
	public SelectPersonPanel(Model model, String varient){
		super("Select Person");
		
		this.model = model;
		MainFrame frame = model.frame;
		layout = new SpringLayout();
		this.setLayout(layout);
		
		this.varient = varient;
		
		personL = new JLabel("Person: ");
		selectPerson = new JComboBox<Person>();
		
		varientB = new JButton(varient);
		cancelB = new JButton("Cancel");
		
		addComboBoxValues();
		addActionListener();
		buildUI();
		
		this.setPreferredSize(new Dimension(500, 800));
	}
	
	private void addActionListener(){
		SelectPersonPanel that = this;
		varientB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Object p = selectPerson.getSelectedItem();
				if (p == null){
					JOptionPane.showMessageDialog(null, "Need to select a Person", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					if (varient.equals("Update"))
					{
						getFrame().close();
						new PopupFrame(new PersonPanel(model, (Person)p, "Update")).display();
						
					}
					else
					{
						model.removePerson((Person)p);
						getFrame().close();
					}
				}
			}
		});
		
		cancelB.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				getFrame().close();
			}
		});
	}
	
	private void addComboBoxValues(){
		ArrayList<Person> personList = model.getPersonList();
		selectPerson.addItem(null);
		for (int i = 0; i < personList.size(); i++)
		{
			selectPerson.addItem(personList.get(i));
		}
	}
	
	private void buildUI(){
		this.add(personL);
		this.add(selectPerson);
		
		this.add(varientB);
		this.add(cancelB);
		
		layout.putConstraint(SpringLayout.WEST, personL, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, personL, 150, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, selectPerson, 140, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, selectPerson, 150, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, varientB, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, varientB, 200, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, cancelB, 160, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, cancelB, 200, SpringLayout.NORTH, this);
	}

}
