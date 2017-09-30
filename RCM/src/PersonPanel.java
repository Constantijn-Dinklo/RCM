import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class PersonPanel extends JPanel {
	
	Model model;
	SpringLayout layout;
	
	JLabel nameL, studentPrefL, teacherPrefL, notHorseL;
	JTextField nameT;
	List<CustomComboBox<Horse>> horsePrefs, teacherPrefs;
	CustomComboBox<Horse> notHorseT;
	JButton addUpdateB, cancelB;
	
	//Whether we are adding a person or updating a person (change into 2 subclasses)
	String varient;
	
	Person curPerson;
	
	public PersonPanel(Model model, Person person, String varient){
		
		this.model = model;
		MainFrame frame = model.frame;
		layout = new SpringLayout();
		this.setLayout(layout);
		
		curPerson = person;
		
		this.varient = varient;

		nameL = new JLabel("Name:");
		studentPrefL = new JLabel("Student Prefs:");
		teacherPrefL = new JLabel("Teacher Pref:");
		notHorseL = new JLabel("Horse not Pref:");

		nameT = new JTextField("Name");
		
		horsePrefs = new ArrayList<CustomComboBox<Horse> >();
		for (int i = 0; i < 3; i++)
		{
			horsePrefs.add(new CustomComboBox<Horse>());
		}
		teacherPrefs = new ArrayList<CustomComboBox<Horse> >();
		for (int i = 0; i < 3; i++)
		{
			teacherPrefs.add(new CustomComboBox<Horse>());
		}
		notHorseT = new CustomComboBox<Horse> ();

		addUpdateB = new JButton(varient);
		cancelB = new JButton("Cancel");

		addActionListener();
		addValuesToComboBoxes();

		if (person != null){
			setUIValues();
		}
		
		buildUI();

		frame.getContentPane().add(this, BorderLayout.CENTER);
		
		frame.currentPanel = this;
	}
	
	private void addActionListener(){
		//Action listener for add button
		//Performs the logic of adding a new horse.
		//Has separate actionListener because it needs local variables.
		addUpdateB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = nameT.getText().trim();
				if (model.getPerson(name) != null && !varient.equals("Update"))
				{
					System.out.println("Person with this name already exists");
					JOptionPane.showMessageDialog(null, "Person with this name already exists", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				ArrayList<Horse> totalHorsePool = new ArrayList<Horse>();
				ArrayList<Horse> horsePref = new ArrayList<Horse>();
				ArrayList<Horse> teacherPref = new ArrayList<Horse>();
				Horse notHorse = null;

				//Getting all the horses
				for (int i = 0; i < horsePrefs.size(); i++)
				{
					Object h = horsePrefs.get(i).getSelectedItem();
					if (h != null && !totalHorsePool.contains(h))
					{
						horsePref.add((Horse) h);
						totalHorsePool.add((Horse) h);
					}
				}
				for (int i = 0; i < teacherPrefs.size(); i++)
				{
					Object h = teacherPrefs.get(i).getSelectedItem();
					if (h != null && !totalHorsePool.contains(h))
					{
						teacherPref.add((Horse) h);
						totalHorsePool.add((Horse) h);
					}
				}
				Object h = notHorseT.getSelectedItem();
				if (h != null && !totalHorsePool.contains(h))
				{
					notHorse = (Horse) h;
					totalHorsePool.add((Horse) h);
				}
				
				//Person person = person;
				Person newPerson = curPerson;
				if (curPerson == null)
				{
					newPerson = new Person(name, horsePref, teacherPref, notHorse);
					model.addPerson(newPerson, horsePref, teacherPref);
				}
				//Updating the person
				else
				{
					model.updatePerson(curPerson, horsePref, teacherPref, notHorse);
				}
				model.toMainMenu();
			}
		});
		cancelB.addActionListener(model);
	}
	
	//Sets the values you can choose from the combobox, also adds the comboboxes to the screen.
	private void addValuesToComboBoxes(){
		ArrayList<Horse> horseList = model.getHorseList();
		//Adding items to the JComboBox's, adds all students.
		for (int boxNum = 0; boxNum < horsePrefs.size(); boxNum++)
		{
			horsePrefs.get(boxNum).addItem(null);
			for (int i = 0; i < horseList.size(); i++)
			{
				horsePrefs.get(boxNum).addItem(horseList.get(i));
			}
			horsePrefs.get(boxNum).setPreferredSize(new Dimension(200, 20));
			this.add(horsePrefs.get(boxNum));
		}
		for (int boxNum = 0; boxNum < teacherPrefs.size(); boxNum++)
		{
			teacherPrefs.get(boxNum).addItem(null);
			for (int i = 0; i < horseList.size(); i++)
			{
				teacherPrefs.get(boxNum).addItem(horseList.get(i));
			}
			teacherPrefs.get(boxNum).setPreferredSize(new Dimension(200, 20));
			this.add(teacherPrefs.get(boxNum));
		}
		notHorseT.addItem(null);
		for (int i = 0; i < horseList.size(); i++)
		{
			notHorseT.addItem(horseList.get(i));
		}
		notHorseT.setPreferredSize(new Dimension(200, 20));
		this.add(notHorseT);
	}
	
	private void setUIValues(){
		System.out.println("In set UI values");
		if (curPerson != null)
		{
			nameT.setText(curPerson.getName());
			nameT.setEditable(false);
			for (int i = 0; i < curPerson.getStudentPref().size(); i++)
			{
				horsePrefs.get(i).setSelectedItem(curPerson.getStudentPref().get(i));
				//System.out.println(curPerson.getStudentPrefgetComponentCount().get(i).);
				//System.out.println(horsePrefs.get(i).());
			}
			for (int i = 0; i < curPerson.getTeacherPref().size(); i++)
			{
				teacherPrefs.get(i).setSelectedItem(curPerson.getTeacherPref().get(i));
			}
			notHorseT.setSelectedItem(curPerson.getNotHorse());
		}
	}

	private void buildUI(){
		//Adds all components to the JPanel and sizes them appropriately.
		this.add(nameL);
		this.add(studentPrefL);
		this.add(teacherPrefL);
		this.add(notHorseL);

		nameT.setPreferredSize(new Dimension(200, 20));
		this.add(nameT);

		this.add(addUpdateB);
		this.add(cancelB);

		//Formats the layout of the JPanel that will be added and displaced on the JFrame
		layout.putConstraint(SpringLayout.WEST, nameL, 100, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, nameL, 150, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, nameT, 20, SpringLayout.EAST, notHorseL);
		layout.putConstraint(SpringLayout.NORTH, nameT, 150, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, studentPrefL, 100, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, studentPrefL, 30, SpringLayout.NORTH, nameL);
		
		for(int i = 0; i < horsePrefs.size(); i++)
		{
			layout.putConstraint(SpringLayout.WEST, horsePrefs.get(i), 20, SpringLayout.EAST, notHorseL);
			layout.putConstraint(SpringLayout.NORTH, horsePrefs.get(i), ((i + 1) * 30), SpringLayout.NORTH, nameT);
		}
		
		layout.putConstraint(SpringLayout.WEST, teacherPrefL, 100, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, teacherPrefL, (30 * (horsePrefs.size() + 1)), SpringLayout.NORTH, nameL);
		
		for (int i = 0; i < teacherPrefs.size(); i++)
		{
			layout.putConstraint(SpringLayout.WEST, teacherPrefs.get(i), 20, SpringLayout.EAST, notHorseL);
			layout.putConstraint(SpringLayout.NORTH, teacherPrefs.get(i), ((i + horsePrefs.size() + 1) * 30), SpringLayout.NORTH, nameT);
		}

		layout.putConstraint(SpringLayout.WEST, notHorseL, 100, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, notHorseL, (30 * (horsePrefs.size() + teacherPrefs.size() + 1)), SpringLayout.NORTH, nameL);

		layout.putConstraint(SpringLayout.WEST, notHorseT, 20, SpringLayout.EAST, notHorseL);
		layout.putConstraint(SpringLayout.NORTH, notHorseT, (30 * (horsePrefs.size() + teacherPrefs.size() + 1)), SpringLayout.NORTH, nameT);

		layout.putConstraint(SpringLayout.WEST, addUpdateB, 100, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, addUpdateB, 75, SpringLayout.NORTH, notHorseT);

		layout.putConstraint(SpringLayout.WEST, cancelB, 20, SpringLayout.EAST, addUpdateB);
		layout.putConstraint(SpringLayout.NORTH, cancelB, 75, SpringLayout.NORTH, notHorseT);
	}
}
