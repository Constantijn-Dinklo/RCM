import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class DayPanel extends JPanel {

	CalendarModel calModel;
	
	SpringLayout layout, lessonLayout;
	
	CustomDate curDate;
	
	ArrayList<JButton> lessonB;
	Map<LessonButton, Lesson> buttonToLessonMap;
	JLabel dateLabel;
	JPanel lessonArea;
	
	public DayPanel (CalendarModel calModel, CustomDate curDate)
	{
		this.calModel = calModel;
		this.curDate = curDate;
		
		layout = new SpringLayout();
		this.setLayout(layout);
		
		lessonB = new ArrayList<JButton>();
		buttonToLessonMap = new HashMap<LessonButton, Lesson>();
		
		dateLabel = new JLabel(curDate.toString());
		
		lessonArea = new JPanel();
		lessonLayout = new SpringLayout();
		lessonArea.setLayout(lessonLayout);
		
		createButtonToLessonMappings();
		addActionListener();
		addComponents();
		buildUI();
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height)
	{
		
		super.setBounds(x,y,width,height);
		lessonArea.setPreferredSize(new Dimension(this.getBounds().width - 16, this.getBounds().height - 20));
	}

	private void createButtonToLessonMappings()
	{
		lessonB.clear();
		Map<CustomDate, ArrayList<Lesson>> lessonMap = calModel.model.getLessonListWithDates();
		ArrayList<Lesson> lessons = new ArrayList<Lesson>();
		if(lessonMap.containsKey(curDate))
		{
			lessons = lessonMap.get(curDate);
		}
		
		for(int i = 0; i < lessons.size(); i++)
		{
			Lesson curLesson = lessons.get(i);
			int lessonNum = curLesson.getLessonNum();
			int startHour = curLesson.getStartTime().get(Calendar.HOUR_OF_DAY);
			int startMin = curLesson.getStartTime().get(Calendar.MINUTE);
			String startTime = "" + startHour + ":" + startMin;
			int endHour = curLesson.getEndTime().get(Calendar.HOUR_OF_DAY);
			int endMin = curLesson.getEndTime().get(Calendar.MINUTE);
			String endTime = "" + endHour + ":" + endMin;
			
			String buttonText = (lessonNum + ":  " + startTime + "  -  " + endTime);
			LessonButton tempB = new LessonButton(buttonText);
			lessonB.add(tempB);
			
			buttonToLessonMap.put(tempB, curLesson);
		}
	}

	private void addActionListener()
	{
		for(int i = 0; i < lessonB.size(); i++)
		{
			lessonB.get(i).addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton tmpB = (JButton) e.getSource();
					Lesson clickedLesson = buttonToLessonMap.get(tmpB);
					calModel.model.frame.currentPanel.setVisible(false);
					calModel.model.frame.remove(calModel.model.frame.currentPanel);
					calModel.model.frame.setSize(new Dimension(500, 800));
					calModel.model.frame.setLocationRelativeTo(null);
					UpdateLessonPanel lp = new UpdateLessonPanel(calModel.model, clickedLesson, "Calendar");
					lp.setVisible(true);
				}
		
			});
		}
	}
	
	private void addComponents()
	{
		dateLabel.setPreferredSize(new Dimension(130, 25));
		this.add(dateLabel);
		
		lessonArea.setPreferredSize(new Dimension(this.getBounds().width - 16, this.getBounds().height - 20));
		this.add(lessonArea);
		
		for(int i = 0; i < lessonB.size(); i++)
		{
			lessonArea.add(lessonB.get(i));
		}
	
	}
	
	private void buildUI()
	{
			
		layout.putConstraint(SpringLayout.WEST,	dateLabel, 4, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, dateLabel, 2, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST,	lessonArea, 0, SpringLayout.WEST, dateLabel);
		layout.putConstraint(SpringLayout.NORTH, lessonArea, 10, SpringLayout.SOUTH, dateLabel);
		
		for(int i = 0; i < lessonB.size(); i++)
		{
			if (i == 0)
			{
				lessonLayout.putConstraint(SpringLayout.WEST,	lessonB.get(i), 4, SpringLayout.WEST, this);
				lessonLayout.putConstraint(SpringLayout.NORTH, lessonB.get(i), 27, SpringLayout.NORTH, dateLabel);
				continue;
			}
			
			lessonLayout.putConstraint(SpringLayout.WEST,	lessonB.get(i), 4, SpringLayout.WEST, this);
			lessonLayout.putConstraint(SpringLayout.NORTH, lessonB.get(i), 27, SpringLayout.NORTH, lessonB.get(i - 1));
		}
	}
	
	public void refresh(CustomDate newDate){
		curDate = newDate;
		
		dateLabel.setText(curDate.toString());
		
		
		lessonArea = new JPanel();
		lessonLayout = new SpringLayout();
		lessonArea.setLayout(lessonLayout);
		
		
		createButtonToLessonMappings();
		addActionListener();
		addComponents();
		buildUI();
				
	}
}
