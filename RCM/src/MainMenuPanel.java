
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainMenuPanel extends JPanel
{
	
	JLabel title;
	
	JButton addHorseB, updateHorseB, removeHorseB;
	JButton addPersonB, updatePersonB, removePersonB;
	JButton addLessonB, updateLessonB, removeLessonB;
	JButton viewScheduleB, calendarB;
	JButton saveB, exitB;

	public MainMenuPanel(Model model)
	{
		MainFrame frame = model.frame;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(20,150,10,10));
		this.add(Box.createHorizontalGlue());

		title = new JLabel("Lesson Planner");
		
		//Setting up Buttons for Main Menu
		//Creating Buttons
		addHorseB = new JButton("Add Horse");
		updateHorseB = new JButton("Update Horse");
		removeHorseB = new JButton ("Remove Horse");
		addPersonB = new JButton("Add Person");
		updatePersonB = new JButton("Update Person");
		removePersonB = new JButton("Remove Person");
		addLessonB = new JButton("Add Lesson");
		updateLessonB = new JButton("Update Lesson");
		removeLessonB = new JButton("Remove Lesson");
		viewScheduleB = new JButton("Display Schedule");
		calendarB = new JButton("Calendar");
		saveB = new JButton("Save");
		exitB = new JButton("Exit");

		//Setting Size for Buttons
		addHorseB.setMaximumSize(new Dimension(200, 30));
		updateHorseB.setMaximumSize(new Dimension(200, 30));
		removeHorseB.setMaximumSize(new Dimension(200, 30));
		addPersonB.setMaximumSize(new Dimension(200, 30));
		updatePersonB.setMaximumSize(new Dimension(200, 30));
		removePersonB.setMaximumSize(new Dimension(200, 30));
		addLessonB.setMaximumSize(new Dimension(200, 30));
		updateLessonB.setMaximumSize(new Dimension(200, 30));
		removeLessonB.setMaximumSize(new Dimension(200, 30));
		viewScheduleB.setMaximumSize(new Dimension(200, 30));
		calendarB.setMaximumSize(new Dimension(200, 30));;
		saveB.setMaximumSize(new Dimension(200, 30));
		exitB.setMaximumSize(new Dimension(200, 30));

		//adding actionlistner
		addHorseB.addActionListener(model);
		updateHorseB.addActionListener(model);
		removeHorseB.addActionListener(model);
		addPersonB.addActionListener(model);
		updatePersonB.addActionListener(model);
		removePersonB.addActionListener(model);
		addLessonB.addActionListener(model);
		updateLessonB.addActionListener(model);
		removeLessonB.addActionListener(model);
		viewScheduleB.addActionListener(model);
		calendarB.addActionListener(model);
		saveB.addActionListener(model);
		exitB.addActionListener(model);

		title.setMaximumSize(new Dimension(200, 30));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(title);
		//this.add(title, BoxLayout.PAGE_AXIS);
		this.add(Box.createRigidArea(new Dimension(0,30)));
		
		//adding buttons to the JPanel
		this.add(addHorseB);
		this.add(Box.createRigidArea(new Dimension(0,5)));
		this.add(updateHorseB);
		this.add(Box.createRigidArea(new Dimension(0,5)));
		this.add(removeHorseB);
		this.add(Box.createRigidArea(new Dimension(0,40)));
		this.add(addPersonB);
		this.add(Box.createRigidArea(new Dimension(0,5)));
		this.add(updatePersonB);
		this.add(Box.createRigidArea(new Dimension(0,5)));
		this.add(removePersonB);
		this.add(Box.createRigidArea(new Dimension(0, 40)));
		this.add(addLessonB);
		this.add(Box.createRigidArea(new Dimension(0,5)));
		this.add(updateLessonB);
		this.add(Box.createRigidArea(new Dimension(0,5)));
		this.add(removeLessonB);
		this.add(Box.createRigidArea(new Dimension(0, 40)));
		this.add(viewScheduleB);
		this.add(Box.createRigidArea(new Dimension(0,5)));
		this.add(calendarB);
		this.add(Box.createRigidArea(new Dimension(0, 40)));
		this.add(saveB);
		this.add(Box.createRigidArea(new Dimension(0,5)));
		this.add(exitB);

		frame.getContentPane().add(this, BorderLayout.CENTER);
		
		frame.currentPanel = this;
	}

	
}