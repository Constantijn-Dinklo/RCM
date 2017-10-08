import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilCalendarModel;

public class DisplaySchedulePanel extends JPanel {
	
	Model model;
	SpringLayout layout;
	
	JLabel dateL;
	JDatePanelImpl datePanel;
	JDatePickerImpl datePicker;
	JButton createB, cancelB;

	public DisplaySchedulePanel (Model model){
		
		this.model = model;
		
		MainFrame frame = model.frame;
		
		layout = new SpringLayout();
		this.setLayout(layout);
		
		dateL = new JLabel("Date: ");
		
		//Creates the Date picker
		UtilCalendarModel calModel = new UtilCalendarModel();
		datePanel = new JDatePanelImpl(calModel);
		datePicker = new JDatePickerImpl(datePanel);
		
		createB = new JButton("Print Schedule");
		cancelB = new JButton("Cancel");
		
		addListeners();
		buildUI();
		
		frame.getContentPane().add(this, BorderLayout.CENTER);
		
		frame.currentPanel = this;
		
	}
	
	private void addListeners(){
		createB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					Calendar selectedDate = (Calendar) datePicker.getModel().getValue();
					int selectedYear = selectedDate.get(Calendar.YEAR);
					int selectedMonth = selectedDate.get(Calendar.MONTH);
					int selectedDay = selectedDate.get(Calendar.DAY_OF_WEEK);
					XWPFDocument document = new XWPFDocument();
					String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
					String[] dayNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saterday"};
					FileOutputStream outStream = new FileOutputStream(new File("Lesson-" + monthNames[selectedMonth] + "-" + dayNames[selectedDay - 1] + "'s-" + selectedYear + ".docx"));
					
					XWPFTable table = document.createTable();
					
					XWPFTableRow topRow = table.getRow(0);
					topRow.getCell(0).setText("Student");
					topRow.addNewTableCell();
					
					Iterator<Lesson> lessonIterator = model.getLessonList().iterator();
					Map<Integer, TreeMap<Integer, TreeMap<Integer, Lesson>>> displayLessons = new TreeMap<Integer, TreeMap< Integer, TreeMap<Integer, Lesson>>>();
					while(lessonIterator.hasNext())
					{
						Lesson curLesson = lessonIterator.next();
						CustomDate lessonDate = curLesson.getLessonDate();
						int lessonYear = lessonDate.getYear();
						int lessonMonth = lessonDate.getMonth();
						int lessonDay = lessonDate.getDay();
						
						//Temporary fix for now
						Calendar startDate = curLesson.getStartTime();
						int lessonWeek = startDate.get(Calendar.WEEK_OF_MONTH);
						
						if ((lessonYear == selectedYear) && (lessonMonth == selectedMonth) && (lessonDay == selectedDay))
						{	
							int timeOfLesson = model.toTimeOfDayInSeconds(curLesson.getStartTime().get(Calendar.HOUR_OF_DAY), curLesson.getStartTime().get(Calendar.MINUTE));
							if (!displayLessons.containsKey(timeOfLesson))
							{
								displayLessons.put(timeOfLesson, new TreeMap<Integer, TreeMap<Integer, Lesson>>());
							}
							if (!displayLessons.get(timeOfLesson).containsKey(curLesson.getLessonNum()))
							{
								displayLessons.get(timeOfLesson).put(curLesson.getLessonNum() , new TreeMap<Integer, Lesson>());
							}
							displayLessons.get(timeOfLesson).get(curLesson.getLessonNum()).put(lessonWeek, curLesson);
						}
					}
					
					List<Integer> dateColumn = new ArrayList<Integer>();
					for (int lessonTime: displayLessons.keySet())
					{
						for (int lessonNum: displayLessons.get(lessonTime).keySet())
						{
							for (int lessonWeek: displayLessons.get(lessonTime).get(lessonNum).keySet())
							{
								if(!dateColumn.contains(lessonWeek))
								{
									dateColumn.add(lessonWeek);
								}
							}
						}
					}
					Collections.sort(dateColumn);
					
					
					XWPFTableRow headerRow;
					Lesson curLesson = null;
					for (int lessonTime: displayLessons.keySet())
					{
						for (int lessonNum: displayLessons.get(lessonTime).keySet())
						{
							headerRow = table.createRow();
							
							Map<String, XWPFTableRow> rows = new Hashtable<String, XWPFTableRow>();
							int dateCounter = 0;
							int nameCounter = 0;
							for (int lessonWeek: displayLessons.get(lessonTime).get(lessonNum).keySet())
							{
								curLesson = displayLessons.get(lessonTime).get(lessonNum).get(lessonWeek);
								while(dateColumn.size() >= headerRow.getTableCells().size())
								{
									topRow.addNewTableCell();
									headerRow.createCell();
								}
								if (dateCounter < 1)
								{
									topRow.getCell(dateColumn.indexOf(lessonWeek) + 1).setText("Date: " + curLesson.getLessonDate().getYear() + "/" 
														   			+ (curLesson.getLessonDate().getMonth() + 1) + "/"
														   			+ curLesson.getLessonDate().getDay());
									dateCounter++;
								}
								ArrayList<Pair> pairs = curLesson.getPairs();
								for (int i = 0; i < pairs.size(); i++)
								{
									String personName = pairs.get(i).getPerson().getName();
									if (!rows.containsKey(personName))
									{
										rows.put(personName, table.createRow());
									}
									if (nameCounter < 1)
									{
										rows.get(personName).getCell(0).setText(personName);
									}
									while(dateColumn.size() >= rows.get(personName).getTableCells().size())
									{
										rows.get(personName).createCell();
									}
									rows.get(personName).getCell(dateColumn.indexOf(lessonWeek) + 1).setText(pairs.get(i).getHorse().getName());
								}
								nameCounter++;
								//displayLessons.get(lessonTime).get(lessonNum).get(lessonWeek).display();
							}
							headerRow.getCell(0).setText(curLesson.getStartTime().get(Calendar.HOUR_OF_DAY) + ":" + 
												   curLesson.getStartTime().get(Calendar.MINUTE)+ " - " + 
												   curLesson.getEndTime().get(Calendar.HOUR_OF_DAY) + ":" + 
												   curLesson.getEndTime().get(Calendar.MINUTE));
						}
					}
					document.write(outStream);
					outStream.close();
					document.close();
					model.toMainMenu();
				}
				catch(Exception excep)
				{
					System.out.println("ERROR");
					System.out.println(excep);
				}
			}
		});
		
		cancelB.addActionListener(model);
	}
	
	private void buildUI(){
		this.add(dateL);
		this.add(datePicker);
		
		this.add(createB);
		this.add(cancelB);
		
		layout.putConstraint(SpringLayout.WEST, dateL, 70, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, dateL, 150, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, datePicker, 140, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, datePicker, 150, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, createB, 130, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, createB, 300, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, cancelB, 280, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, cancelB, 300, SpringLayout.NORTH, this);
	}
}
