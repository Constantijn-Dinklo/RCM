import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class UpdateLessonPanel extends LessonPanel {

	JTextField dateField, typeField;
	
	String [] monthStr = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	
	public UpdateLessonPanel(Model model, Lesson lesson, String varient) {
		super(model, lesson, varient);
		// TODO Auto-generated constructor stub
		
		lesson.display();
		Calendar startDay = lesson.getStartTime();
		startTimeField.setText(startDay.get(Calendar.HOUR_OF_DAY) + ":" + startDay.get(Calendar.MINUTE));
		startTimeField.setEditable(false);
		
		Calendar endDay = lesson.getEndTime();
		endTimeField.setText(endDay.get(Calendar.HOUR_OF_DAY) + ":" + endDay.get(Calendar.MINUTE));
		endTimeField.setEditable(false);
		
		lessonNumField.setText("" + lesson.getLessonNum());
		
		startDatePicker.getModel().setDate(startDay.get(Calendar.YEAR), startDay.get(Calendar.MONTH), startDay.get(Calendar.DAY_OF_MONTH));
		startDatePicker.getModel().setSelected(true);
		
		endDatePicker.getModel().setDate(endDay.get(Calendar.YEAR), endDay.get(Calendar.MONTH), endDay.get(Calendar.DAY_OF_MONTH));
		endDatePicker.getModel().setSelected(true);
		
		ArrayList<Pair> pairs = lesson.getPairs();
		
		for (int i = 0; i < pairs.size(); i++)
		{
			Person p = pairs.get(i).getPerson();
			personComboBoxes.get(i).setSelectedItem(p);
			Horse h = pairs.get(i).getHorse();
			horseComboBoxes.get(i).setSelectedItem(h);
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
		
		
		
		dateField = new JTextField("" + monthStr[startDay.get(Calendar.MONTH)] + " " + startDay.get(Calendar.DAY_OF_MONTH) + ", " + startDay.get(Calendar.YEAR));
		dateField.setPreferredSize(new Dimension(200, 30));
		dateField.setEditable(false);
		
		typeField = new JTextField(lesson.getType().toString());
		typeField.setPreferredSize(new Dimension(200, 30));
		typeField.setEditable(false);
				
		buildUI();
		addActionListener();
		
		frame.setSize(new Dimension(700,800));
		
		frame.getContentPane().add(this, BorderLayout.CENTER);
		
		frame.currentPanel = this;
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
			
			horseComboBoxes.get(i).setPreferredSize(new Dimension(130, 23));
			this.add(horseComboBoxes.get(i));
		}
		
		this.add(startDateLabel);
		this.add(dateField);
				
		this.add(typeLabel);
		this.add(typeField);

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
			
			layout.putConstraint(SpringLayout.WEST, horseComboBoxes.get(i), 0, SpringLayout.WEST, horseLabel);
			layout.putConstraint(SpringLayout.NORTH, horseComboBoxes.get(i), (30 + (i * 30)), SpringLayout.NORTH, horseLabel);
		}
		
		layout.putConstraint(SpringLayout.WEST, startDateLabel, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, startDateLabel, VIRTICAL_GAP, SpringLayout.NORTH, personComboBoxes.get(NUMPEOPLE - 1));
		
		layout.putConstraint(SpringLayout.WEST, dateField, HORIZONTAL_GAP, SpringLayout.EAST, lessonNumLabel);
		layout.putConstraint(SpringLayout.NORTH, dateField, 0, SpringLayout.NORTH, startDateLabel);
		
		layout.putConstraint(SpringLayout.WEST, typeLabel, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, typeLabel, VIRTICAL_GAP, SpringLayout.NORTH, startDateLabel);
		
		layout.putConstraint(SpringLayout.WEST, typeField, HORIZONTAL_GAP, SpringLayout.EAST, lessonNumLabel);
		layout.putConstraint(SpringLayout.NORTH, typeField, VIRTICAL_GAP, SpringLayout.NORTH, startDateLabel);
		
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
				
				ArrayList<Pair> lessonPairs = new ArrayList<Pair>();
				
				int numPairs = 0;
				//Add all students selected to be in this lesson
				for (int i = 0; i < NUMPEOPLE; i++)
				{
					Object p = personComboBoxes.get(i).getSelectedItem();
					Object h = horseComboBoxes.get(i).getSelectedItem();
					if ((p == null) && (h == null))
					{
						break;
					}
					else if ((p == null) || (h == null))
					{
						System.out.println("Need to assign a person to each horse");
						JOptionPane.showMessageDialog(null, "Need to assign a person to each horse", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					else
					{
						lessonPairs.add(new Pair((Person)p, (Horse)h));
						numPairs++;
					}
					
				}
				
				if (numPairs == 0)
				{
					System.out.println("Need at least 1 person and 1 horse");
					JOptionPane.showMessageDialog(null, "Need at least 1 student in the lesson", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				curLesson.setPairs(lessonPairs);
				
				model.toMainMenu();
				
			}
		});
	}

}
