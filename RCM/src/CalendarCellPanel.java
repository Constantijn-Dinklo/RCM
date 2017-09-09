import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class CalendarCellPanel extends JPanel {

	Model model;
	CalendarModel calModel;
	CustomDate panelDate;
	
	SpringLayout layout;
	
	JLabel dateLabel;
	ArrayList<JButton> lessonB;
	Map<JButton, Lesson> buttonToLessonMap;
	
	public CalendarCellPanel(CalendarModel calModel, CustomDate panelDate)
	{
		this.model = calModel.model;
		this.calModel = calModel;
		this.panelDate = panelDate;
		
		this.setSize(new Dimension(140, 88));
		
		layout = new SpringLayout();
		this.setLayout(layout);
		
		
		lessonB = new ArrayList<JButton>();
		buttonToLessonMap = new HashMap<JButton, Lesson>();
		setup();
		
		addActionListener();
		buildUI();
	}
	
	private void setup()
	{
		dateLabel = new JLabel("" +panelDate.getDay());
		
		Map<CustomDate, ArrayList<Lesson>> lessonMap = model.getLessonListWithDates();
		ArrayList<Lesson> lessons = new ArrayList<Lesson>();
		if(lessonMap.containsKey(panelDate))
		{
			lessons = lessonMap.get(panelDate);
		}
		
		for(int i = 0; i < lessons.size(); i++)
		{
			Lesson curLesson = lessons.get(i);
			int lessonNum = curLesson.getLessonNum();
			int startHour = curLesson.getStartDate().get(Calendar.HOUR_OF_DAY);
			int startMin = curLesson.getStartDate().get(Calendar.MINUTE);
			String startTime = "" + startHour + ":" + startMin;
			int endHour = curLesson.getEndDate().get(Calendar.HOUR_OF_DAY);
			int endMin = curLesson.getEndDate().get(Calendar.MINUTE);
			String endTime = "" + endHour + ":" + endMin;
			
			JButton tempB = new JButton(lessonNum + ":  " + startTime + "  -  " + endTime);
			lessonB.add(tempB);
			
			buttonToLessonMap.put(tempB, curLesson);
		}
	}
	
	private void addActionListener()
	{
		this.addMouseListener(new MouseListener()
		{

			@Override
			public void mouseClicked(MouseEvent e) {
				//Don't need
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				//Don't need
			}

			@Override
			public void mouseExited(MouseEvent e) {
				//Don't need
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				calModel.setSelectedDate(panelDate);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				//Don't need				
			}
	
		});
		/*for(int i = 0; i < lessonB.size(); i++)
		{
			lessonB.get(i).addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) {
					JButton tmpB = (JButton) e.getSource();
					Lesson clickedLesson = buttonToLessonMap.get(tmpB);
					model.frame.currentPanel.setVisible(false);
					model.frame.remove(model.frame.currentPanel);
					model.frame.setSize(new Dimension(500, 700));
					model.frame.setLocationRelativeTo(null);
					LessonPanel lp = new LessonPanel(model, clickedLesson, "Calendar");
					lp.setVisible(true);
				}
		
			});
		}*/
	}

	public void buildUI()
	{
		dateLabel.setPreferredSize(new Dimension(130, 25));
		this.add(dateLabel);
		
		for(int i = 0; i < lessonB.size(); i++)
		{
			lessonB.get(i).setPreferredSize(new Dimension(130, 25));
			this.add(lessonB.get(i));
		}
			
		layout.putConstraint(SpringLayout.WEST,	dateLabel, 4, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, dateLabel, 2, SpringLayout.NORTH, this);
		
		for(int i = 0; i < lessonB.size(); i++)
		{
			if (i == 0)
			{
				layout.putConstraint(SpringLayout.WEST,	lessonB.get(i), 4, SpringLayout.WEST, this);
				layout.putConstraint(SpringLayout.NORTH, lessonB.get(i), 27, SpringLayout.NORTH, dateLabel);
				continue;
			}
			
			layout.putConstraint(SpringLayout.WEST,	lessonB.get(i), 4, SpringLayout.WEST, this);
			layout.putConstraint(SpringLayout.NORTH, lessonB.get(i), 27, SpringLayout.NORTH, lessonB.get(i - 1));
		}
	}

}
