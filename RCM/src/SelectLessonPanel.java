import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilCalendarModel;

public class SelectLessonPanel extends JPanel {
	
	Model model;
	SpringLayout layout;
	
	JLabel dateL, lessonL;
	JDatePanelImpl datePanel;
	JDatePickerImpl datePicker;
	JComboBox<Lesson> lessonComboBox;
	JButton varientB, cancelB;
	
	String varient;
	public SelectLessonPanel (Model model, String varient){
		
		this.model = model;
		this.varient = varient;
		
		MainFrame frame = model.frame;
		
		layout = new SpringLayout();
		this.setLayout(layout);
		
		dateL = new JLabel("Date: ");
		
		//Creates the Date picker
		UtilCalendarModel curModel = new UtilCalendarModel();
		datePanel = new JDatePanelImpl(curModel);
		datePicker = new JDatePickerImpl(datePanel);
		
		lessonL = new JLabel("Lesson: ");
		lessonComboBox = new JComboBox<Lesson>();
		
		varientB = new JButton(varient);
		cancelB = new JButton("Cancel");
		
		addListeners();
		buildUI();
		
		frame.getContentPane().add(this, BorderLayout.CENTER);
		frame.currentPanel = this;
	}
	
	private void addListeners(){
		datePicker.getModel().addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				
				if (evt.getPropertyName().equals("value"))
				{					
					if (evt.getNewValue() != null)
					{
						Calendar datePicked = (Calendar) evt.getNewValue();
					
						Iterator<Lesson> lessonIterator = model.getLessonList().iterator();
						List<Lesson> lessonsOnDate = new ArrayList<Lesson>();
						while(lessonIterator.hasNext())
						{
							Lesson curLesson = lessonIterator.next();
							Calendar lessonDate = curLesson.getStartDate();
							
							if (model.sameDate(datePicked, lessonDate))
							{
								lessonsOnDate.add(curLesson);
							}
						}

						lessonComboBox.removeAllItems();
						lessonIterator = lessonsOnDate.iterator();
						while(lessonIterator.hasNext())
						{
							Lesson curLesson = lessonIterator.next();
							lessonComboBox.addItem(curLesson);
						}
						lessonComboBox.updateUI();
					}
				}
			}
			
		});
		
		varientB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Object l = lessonComboBox.getSelectedItem();
				if (l == null)
				{
					JOptionPane.showMessageDialog(null, "Need to select a Lesson", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					if (varient.equals("Update"))
					{
						model.frame.currentPanel.setVisible(false);
						LessonPanel lp = new LessonPanel(model, (Lesson)l , "Update");
					}
					else
					{
						model.removeLesson((Lesson) l);
						model.toMainMenu();
					}
				}
			}
		});
		
		cancelB.addActionListener(model);
	}
	
	private void buildUI(){
		this.add(dateL);
		this.add(datePicker);
		
		this.add(lessonL);
		this.add(lessonComboBox);
		
		this.add(varientB);
		this.add(cancelB);
		
		layout.putConstraint(SpringLayout.WEST, dateL, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, dateL, 150, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, datePicker, 140, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, datePicker, 150, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, lessonL, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, lessonL, 100, SpringLayout.NORTH, dateL);
		
		layout.putConstraint(SpringLayout.WEST, lessonComboBox, 70, SpringLayout.WEST, lessonL);
		layout.putConstraint(SpringLayout.NORTH, lessonComboBox, 100, SpringLayout.NORTH, datePicker);
		
		layout.putConstraint(SpringLayout.WEST, varientB, 130, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, varientB, 100, SpringLayout.NORTH, lessonL);
		
		layout.putConstraint(SpringLayout.WEST, cancelB, 280, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, cancelB, 100, SpringLayout.NORTH, lessonComboBox);
	}
}
