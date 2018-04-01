import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class HorsePanel extends PopupPanel{
	
	SpringLayout layout;
	Model model;
	JLabel nameL, maxHoursL, minHoursL, breakL, jumpL;
	JTextField nameT, maxHoursT, minHoursT, breakT;
	JCheckBox jumpC; 
	JButton addUpdateB, cancelB;
	
	//The current horse that is being updated, in case a new horse is being added, this is null
	Horse curHorse;
	
	public HorsePanel (Model model, Horse horse, String varient)
	{
		super("Add Horse");
		
		this.model = model;
		MainFrame frame = model.frame;
		curHorse = horse;
		layout = new SpringLayout();
		this.setLayout(layout);

		nameL = new JLabel("Name:");
		maxHoursL = new JLabel("Max Hours:");
		minHoursL = new JLabel("Min Hours:");
		breakL = new JLabel("Break Time:");
		jumpL = new JLabel("Can Jump:");

		nameT = new JTextField("name");
		maxHoursT = new JTextField("0");
		minHoursT = new JTextField("0");
		breakT = new JTextField("0");
		jumpC = new JCheckBox("", false);
		
		if (horse != null)
		{
			nameT.setText(horse.getName());
			nameT.setEditable(false);
			maxHoursT.setText(Integer.toString(horse.getMaxTime() / 3600));
			minHoursT.setText(Integer.toString(horse.getMinTime() / 3600));
		}

		addUpdateB = new JButton(varient);
		cancelB = new JButton("Cancel");

		addActionListeners();
		buildUI();

		this.setPreferredSize(new Dimension(500, 800));
	}
	
	private void addActionListeners(){
		//Action listener for add button
		//Performs the logic of adding a new horse.
		//Has separate actionListener because it needs local variables.
		addUpdateB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = nameT.getText();
				if(curHorse == null && model.getHorseListByName().containsKey(name))
				{
					System.out.println("Name already exists");
					JOptionPane.showMessageDialog(null, "A horse with this name already exists", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				int maxTime, minTime, breakTime;
				ArrayList<LessonType> types = new ArrayList<LessonType>();
				try
				{
					maxTime = Integer.parseInt(maxHoursT.getText().trim());
					maxTime = maxTime * 3600; //This modifies the time to maximum time in milliseconds 
				}
				catch(Exception ex)
				{
					System.out.println("Need integer");
					JOptionPane.showMessageDialog(null, "Need a number for Max Hours", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try
				{
					minTime = Integer.parseInt(minHoursT.getText().trim());
					minTime = minTime * 3600; //same as above
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null, "Need a number for Min Hours", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try
				{
					breakTime = Integer.parseInt(breakT.getText().trim());
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null, "Need a number for Break Time", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (jumpC.isSelected())
				{
					types.add(LessonType.Jump);
				}
				
				//Adding a horse
				if (curHorse == null){
					model.addHorse(new Horse(name, maxTime, minTime, breakTime, types));
				}
				//Updating a horse
				else
				{
					model.updateHorse(curHorse, maxTime, minTime);
				}
				getFrame().close();
			}
		});

		cancelB.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				getFrame().close();
			}
		});
	}
	
	private void buildUI(){
		//Adds all components to the JPanel and sizes them appropriately
		this.add(nameL);
		this.add(maxHoursL);
		this.add(minHoursL);
		this.add(breakL);
		this.add(jumpL);

		nameT.setPreferredSize(new Dimension(200, 20));
		this.add(nameT);
		maxHoursT.setPreferredSize(new Dimension(200, 20));
		this.add(maxHoursT);
		minHoursT.setPreferredSize(new Dimension(200, 20));
		this.add(minHoursT);
		breakT.setPreferredSize(new Dimension(200, 20));
		this.add(breakT);
		jumpL.setPreferredSize(new Dimension(200, 20));
		this.add(jumpC);

		this.add(addUpdateB);
		this.add(cancelB);

		//Formats all the components on the JPanel correctly.
		layout.putConstraint(SpringLayout.WEST, nameL, 100, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, nameL, 150, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, nameT, 200, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, nameT, 150, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, maxHoursL, 100, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, maxHoursL, 30, SpringLayout.NORTH, nameL);

		layout.putConstraint(SpringLayout.WEST, maxHoursT, 200, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, maxHoursT, 30, SpringLayout.NORTH, nameT);

		layout.putConstraint(SpringLayout.WEST, minHoursL, 100, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, minHoursL, 30, SpringLayout.NORTH, maxHoursL);

		layout.putConstraint(SpringLayout.WEST, minHoursT, 200, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, minHoursT, 30, SpringLayout.NORTH, maxHoursT);
		
		layout.putConstraint(SpringLayout.WEST, breakL, 100, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, breakL, 30, SpringLayout.NORTH, minHoursL);
		
		layout.putConstraint(SpringLayout.WEST, breakT, 200, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, breakT, 30, SpringLayout.NORTH, minHoursT);
		
		layout.putConstraint(SpringLayout.WEST, jumpL, 100, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, jumpL, 30, SpringLayout.NORTH, breakL);
		
		layout.putConstraint(SpringLayout.WEST, jumpC, 200, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, jumpC, 30, SpringLayout.NORTH, breakT);

		layout.putConstraint(SpringLayout.WEST, addUpdateB, 100, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, addUpdateB, 75, SpringLayout.NORTH, breakL);

		layout.putConstraint(SpringLayout.WEST, cancelB, 20, SpringLayout.EAST, addUpdateB);
		layout.putConstraint(SpringLayout.NORTH, cancelB, 75, SpringLayout.NORTH, breakT);
	}
}
