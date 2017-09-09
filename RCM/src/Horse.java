
import java.lang.Boolean;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Horse
{
  final int maxDaysInMonth = 31;
  final int monthsInYears = 12;
  
  private String name;
  private int maxTime, minTime, breakTime;
  //private boolean canJump;
  private ArrayList<LessonType> types; //All the different type of lessons this horse can participate in.
  
  //stores the amount of time a horse has been ridden on a certain day.
  //First index is the year, second is an array of months and days for that year. (Which to CustomDate)
  Map<Integer, Integer[][]> timeRidden = new HashMap<>();
  //A List of people that have this horse as their preference.
  transient List<Person> people = new ArrayList<Person>();
  
  public Horse(){};
   
  public Horse(String name, int maxHours, int minHours, int breakTime, ArrayList<LessonType> types)
  {
    this.name = name;
    this.maxTime = maxHours;
    this.minTime = minHours;
    this.breakTime = breakTime;
    //this.canJump = canJump;
    this.types = new ArrayList<LessonType>();
    for (int i = 0; i < types.size(); i++)
    {
    	this.types.add(types.get(i));
    }
  }
  
  //Increases the time a horse has been ridden on a day
  public void updateTimeRidden(int time, Calendar date)
  {
	  System.out.println("Updating");
	  System.out.println(getName());
	  int year = date.get(Calendar.YEAR);
	  int month = date.get(Calendar.MONTH);
	  int day = date.get(Calendar.DAY_OF_MONTH);
	  if (timeRidden.containsKey(year))
	  {
		  if (timeRidden.get(year)[month][day - 1] != null)
		  {
			  int totalTime = timeRidden.get(year)[month][day - 1] + time;
			  timeRidden.get(year)[month][day - 1] = totalTime;
		  }
		  else
		  {
			  timeRidden.get(year)[month][day - 1] = time;
		  }
	  }
	  else
	  {
		  timeRidden.put(year, new Integer[monthsInYears][maxDaysInMonth]);
		  timeRidden.get(year)[month][day - 1] = time;
	  }
  }
  
  //Determines if a horse has the potential to be ridden in a lesson.
  //Returns true if the horse can still be ridden for the added amount of time
  //Return false if the added time would exceed the horses maximum allowed time to be ridden.
  public Boolean canBeRidden (int time, Calendar date)
  {
	  int year = date.get(Calendar.YEAR);
	  int month = date.get(Calendar.MONTH);
	  int day = date.get(Calendar.DAY_OF_MONTH);
	  if (timeRidden.containsKey(year))
	  {
		  int totalTime;
		  if (timeRidden.get(year)[month][day - 1] != null)
		  {
			  totalTime = timeRidden.get(year)[month][day - 1] + time;
		  }
		  else
		  {
			  totalTime = time;
		  }
		  if (totalTime > maxTime)
		  {
			  return false;
		  }
	  }
	  else
	  {
		  timeRidden.put(year, new Integer[12][31]);
	  }
	  return true;
  }
  
  public void addPerson(Person p)
  {
	  if (!people.contains(p))
	  {
		  people.add(p);
	  }
  }
  
  //Removes the horses from a persons preference list (this needs to happen a horse is removed)
  public void removeFromPeoplePref()
  {
	  for(int i = 0; i < people.size(); i++)
	  {
		  if (people.get(i).getStudentPref().contains(this))
			  people.get(i).getStudentPref().remove(this);
		  else if (people.get(i).getTeacherPref().contains(this))
			  people.get(i).getTeacherPref().remove(this);
	  }
  }
  
  public void removePerson(Person p)
  {
	  if (people.contains(p))
	  {
		  people.remove(p);
	  }
  }
  
  public Boolean hasToBeRiden()
  {
	  return true;
    //return hoursRiden < minHours;
  }

  public String getName()
  {
    return name;
  }
  public int getMaxTime()
  {
	  return maxTime;
  }  
  public int getMinTime()
  {
	  return minTime;
  }
  public int getBreakTime()
  {
	  return breakTime;
  }
  public ArrayList<LessonType> getTypes()
  {
	  return types;
  }
  public boolean canJump()
  {
	  return types.contains(LessonType.Jump);
  }
  
  public void setMaxTime(int time)
  {
	  maxTime = time;
  }
  public void setMinTime(int time)
  {
	  minTime = time;
  }

  public String toString()
  {
	  return getName();
  }
  
  public void print()
  {
    System.out.println(name);
    System.out.println(maxTime);
    System.out.println(minTime);
  }
}