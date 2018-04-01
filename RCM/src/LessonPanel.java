import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilCalendarModel;

public class LessonPanel extends PopupPanel {
	
	final int NUMPEOPLE = 10;
	
	JLayeredPane thisPanel;
	
	Model model;
	MainFrame frame;
	SpringLayout layout;
	
	JLabel studentLabel, horseLabel, startDateLabel, startTimeLabel, endDateLabel, endTimeLabel, weeklyLabel, timeFormat1, timeFormat2, lessonNumLabel, typeLabel;
	JTextField startTimeField, endTimeField, lessonNumField;
	UtilCalendarModel calModel, endCalModel;
	JDatePanelImpl datePanel, endDatePanel;
	JDatePickerImpl startDatePicker, endDatePicker;
	
	ButtonGroup typeGroup;
	JRadioButton normalR, jumpR;
	
	ArrayList<CustomComboBox<Person>> personComboBoxes;
	ArrayList<CustomComboBox<Horse>> horseComboBoxes;
	ArrayList<JTextField> horseFields;
	
	ArrayList<JTextField> peopleFields;
	
	JButton varientB, cancelB;
	
	String varient;
	Lesson curLesson;
	LessonType curLessonType;
	
	JList<Horse> horseList;
	
		
	public LessonPanel (Model model, Lesson lesson, String varient){
		super(varient);
		
		thisPanel = new JLayeredPane();
		this.model = model;
		this.curLesson = lesson;
		this.varient = varient;
		
		this.frame = model.frame;

		layout = new SpringLayout();
		this.setLayout(layout);

		studentLabel = new JLabel("Students: ");
		horseLabel = new JLabel("Horses: ");
		startDateLabel = new JLabel("Start Date: ");
		endDateLabel = new JLabel("End Date: ");
		weeklyLabel = new JLabel("Weekly: ");
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
		calModel = new UtilCalendarModel();
		datePanel = new JDatePanelImpl(calModel);
		startDatePicker = new JDatePickerImpl(datePanel);
		
		endCalModel = new UtilCalendarModel();
		endDatePanel = new JDatePanelImpl(endCalModel);
		endDatePicker = new JDatePickerImpl(endDatePanel);
	
		Calendar tmpCal = Calendar.getInstance();
		
		startDatePicker.getModel().setDate(tmpCal.get(Calendar.YEAR), tmpCal.get(Calendar.MONTH), tmpCal.get(Calendar.DAY_OF_MONTH));
		startDatePicker.getModel().setSelected(true);
		
		endDatePicker.getModel().setDate(tmpCal.get(Calendar.YEAR), tmpCal.get(Calendar.MONTH), tmpCal.get(Calendar.DAY_OF_MONTH));
		endDatePicker.getModel().setSelected(true);
				

		//Creating all the ComboBox's
		personComboBoxes = new ArrayList<CustomComboBox<Person> >();
		horseComboBoxes = new ArrayList<CustomComboBox<Horse>>();
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
			
			CustomComboBox<Horse> horseBox = new CustomComboBox<Horse>();
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
		
		
		varientB = new JButton(varient);
		cancelB = new JButton("Cancel");
		
		curLessonType = LessonType.Normal;
		
		addActionListeners();
				
		endDateLabel.setVisible(false);
		endDatePicker.setVisible(false);
	}
	
	private void addActionListeners()
	{
		cancelB.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				getFrame().close();
			}
		});

	}
}
