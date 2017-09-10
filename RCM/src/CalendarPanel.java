import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TimeZone;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class CalendarPanel extends JPanel {

	Model model;
	CalendarModel calModel;
	
	static JLabel monthL, yearL;
	static JButton nextB, prevB, backB;
	static JComboBox<String> yearC;
	static JTable calendarT;
	static DefaultTableModel calendarTM;
	static JScrollPane calendarS;
	static int realDay, realMonth, realYear, currentMonth, currentYear;
	
	JPanel calendarArea;
	
	DayPanel dayArea;
			
	public CalendarPanel (CalendarModel calModel)
	{
		this.model = calModel.model;
		this.calModel = calModel;
		MainFrame frame = model.frame;
				
		this.setLayout(null);
		
		monthL = new JLabel("January");
		yearL = new JLabel("Change Year:");
		nextB = new JButton(">>");
		prevB = new JButton("<<");
		backB = new JButton("Back");
		yearC = new JComboBox<String>();
		
		calendarTM = new DefaultTableModel();
		calendarT = new JTable(calendarTM);
		
		calendarS = new JScrollPane(calendarT);
		
		calendarArea = new JPanel();
		calendarArea.setLayout(null);
		calendarArea.setBorder(BorderFactory.createTitledBorder("Calendar"));
		
		//dayArea.setLayout(null);

				
		GregorianCalendar cal = new GregorianCalendar();
		realDay = cal.get(GregorianCalendar.DAY_OF_MONTH);
		realMonth = cal.get(GregorianCalendar.MONTH);
		realYear = cal.get(GregorianCalendar.YEAR);
		currentMonth = realMonth;
		currentYear = realYear;
		
		dayArea = new DayPanel(calModel, new CustomDate(realYear, realMonth, realDay));
		dayArea.setBorder(BorderFactory.createTitledBorder("Day"));
		
		String[] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
		for(int i = 0; i < 7; i++)
		{
			calendarTM.addColumn(headers[i]);
		}
		
		calendarT.getParent().setBackground(calendarT.getBackground());
		calendarT.getTableHeader().setResizingAllowed(false);
		calendarT.getTableHeader().setReorderingAllowed(false);
		
		//Single cell selection
		calendarT.setColumnSelectionAllowed(false);
		calendarT.setRowSelectionAllowed(false);
		//calendarT.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		calendarT.setRowHeight(88);
		calendarTM.setColumnCount(7);
		calendarTM.setRowCount(6);
		
		for(int i = realYear - 100; i <= realYear + 100; i++)
		{
			yearC.addItem(String.valueOf(i));
		}
		//yearC.setSelectedIndex(100);
		refreshCalendar(realMonth, realYear);

		addActionListener();
		buildUI();
		
		frame.setSize(new Dimension(1200,770));
		//frame.setLocationRelativeTo(null);
		frame.getContentPane().add(this, BorderLayout.CENTER);
		
		frame.currentPanel = this;
		
	}
	
	private void addActionListener()
	{
		nextB.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentMonth == 11)
				{
					currentMonth = 0;
					currentYear += 1;
				}
				else
				{
					currentMonth += 1;
				}
				refreshCalendar(currentMonth, currentYear);
			}
		});
		
		prevB.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentMonth == 0)
				{
					currentMonth = 11;
					currentYear -= 1;
				}
				else
				{
					currentMonth -= 1;
				}
				refreshCalendar(currentMonth, currentYear);
			}
		});
		
		backB.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				//Remove later when resizing is added
				model.frame.setSize(new Dimension(500, 700));
				model.frame.setLocationRelativeTo(null);
				
				model.toMainMenu();
			}
	
		});
		
		yearC.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				if (yearC.getSelectedItem() != null)
				{
					String newYear = yearC.getSelectedItem().toString();
					currentYear = Integer.parseInt(newYear);
					refreshCalendar(currentMonth, currentYear);
				}
			}
	
		});
	}
	
	private void buildUI()
	{
		this.add(calendarArea);
		calendarArea.add(monthL);
		calendarArea.add(yearL);
		calendarArea.add(yearC);
		calendarArea.add(prevB);
		calendarArea.add(nextB);
		calendarArea.add(backB);
		calendarArea.add(calendarS);
		
		this.add(dayArea);
		
		calendarArea.setBounds(0, 0, 700, 640);
		prevB.setBounds(10, 25, 50, 25);
		monthL.setBounds(350-monthL.getPreferredSize().width/2, 25, 100, 25);
		nextB.setBounds(640, 25, 50, 25);
		calendarS.setBounds(10, 50, 680, 555);
		yearL.setBounds(10, 605, 80, 20);
		yearC.setBounds(10 + yearL.getPreferredSize().width + 5, 605, 80, 20);
		backB.setBounds(610, 605, 80, 25);
		
		dayArea.setBounds(710, 0, 480, 640);
	}
	
	private void refreshCalendar(int month, int year)
	{
		String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		int numDays, som; //Number of Days, Start Of Month
		
		prevB.setEnabled(true);
		nextB.setEnabled(true);
		if(month == 0 && year <= realYear - 100)
		{
			prevB.setEnabled(false);
		}
		if (month == 11 && year >= realYear + 100)
		{
			nextB.setEnabled(false);
		}
		monthL.setText(months[month]);
		monthL.setBounds(350-monthL.getPreferredSize().width/2, 25, 100, 25);
		yearC.setSelectedItem(String.valueOf(year));
		
		GregorianCalendar cal = new GregorianCalendar(year, month, 1);
		numDays = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		som = cal.get(GregorianCalendar.DAY_OF_WEEK);
		
		//Clear table
		for(int i = 0; i < 6; i++)
		{
			for (int j = 0; j < 7; j++)
			{
				calendarTM.setValueAt(null, i, j);
			}
		}
		
		//Populate Calendar
		for(int i = 1; i <=numDays; i++)
		{
			int row = new Integer((i+som-2)/7);
			int column = (i+som-2)%7;
			CalendarCellPanel datePanel = new CalendarCellPanel(calModel, new CustomDate(currentYear, currentMonth, i));
			
			calendarTM.setValueAt(datePanel, row, column);
			
		}
		
		calendarT.setDefaultRenderer(calendarT.getColumnClass(0), new tblCalendarCell());
		calendarT.setDefaultEditor(calendarT.getColumnClass(0), new tblCalendarCell());
	}

	public void update()
	{
		refreshCalendar(calModel.selectedDate.getMonth(), calModel.selectedDate.getYear());
		dayArea.refresh(calModel.selectedDate);
	}

	public class tblCalendarCell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer
	{

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
			if (value != null)
			{
				CalendarCellPanel datePanel = (CalendarCellPanel) value;
								
				if (column == 0 || column == 6)
				{
					datePanel.setBackground(new Color(255, 220, 200));
				}
				else {
					datePanel.setBackground(new Color(255, 255, 255));
				}
				if (datePanel.panelDate.getDay() == realDay &&
					datePanel.panelDate.getMonth() == realMonth &&
					datePanel.panelDate.getYear() == realYear)
				{
					datePanel.setBackground(new Color(220, 220, 255));
				}
				
				datePanel.setBorder(null);
				datePanel.setForeground(Color.black);
				return datePanel;
			}
			return null;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean selected, int row, int column) {
			if(value != null)
			{
				CalendarCellPanel datePanel = (CalendarCellPanel) value;
				refreshCalendar(currentMonth, currentYear);
				return datePanel;
			}
			return null;
		}
		
		@Override
		public Object getCellEditorValue() {
			return null;
		}
		
	}

}
