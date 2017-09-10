

public class CalendarModel {
	
	Model model;
	
	CalendarPanel displayPanel;
	
	int currentYear, currentMonth;
	CustomDate selectedDate;
	
	public CalendarModel(Model model)
	{
		this.model = model;
		
		displayPanel = new CalendarPanel(this);
		displayPanel.setVisible(true);
	}
	
	public void setSelectedDate(CustomDate selectedDate)
	{
		//System.out.println("New Date: " + selectedDate.toString());
		this.selectedDate = selectedDate;
		update();
	}
	
	public void update()
	{
		displayPanel.update();
	}
}
