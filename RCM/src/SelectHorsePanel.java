import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class SelectHorsePanel extends JPanel {
	
	Model model;
	SpringLayout layout;
	
	//The elements on this panel
	JLabel horse;
	JButton varientB, cancelB;
	JComboBox<Horse> horseComboBox;
	
	//Whether we are updating a horse or removing a horse (change into 2 subclasses)
	String varient;
	
	public SelectHorsePanel (Model model, String varient){
		
		this.model = model;
		MainFrame frame = model.frame;
		layout = new SpringLayout();
		this.setLayout(layout);
		
		this.varient = varient;
		
		horse = new JLabel("Horse: ");
		horseComboBox = new JComboBox<Horse>();
		
		varientB = new JButton(varient);
		cancelB = new JButton("Cancel");
		
		horseComboBox.addItem(null);
		
		ArrayList<Horse> horseList = model.getHorseList();
		for (int i = 0; i < horseList.size(); i++)
		{
			horseComboBox.addItem(horseList.get(i));
		}
		
		addActionListener();
		buildUI();
				
		frame.getContentPane().add(this, BorderLayout.CENTER);
		
		frame.currentPanel = this;
	}
	
	public void addActionListener(){
		varientB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Object p = horseComboBox.getSelectedItem();
				//create error if no horse is selected
				if (p == null){
					JOptionPane.showMessageDialog(null, "Need to select a Horse", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					if (varient.equals("Update"))
					{
						//model.horseUpdateScreen((Horse)p);
						model.frame.currentPanel.setVisible(false);
						HorsePanel hp = new HorsePanel(model, (Horse)p, "Update");
					}
					else
					{
						model.removeHorse((Horse)p);
						model.toMainMenu();
					}
				}
			}
		});
		
		cancelB.addActionListener(model);
	}
	
	public void buildUI(){
		
		this.add(horse);
		this.add(horseComboBox);
		
		this.add(varientB);
		this.add(cancelB);
		
		layout.putConstraint(SpringLayout.WEST, horse, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, horse, 150, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, horseComboBox, 140, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, horseComboBox, 150, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, varientB, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, varientB, 200, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, cancelB, 160, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, cancelB, 200, SpringLayout.NORTH, this);
		
	}

}
