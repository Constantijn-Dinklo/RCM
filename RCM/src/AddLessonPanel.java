import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;

public class AddLessonPanel extends LessonPanel {

	JCheckBox weeklyCB;
	
	public AddLessonPanel(Model model) {
		super(model, null, "Add");
		
		weeklyCB = new JCheckBox("Repeat this lesson weekly");
				
		addActionListener();
		buildUI();
		
		this.setPreferredSize(new Dimension(700, 800));
	}
	
	private void addActionListener(){
		varientB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				System.out.println("In Add Lesson");
				ArrayList<Person> studentsOfLesson = new ArrayList<Person>();
				
				int numPeopleInLesson = 0;
				//Add all students selected to be in this lesson
				for (int i = 0; i < personComboBoxes.size(); i++)
				{
					Object p = personComboBoxes.get(i).getSelectedItem();
					if (p != null)
					{
						//Prevent the same person from being added twice
						if (!(studentsOfLesson.contains((Person)p)))
						{
							studentsOfLesson.add((Person)p);
							numPeopleInLesson++;
						}
					}
				}
				
				if (numPeopleInLesson == 0)
				{
					System.out.println("Need at least 1 person in the lesson");
					JOptionPane.showMessageDialog(null, "Need at least 1 student in the lesson", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				Calendar selectedStartDay = (Calendar) startDatePicker.getModel().getValue();
				if (selectedStartDay == null)
				{
					System.out.println("Need a date for the lesson");
					JOptionPane.showMessageDialog(null, "Need a date for the lesson", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				Calendar selectedEndDay = (Calendar) endDatePicker.getModel().getValue();
				if (weeklyCB.isSelected() && (selectedEndDay == null))
				{
					System.out.println("Need a date for the end date of the lesson");
					JOptionPane.showMessageDialog(null, "Need a date for the end date of the lesson", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (!weeklyCB.isSelected())
				{
					selectedEndDay = selectedStartDay;
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
				
				
				
				Calendar checkDate = selectedStartDay;
				ArrayList<Lesson> newLessons = new ArrayList<Lesson>();
				while(checkDate.compareTo(selectedEndDay) <= 0)
				{
					System.out.println(checkDate);
					System.out.println(selectedEndDay);
					System.out.println("Create Start date");
	
					Calendar startTime = Calendar.getInstance();
					startTime.setTimeZone(TimeZone.getTimeZone("Europe/Amsterdam"));
	
					startTime.set(checkDate.get(Calendar.YEAR), 
							checkDate.get(Calendar.MONTH), 
							checkDate.get(Calendar.DAY_OF_MONTH),
							startHour,
							startMinutes);
					
					System.out.println("Create End date");
	
					Calendar endTime = Calendar.getInstance();
					endTime.setTimeZone(TimeZone.getTimeZone("Europe/Amsterdam"));
					endTime.set(checkDate.get(Calendar.YEAR), 
							checkDate.get(Calendar.MONTH), 
							checkDate.get(Calendar.DAY_OF_MONTH),
							endHour,
							endMinutes);
									
					int lessonTime = (int) (endTime.getTimeInMillis() - startTime.getTimeInMillis()) / 1000;
					CustomDate tmpDate = new CustomDate(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime.get(Calendar.DAY_OF_MONTH));

					if (!model.lessonExists(startTime, endTime, lessonNum))
					{
						Lesson newLesson = new Lesson(studentsOfLesson, model.getAvailableHorses(lessonTime, tmpDate, curLessonType), startTime, endTime, lessonTime, lessonNum, curLessonType);
						try
						{
							newLesson.pair();
						}
						catch(NotEnoughHorsesException neh)
						{
							JOptionPane.showMessageDialog(null, "Not enough horses that fit the specifications for this lesson, need " + neh.numHorsesNeeded + " more horse(s) for date " + tmpDate.toString(), "Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
						newLessons.add(newLesson);
					}
					else
					{
						System.out.println("Lesson already Exists");
						JOptionPane.showMessageDialog(null, "Lesson with this time and lesson number already exists on date " + tmpDate.toString(), "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					checkDate.add(Calendar.DATE, 7);
					
					if (!weeklyCB.isSelected())
					{
						break;
					}
				}
				
				for (int i = 0; i < newLessons.size(); i++)
				{
					model.addLesson(newLessons.get(i));
				}
				
				getFrame().close();
			}
		});
		
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
		
		weeklyCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Clicked");
				System.out.println(e.getSource());
				
				JCheckBox tmpCB = (JCheckBox) e.getSource();

				endDateLabel.setVisible(tmpCB.isSelected());
				endDatePicker.setVisible(tmpCB.isSelected());
			}
		});
		
	}
	
	
	private void buildUI(){
		final int VIRTICAL_GAP = 40;
		final int HORIZONTAL_GAP = 10;
		
		studentLabel.setPreferredSize(new Dimension(130, 23));
		this.add(studentLabel);
		horseLabel.setPreferredSize(new Dimension(130, 23));
		this.add(horseLabel);
		
		for (int i = 0; i < NUMPEOPLE; i++)
		{
			personComboBoxes.get(i).setPreferredSize(new Dimension(130, 23));
			this.add(personComboBoxes.get(i));
			
			horseFields.get(i).setPreferredSize(new Dimension(130, 23));
			this.add(horseFields.get(i));
		}
		
		this.add(startDateLabel);
		this.add(startDatePicker);
		
		this.add(endDateLabel);
		this.add(endDatePicker);
		
		this.add(weeklyCB);
		
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
		layout.putConstraint(SpringLayout.NORTH, studentLabel, VIRTICAL_GAP, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST,	horseLabel, 30, SpringLayout.EAST, studentLabel);
		layout.putConstraint(SpringLayout.NORTH, horseLabel, VIRTICAL_GAP, SpringLayout.NORTH, this);

		for(int i = 0; i < NUMPEOPLE; i++)
		{
			layout.putConstraint(SpringLayout.WEST, personComboBoxes.get(i), 0, SpringLayout.WEST, studentLabel);
			layout.putConstraint(SpringLayout.NORTH, personComboBoxes.get(i), (30 + (i * 30)), SpringLayout.NORTH, studentLabel);
			
			layout.putConstraint(SpringLayout.WEST, horseFields.get(i), 0, SpringLayout.WEST, horseLabel);
			layout.putConstraint(SpringLayout.NORTH, horseFields.get(i), (30 + (i * 30)), SpringLayout.NORTH, horseLabel);
		}
		
		layout.putConstraint(SpringLayout.WEST, startDateLabel, HORIZONTAL_GAP, SpringLayout.EAST, lessonNumLabel);
		layout.putConstraint(SpringLayout.NORTH, startDateLabel, VIRTICAL_GAP, SpringLayout.NORTH, personComboBoxes.get(NUMPEOPLE - 1));
		
		layout.putConstraint(SpringLayout.WEST, startDatePicker, 0, SpringLayout.WEST, startDateLabel);
		layout.putConstraint(SpringLayout.NORTH, startDatePicker, VIRTICAL_GAP, SpringLayout.NORTH, startDateLabel);
		
		layout.putConstraint(SpringLayout.WEST, endDateLabel, HORIZONTAL_GAP, SpringLayout.EAST, startDatePicker);
		layout.putConstraint(SpringLayout.NORTH, endDateLabel, VIRTICAL_GAP, SpringLayout.NORTH, personComboBoxes.get(NUMPEOPLE - 1));
		
		layout.putConstraint(SpringLayout.WEST, endDatePicker, 0, SpringLayout.WEST, endDateLabel);
		layout.putConstraint(SpringLayout.NORTH, endDatePicker, VIRTICAL_GAP, SpringLayout.NORTH, startDateLabel);
		
		layout.putConstraint(SpringLayout.WEST, weeklyCB, 0, SpringLayout.WEST, startDateLabel);
		layout.putConstraint(SpringLayout.NORTH, weeklyCB, VIRTICAL_GAP, SpringLayout.NORTH, startDatePicker);
		
		layout.putConstraint(SpringLayout.WEST, typeLabel, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, typeLabel, VIRTICAL_GAP, SpringLayout.NORTH, weeklyCB);
		
		layout.putConstraint(SpringLayout.WEST, normalR, HORIZONTAL_GAP, SpringLayout.EAST, lessonNumLabel);
		layout.putConstraint(SpringLayout.NORTH, normalR, VIRTICAL_GAP, SpringLayout.NORTH, weeklyCB);
		
		layout.putConstraint(SpringLayout.WEST, jumpR, 100, SpringLayout.WEST,  normalR);
		layout.putConstraint(SpringLayout.NORTH, jumpR, VIRTICAL_GAP, SpringLayout.NORTH, weeklyCB);
		
		layout.putConstraint(SpringLayout.WEST, startTimeLabel, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, startTimeLabel, VIRTICAL_GAP, SpringLayout.NORTH, typeLabel);

		layout.putConstraint(SpringLayout.WEST, startTimeField, HORIZONTAL_GAP, SpringLayout.EAST, lessonNumLabel);
		layout.putConstraint(SpringLayout.NORTH, startTimeField, VIRTICAL_GAP, SpringLayout.NORTH, typeLabel);

		layout.putConstraint(SpringLayout.WEST, timeFormat1, 150, SpringLayout.WEST, startTimeField);
		layout.putConstraint(SpringLayout.NORTH, timeFormat1, VIRTICAL_GAP, SpringLayout.NORTH, typeLabel);

		layout.putConstraint(SpringLayout.WEST, endTimeLabel, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, endTimeLabel, VIRTICAL_GAP, SpringLayout.NORTH, startTimeLabel);

		layout.putConstraint(SpringLayout.WEST, endTimeField, HORIZONTAL_GAP, SpringLayout.EAST, lessonNumLabel);
		layout.putConstraint(SpringLayout.NORTH, endTimeField, VIRTICAL_GAP, SpringLayout.NORTH, startTimeField);

		layout.putConstraint(SpringLayout.WEST, timeFormat2, 150, SpringLayout.WEST, endTimeField);
		layout.putConstraint(SpringLayout.NORTH, timeFormat2, VIRTICAL_GAP, SpringLayout.NORTH, timeFormat1);
		
		layout.putConstraint(SpringLayout.WEST, lessonNumLabel, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, lessonNumLabel, VIRTICAL_GAP, SpringLayout.NORTH, endTimeLabel);
		
		layout.putConstraint(SpringLayout.WEST, lessonNumField, HORIZONTAL_GAP, SpringLayout.EAST, lessonNumLabel);
		layout.putConstraint(SpringLayout.NORTH, lessonNumField, VIRTICAL_GAP, SpringLayout.NORTH, endTimeLabel);

		layout.putConstraint(SpringLayout.WEST, varientB, 250, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, varientB, VIRTICAL_GAP, SpringLayout.NORTH, lessonNumLabel);

		layout.putConstraint(SpringLayout.WEST, cancelB, 20, SpringLayout.EAST, varientB);
		layout.putConstraint(SpringLayout.NORTH, cancelB, VIRTICAL_GAP, SpringLayout.NORTH, lessonNumLabel);
	}

}
