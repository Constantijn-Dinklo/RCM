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
	
	JLabel dateLabel, numLessonsL;
	
	boolean hasLessons;
	
	public CalendarCellPanel(CalendarModel calModel, CustomDate panelDate)
	{
		this.model = calModel.model;
		this.calModel = calModel;
		this.panelDate = panelDate;
		
		this.setSize(new Dimension(140, 88));
		
		layout = new SpringLayout();
		this.setLayout(layout);
		
		setup();
		
		addActionListener();
		buildUI();
	}
	
	private void setup()
	{
		dateLabel = new JLabel("" +panelDate.getDay());
		
		Map<CustomDate, ArrayList<Lesson>> lessonMap = calModel.model.getLessonListWithDates();
		ArrayList<Lesson> lessons = new ArrayList<Lesson>();
		if(lessonMap.containsKey(panelDate))
		{
			lessons = lessonMap.get(panelDate);
		}
		if (lessons.size() > 0)
		{
			hasLessons = true;
		}
		numLessonsL = new JLabel(String.valueOf(lessons.size()) + " lesson(s)");
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
		
		numLessonsL.addMouseListener(new MouseListener()
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
		
		dateLabel.addMouseListener(new MouseListener()
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
		
		
	}

	public void buildUI()
	{
		dateLabel.setPreferredSize(new Dimension(130, 25));
		this.add(dateLabel);
		
		if(hasLessons)
		{
			numLessonsL.setPreferredSize(new Dimension(130, 25));
			this.add(numLessonsL);
		}
		
		layout.putConstraint(SpringLayout.WEST,	dateLabel, 4, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, dateLabel, 2, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST,	numLessonsL, 4, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, numLessonsL, 10, SpringLayout.SOUTH, dateLabel);
	}

}
