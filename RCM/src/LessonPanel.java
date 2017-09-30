import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilCalendarModel;

public class LessonPanel extends JPanel {
	
	final int NUMPEOPLE = 10;
	
	JLayeredPane thisPanel;
	
	Model model;
	MainFrame frame;
	SpringLayout layout;
	
	JLabel studentLabel, horseLabel, dateLabel, startTimeLabel, endTimeLabel, timeFormat1, timeFormat2, lessonNumLabel, typeLabel;
	JTextField startTimeField, endTimeField, lessonNumField;
	JDatePanelImpl datePanel;
	JDatePickerImpl datePicker;
	
	ButtonGroup typeGroup;
	JRadioButton normalR, jumpR;
	
	ArrayList<CustomComboBox<Person>> personComboBoxes;
	ArrayList<JComboBox<Horse>> horseComboBoxes;
	ArrayList<JTextField> horseFields;
	
	ArrayList<JTextField> peopleFields;
	
	JButton varientB, cancelB;
	
	String varient;
	Lesson curLesson;
	LessonType curLessonType;
	
	JList<Person> peopleList;
	JScrollPane peopleListPane;
	JList<Horse> horseList;
	
	//AutoCompleteDecorator decorator;
		
	public LessonPanel (Model model, Lesson lesson, String varient){

		thisPanel = new JLayeredPane();
		this.model = model;
		this.curLesson = lesson;
		this.varient = varient;
		
		this.frame = model.frame;

		layout = new SpringLayout();
		this.setLayout(layout);

		studentLabel = new JLabel("Students: ");
		horseLabel = new JLabel("Horses: ");
		dateLabel = new JLabel("Date: ");
		typeLabel = new JLabel("Type: ");
		startTimeLabel = new JLabel("Start Time: ");
		endTimeLabel = new JLabel("End Time: ");
		timeFormat1 = new JLabel ("hh:mm (24 hour clock)");
		timeFormat2 = new JLabel ("hh:mm (24 hour clock)");
		lessonNumLabel = new JLabel ("Lesson Number: ");

		startTimeField = new JTextField();
		endTimeField = new JTextField();
		lessonNumField = new JTextField();
		
		typeGroup = new ButtonGroup();
		normalR = new JRadioButton("Normal");
		jumpR = new JRadioButton("Jump");
		
		typeGroup.add(normalR);
		typeGroup.add(jumpR);
		
		normalR.setSelected(true);

		//Creates the Date picker
		UtilCalendarModel calModel = new UtilCalendarModel();
		datePanel = new JDatePanelImpl(calModel);
		datePicker = new JDatePickerImpl(datePanel);
		
		Calendar tmpCal = Calendar.getInstance();
		
		datePicker.getModel().setDate(tmpCal.get(Calendar.YEAR), tmpCal.get(Calendar.MONTH), tmpCal.get(Calendar.DAY_OF_MONTH));
		datePicker.getModel().setSelected(true);
		
		peopleList = new JList(model.getPersonList().toArray());
		peopleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		peopleList.setLayoutOrientation(JList.VERTICAL);
		peopleList.setVisibleRowCount(-1);
		
		peopleListPane = new JScrollPane(peopleList);
		

		//Creating all the ComboBox's
		personComboBoxes = new ArrayList<CustomComboBox<Person> >();
		horseComboBoxes = new ArrayList<JComboBox<Horse>>();
		peopleFields = new ArrayList<JTextField>();
		horseFields = new ArrayList<JTextField>();
		for (int boxNum = 0; boxNum < NUMPEOPLE; boxNum++)
		{
			CustomComboBox<Person> personBox = new CustomComboBox<Person>();
			personBox.addItem(null);
			for (int i = 0; i < model.getPersonList().size(); i++)
			{
				personBox.addItem(model.getPersonList().get(i));
			}
			personComboBoxes.add(personBox);
			//AutoCompleteDecorator.decorate(personBox);
			
			
			JComboBox<Horse> horseBox = new JComboBox<Horse>();
			horseBox.addItem(null);
			for (int i = 0; i < model.getHorseList().size(); i++)
			{
				horseBox.addItem(model.getHorseList().get(i));
			}
			horseComboBoxes.add(horseBox);
			

			JTextField person = new JTextField();
			peopleFields.add(person);
			
			
			JTextField horse = new JTextField();
			horse.setEditable(false);
			horseFields.add(horse);
		}
		
		
		
		if (lesson != null)
		{
			lesson.display();
			Calendar startDay = lesson.getStartDate();
			startTimeField.setText(startDay.get(Calendar.HOUR_OF_DAY) + ":" + startDay.get(Calendar.MINUTE));
			
			Calendar endDay = lesson.getEndDate();
			endTimeField.setText(endDay.get(Calendar.HOUR_OF_DAY) + ":" + endDay.get(Calendar.MINUTE));
			
			lessonNumField.setText("" + lesson.getLessonNum());
			
			datePicker.getModel().setDate(startDay.get(Calendar.YEAR), startDay.get(Calendar.MONTH), startDay.get(Calendar.DAY_OF_MONTH));
			datePicker.getModel().setSelected(true);
			
			ArrayList<Pair> pairs = lesson.getPairs();
			
			for (int i = 0; i < pairs.size(); i++)
			{
				Person p = pairs.get(i).getPerson();
				personComboBoxes.get(i).setSelectedItem(p);
				Horse h = pairs.get(i).getHorse();
				horseFields.get(i).setText(h.getName());
			}
			
			curLessonType = lesson.getType();
			if (curLessonType.equals(LessonType.Normal))
			{
				normalR.setSelected(true);
			}
			else if (curLessonType.equals(LessonType.Jump))
			{
				jumpR.setSelected(true);
			}
		}
		
		varientB = new JButton(varient);
		cancelB = new JButton("Cancel");
		
		addFieldListeners();
		addActionListener();
		buildUI();

		frame.setSize(new Dimension(700,800));
		
		frame.getContentPane().add(this, BorderLayout.CENTER);
		
		frame.currentPanel = this;
	}
	
	private void addFieldListeners()
	{
		for(int i = 0; i < NUMPEOPLE; i++)
		{
			peopleFields.get(i).addKeyListener(new KeyListener(){

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					//System.out.println("Pressed");
				}

				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub
					//System.out.println("Released");
				}

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub
					System.out.println("Typed");
					System.out.println(e.getSource());
					
					JTextField curTextField = (JTextField) e.getSource();
					
					peopleListPane.setPreferredSize(new Dimension(80,100));
					thisPanel.add(peopleListPane);
					
					layout.putConstraint(SpringLayout.WEST,	peopleListPane, 0, SpringLayout.WEST, curTextField);
					layout.putConstraint(SpringLayout.NORTH, peopleListPane, 0, SpringLayout.SOUTH, curTextField);
					
					thisPanel.repaint();
					thisPanel.revalidate();
					
				}
				
			});
		}
	}

	private void addActionListener(){
		varientB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (varient == "Calendar")
				{
					frame.currentPanel.setVisible(false);
					frame.remove(frame.currentPanel);
					CalendarModel calModel = new CalendarModel(model);
					CalendarPanel slp = new CalendarPanel(calModel);
					slp.setVisible(true);
					return;
				}
				
				ArrayList<Person> studentsOfLesson = new ArrayList<Person>();
				
				System.out.println("Getting people");

				int numPeopleInLesson = 0;
				//Add all students selected to be in this lesson
				for (int i = 0; i < personComboBoxes.size(); i++)
				{
					Object p = personComboBoxes.get(i).getSelectedItem();
					if (p != null)
					{
						//Prevent the same person from being added twice
						if (!(studentsOfLesson.contains((Person)p)))
							studentsOfLesson.add((Person)p);
						numPeopleInLesson++;
						
					}
				}
				
				if (numPeopleInLesson == 0)
				{
					System.out.println("Need at least 1 person in the lesson");
					JOptionPane.showMessageDialog(null, "Need at least 1 student in the lesson", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				System.out.println("Getting date");

				Calendar selectedDay = (Calendar) datePicker.getModel().getValue();
				if (selectedDay == null)
				{
					System.out.println("Need a date for the lesson");
					JOptionPane.showMessageDialog(null, "Need a date for the lesson", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				

				String startSelectedTime = startTimeField.getText().replace(" ", "");
				String endSelectedTime = endTimeField.getText().replace(" ", "");
				String lessonNumSt = lessonNumField.getText().replace(" ", "");
				
				int startHour = 0;
				int startMinutes = 0;
				try
				{
					String [] timeParts = startSelectedTime.split(":");
					startHour = Integer.parseInt(timeParts[0]);
					startMinutes = Integer.parseInt(timeParts[1]);
					
					if ((startHour > 24) || (startHour < 0) || (startMinutes > 60) || (startMinutes < 0))
						throw new Exception();
				}
				catch(Exception exc)
				{
					JOptionPane.showMessageDialog(null, "Need a correct time format for Start Time", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				int endHour = 0;
				int endMinutes = 0;
				try
				{
					String [] timeParts = endSelectedTime.split(":");
					endHour = Integer.parseInt(timeParts[0]);
					endMinutes = Integer.parseInt(timeParts[1]);
					
					if ((endHour > 24) || (endHour < 0) || (endMinutes > 60) || (endMinutes < 0))
						throw new Exception();
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null, "Need a correct time format for End Time", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}				
				
				
				int lessonNum = 1;
				try
				{
					lessonNum = Integer.parseInt(lessonNumSt);
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null, "Need a number for Lesson Number", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
				System.out.println("Create Start date");

				Calendar startDay = Calendar.getInstance();
				startDay.setTimeZone(TimeZone.getTimeZone("Europe/Amsterdam"));

				startDay.set(selectedDay.get(Calendar.YEAR), 
						selectedDay.get(Calendar.MONTH), 
						selectedDay.get(Calendar.DAY_OF_MONTH),
						startHour,
						startMinutes);
				
				System.out.println(startDay.get(Calendar.YEAR));
				System.out.println("Create End date");

				Calendar endDay = Calendar.getInstance();
				endDay.setTimeZone(TimeZone.getTimeZone("Europe/Amsterdam"));
				endDay.set(selectedDay.get(Calendar.YEAR), 
						selectedDay.get(Calendar.MONTH), 
						selectedDay.get(Calendar.DAY_OF_MONTH),
						endHour,
						endMinutes);
								
				int lessonTime = (int) (endDay.getTimeInMillis() - startDay.getTimeInMillis()) / 1000;
				
				if (curLesson != null)
				{
					//We remove the lesson from the list, since if we didn't update the time or lesson number, then we don't want this lesson to already exist
					//We also remove it, because updating a lesson will always reassign pairs. (this might be changed depending on consumer request)
					model.removeLesson(curLesson);
				}
				
				if (!model.lessonExists(startDay, endDay, lessonNum))
				{
					Lesson newLesson = new Lesson(studentsOfLesson, model.getAvailableHorses(lessonTime, startDay, curLessonType), startDay, endDay, lessonTime, lessonNum, curLessonType);
					try
					{
						newLesson.pair();
					}
					catch(NotEnoughHorsesException neh)
					{
						JOptionPane.showMessageDialog(null, "Not enough horses that fit the specifications for this lesson, need " + neh.numHorsesNeeded + " more horse(s)", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					model.addLesson(newLesson);
					model.toMainMenu();
				}
				else
				{
					System.out.println("Lesson already Exists");
					if (curLesson != null)
					{
						//We add the lesson back in since we didn't update it.
						model.addLesson(curLesson);
					}
					JOptionPane.showMessageDialog(null, "Lesson with this time and lesson number already exists", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		cancelB.addActionListener(model);
		
		normalR.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(e);
				curLessonType = LessonType.Normal;
			}		
		});
		jumpR.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(e);
				curLessonType = LessonType.Jump;
			}		
		});
	}
	
	private void buildUI(){
		final int VIRTICALGAP = 40;
		final int HORIZONTAL_GAP = 10;
		
		studentLabel.setPreferredSize(new Dimension(130, 23));
		this.add(studentLabel);
		horseLabel.setPreferredSize(new Dimension(130, 23));
		this.add(horseLabel);
		
		for (int i = 0; i < NUMPEOPLE; i++)
		{
			personComboBoxes.get(i).setPreferredSize(new Dimension(130, 23));
			this.add(personComboBoxes.get(i));
			//peopleFields.get(i).setPreferredSize(new Dimension(80, 23));
			//this.add(peopleFields.get(i));
			
			horseFields.get(i).setPreferredSize(new Dimension(130, 23));
			this.add(horseFields.get(i));
		}
		
		this.add(dateLabel);
		this.add(datePicker);
		
		this.add(typeLabel);
		this.add(normalR);
		this.add(jumpR);

		this.add(startTimeLabel);
		startTimeField.setPreferredSize(new Dimension(130, 23));
		this.add(startTimeField);
		this.add(timeFormat1);

		this.add(endTimeLabel);
		endTimeField.setPreferredSize(new Dimension(130, 23));
		this.add(endTimeField);
		this.add(timeFormat2);
		
		this.add(lessonNumLabel);
		lessonNumField.setPreferredSize(new Dimension(130, 23));
		this.add(lessonNumField);

		this.add(varientB);
		this.add(cancelB);

		layout.putConstraint(SpringLayout.WEST, studentLabel, HORIZONTAL_GAP, SpringLayout.EAST, lessonNumLabel);
		layout.putConstraint(SpringLayout.NORTH, studentLabel, VIRTICALGAP, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST,	horseLabel, 30, SpringLayout.EAST, studentLabel);
		layout.putConstraint(SpringLayout.NORTH, horseLabel, VIRTICALGAP, SpringLayout.NORTH, this);

		for(int i = 0; i < NUMPEOPLE; i++)
		{
			layout.putConstraint(SpringLayout.WEST, personComboBoxes.get(i), 0, SpringLayout.WEST, studentLabel);
			layout.putConstraint(SpringLayout.NORTH, personComboBoxes.get(i), (20 + (i * 30)), SpringLayout.NORTH, studentLabel);
		
			//layout.putConstraint(SpringLayout.WEST, peopleFields.get(i), 0, SpringLayout.WEST, studentLabel);
			//layout.putConstraint(SpringLayout.NORTH, peopleFields.get(i), (20 + (i * 30)), SpringLayout.NORTH, studentLabel);
			
			layout.putConstraint(SpringLayout.WEST, horseFields.get(i), 0, SpringLayout.WEST, horseLabel);
			layout.putConstraint(SpringLayout.NORTH, horseFields.get(i), (20 + (i * 30)), SpringLayout.NORTH, horseLabel);
		}
		
		layout.putConstraint(SpringLayout.WEST, dateLabel, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, dateLabel, VIRTICALGAP, SpringLayout.NORTH, personComboBoxes.get(NUMPEOPLE - 1));
		
		layout.putConstraint(SpringLayout.WEST, datePicker, HORIZONTAL_GAP, SpringLayout.EAST, lessonNumLabel);
		layout.putConstraint(SpringLayout.NORTH, datePicker, VIRTICALGAP, SpringLayout.NORTH, personComboBoxes.get(NUMPEOPLE - 1));
		
		layout.putConstraint(SpringLayout.WEST, typeLabel, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, typeLabel, VIRTICALGAP, SpringLayout.NORTH, datePicker);
		
		layout.putConstraint(SpringLayout.WEST, normalR, HORIZONTAL_GAP, SpringLayout.EAST, lessonNumLabel);
		layout.putConstraint(SpringLayout.NORTH, normalR, VIRTICALGAP, SpringLayout.NORTH, datePicker);
		
		layout.putConstraint(SpringLayout.WEST, jumpR, 100, SpringLayout.WEST,  normalR);
		layout.putConstraint(SpringLayout.NORTH, jumpR, VIRTICALGAP, SpringLayout.NORTH, datePicker);
		
		layout.putConstraint(SpringLayout.WEST, startTimeLabel, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, startTimeLabel, VIRTICALGAP, SpringLayout.NORTH, typeLabel);

		layout.putConstraint(SpringLayout.WEST, startTimeField, HORIZONTAL_GAP, SpringLayout.EAST, lessonNumLabel);
		layout.putConstraint(SpringLayout.NORTH, startTimeField, VIRTICALGAP, SpringLayout.NORTH, typeLabel);

		layout.putConstraint(SpringLayout.WEST, timeFormat1, 150, SpringLayout.WEST, startTimeField);
		layout.putConstraint(SpringLayout.NORTH, timeFormat1, VIRTICALGAP, SpringLayout.NORTH, typeLabel);

		layout.putConstraint(SpringLayout.WEST, endTimeLabel, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, endTimeLabel, VIRTICALGAP, SpringLayout.NORTH, startTimeLabel);

		layout.putConstraint(SpringLayout.WEST, endTimeField, HORIZONTAL_GAP, SpringLayout.EAST, lessonNumLabel);
		layout.putConstraint(SpringLayout.NORTH, endTimeField, VIRTICALGAP, SpringLayout.NORTH, startTimeField);

		layout.putConstraint(SpringLayout.WEST, timeFormat2, 150, SpringLayout.WEST, endTimeField);
		layout.putConstraint(SpringLayout.NORTH, timeFormat2, VIRTICALGAP, SpringLayout.NORTH, timeFormat1);
		
		layout.putConstraint(SpringLayout.WEST, lessonNumLabel, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, lessonNumLabel, VIRTICALGAP, SpringLayout.NORTH, endTimeLabel);
		
		layout.putConstraint(SpringLayout.WEST, lessonNumField, HORIZONTAL_GAP, SpringLayout.EAST, lessonNumLabel);
		layout.putConstraint(SpringLayout.NORTH, lessonNumField, VIRTICALGAP, SpringLayout.NORTH, endTimeLabel);

		layout.putConstraint(SpringLayout.WEST, varientB, 100, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, varientB, VIRTICALGAP, SpringLayout.NORTH, lessonNumLabel);

		layout.putConstraint(SpringLayout.WEST, cancelB, 20, SpringLayout.EAST, varientB);
		layout.putConstraint(SpringLayout.NORTH, cancelB, VIRTICALGAP, SpringLayout.NORTH, lessonNumLabel);
	}
}
