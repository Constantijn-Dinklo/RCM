import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilCalendarModel;

public class LessonPanel extends JPanel {
	
	final int NUMPEOPLE = 10;
	
	Model model;
	MainFrame frame;
	SpringLayout layout;
	
	JLabel studentLabel, horseLabel, dateLabel, startTimeLabel, endTimeLabel, timeFormat1, timeFormat2, lessonNumLabel, typeLabel;
	JTextField startTimeField, endTimeField, lessonNumField;
	JDatePanelImpl datePanel;
	JDatePickerImpl datePicker;
	
	ButtonGroup typeGroup;
	JRadioButton normalR, jumpR;
	
	ArrayList<JComboBox<Person>> personComboBoxes;
	ArrayList<JComboBox<Horse>> horseComboBoxes;
	ArrayList<JTextField> horseFields;
	
	JButton varientB, cancelB;
	
	String varient;
	Lesson curLesson;
	LessonType curLessonType;
	
	public LessonPanel (Model model, Lesson lesson, String varient){

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

		//Creates the Date picker
		UtilCalendarModel calModel = new UtilCalendarModel();
		datePanel = new JDatePanelImpl(calModel);
		datePicker = new JDatePickerImpl(datePanel);

		//Creating all the ComboBox's
		personComboBoxes = new ArrayList<JComboBox<Person> >();
		horseComboBoxes = new ArrayList<JComboBox<Horse>>();
		horseFields = new ArrayList<JTextField>();
		for (int boxNum = 0; boxNum < NUMPEOPLE; boxNum++)
		{
			JComboBox<Person> personBox = new JComboBox<Person>();
			personBox.addItem(null);
			for (int i = 0; i < model.getPersonList().size(); i++)
			{
				personBox.addItem(model.getPersonList().get(i));
			}
			personComboBoxes.add(personBox);
			
			JComboBox<Horse> horseBox = new JComboBox<Horse>();
			horseBox.addItem(null);
			for (int i = 0; i < model.getHorseList().size(); i++)
			{
				horseBox.addItem(model.getHorseList().get(i));
			}
			horseComboBoxes.add(horseBox);
			

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
		
		addActionListener();
		buildUI();

		frame.getContentPane().add(this, BorderLayout.CENTER);
		
		frame.currentPanel = this;
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

				//Add all students selected to be in this lesson
				for (int i = 0; i < personComboBoxes.size(); i++)
				{
					Object p = personComboBoxes.get(i).getSelectedItem();
					if (p != null)
					{
						//Prevent the same person from being added twice
						if (!(studentsOfLesson.contains((Person)p)))
							studentsOfLesson.add((Person)p);
					}
				}
				
				System.out.println("Getting date");

				Calendar selectedDay = (Calendar) datePicker.getModel().getValue();

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
		
		this.add(studentLabel);
		this.add(horseLabel);
		
		for (int i = 0; i < NUMPEOPLE; i++)
		{
			this.add(personComboBoxes.get(i));
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

		layout.putConstraint(SpringLayout.WEST, studentLabel, 170, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, studentLabel, VIRTICALGAP, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST,	horseLabel, 100, SpringLayout.WEST, studentLabel);
		layout.putConstraint(SpringLayout.NORTH, horseLabel, VIRTICALGAP, SpringLayout.NORTH, this);

		for(int i = 0; i < NUMPEOPLE; i++)
		{
			layout.putConstraint(SpringLayout.WEST, personComboBoxes.get(i), 0, SpringLayout.WEST, studentLabel);
			layout.putConstraint(SpringLayout.NORTH, personComboBoxes.get(i), (20 + (i * 30)), SpringLayout.NORTH, studentLabel);
		
			layout.putConstraint(SpringLayout.WEST, horseFields.get(i), 0, SpringLayout.WEST, horseLabel);
			layout.putConstraint(SpringLayout.NORTH, horseFields.get(i), (20 + (i * 30)), SpringLayout.NORTH, horseLabel);
		}
		
		layout.putConstraint(SpringLayout.WEST, dateLabel, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, dateLabel, VIRTICALGAP, SpringLayout.NORTH, personComboBoxes.get(NUMPEOPLE - 1));
		
		layout.putConstraint(SpringLayout.WEST, datePicker, 100, SpringLayout.WEST, dateLabel);
		layout.putConstraint(SpringLayout.NORTH, datePicker, VIRTICALGAP, SpringLayout.NORTH, personComboBoxes.get(NUMPEOPLE - 1));
		
		layout.putConstraint(SpringLayout.WEST, typeLabel, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, typeLabel, VIRTICALGAP, SpringLayout.NORTH, datePicker);
		
		layout.putConstraint(SpringLayout.WEST, normalR, 100, SpringLayout.WEST, typeLabel);
		layout.putConstraint(SpringLayout.NORTH, normalR, VIRTICALGAP, SpringLayout.NORTH, datePicker);
		
		layout.putConstraint(SpringLayout.WEST, jumpR, 100, SpringLayout.WEST,  normalR);
		layout.putConstraint(SpringLayout.NORTH, jumpR, VIRTICALGAP, SpringLayout.NORTH, datePicker);
		
		layout.putConstraint(SpringLayout.WEST, startTimeLabel, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, startTimeLabel, VIRTICALGAP, SpringLayout.NORTH, typeLabel);

		layout.putConstraint(SpringLayout.WEST, startTimeField, 100, SpringLayout.WEST, startTimeLabel);
		layout.putConstraint(SpringLayout.NORTH, startTimeField, VIRTICALGAP, SpringLayout.NORTH, typeLabel);

		layout.putConstraint(SpringLayout.WEST, timeFormat1, 150, SpringLayout.WEST, startTimeField);
		layout.putConstraint(SpringLayout.NORTH, timeFormat1, VIRTICALGAP, SpringLayout.NORTH, typeLabel);

		layout.putConstraint(SpringLayout.WEST, endTimeLabel, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, endTimeLabel, VIRTICALGAP, SpringLayout.NORTH, startTimeLabel);

		layout.putConstraint(SpringLayout.WEST, endTimeField, 100, SpringLayout.WEST, endTimeLabel);
		layout.putConstraint(SpringLayout.NORTH, endTimeField, VIRTICALGAP, SpringLayout.NORTH, startTimeField);

		layout.putConstraint(SpringLayout.WEST, timeFormat2, 150, SpringLayout.WEST, endTimeField);
		layout.putConstraint(SpringLayout.NORTH, timeFormat2, VIRTICALGAP, SpringLayout.NORTH, timeFormat1);
		
		layout.putConstraint(SpringLayout.WEST, lessonNumLabel, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, lessonNumLabel, VIRTICALGAP, SpringLayout.NORTH, endTimeLabel);
		
		layout.putConstraint(SpringLayout.WEST, lessonNumField, 100, SpringLayout.WEST, lessonNumLabel);
		layout.putConstraint(SpringLayout.NORTH, lessonNumField, VIRTICALGAP, SpringLayout.NORTH, endTimeLabel);

		layout.putConstraint(SpringLayout.WEST, varientB, 100, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, varientB, VIRTICALGAP, SpringLayout.NORTH, lessonNumLabel);

		layout.putConstraint(SpringLayout.WEST, cancelB, 20, SpringLayout.EAST, varientB);
		layout.putConstraint(SpringLayout.NORTH, cancelB, VIRTICALGAP, SpringLayout.NORTH, lessonNumLabel);
	}
}
