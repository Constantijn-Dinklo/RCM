import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

public class Model implements ActionListener {
	
	//Add name to person/horse mappings
	private ArrayList<Person> personList = new ArrayList<Person>();
	private ArrayList<Horse> horseList = new ArrayList<Horse>();
	private ArrayList<Lesson> lessonList = new ArrayList<Lesson>();
	
	//Organize lessons by their specific date (year, month and day) but not time of day.
	private Map<CustomDate , ArrayList<Lesson>> lessonListWithDate = new HashMap<CustomDate, ArrayList<Lesson>>();
	
	MainFrame frame;
	

	public Model () {
		frame = new MainFrame();
		setup();
		MainMenuPanel main = new MainMenuPanel(this);
		main.setVisible(true);
		frame.display();
	}
	
	//Setup the data, mainly reload all the data from previous use.
	public void setup() {
		fromJSON();
	}
	
	public void toMainMenu() {
		frame.currentPanel.setVisible(false);
		frame.remove(frame.currentPanel);
		frame.setSize(new Dimension(500, 800));
		MainMenuPanel main = new MainMenuPanel(this);
		main.setVisible(true);
	}
	
	public void addHorse(Horse horse){
		horseList.add(horse);
	}
	public void updateHorse(Horse horse, int maxHours, int minHourse){
		horse.setMaxTime(maxHours);
		horse.setMinTime(minHourse);
	}
	public void removeHorse(Horse horse){
		horseList.get(horseList.indexOf(horse)).removeFromPeoplePref(); //This removes the horse from peoples' preference list
		horseList.remove(horse);
	}
	public ArrayList<Horse> getHorseList(){
		return horseList;
	}
	public Horse getHorse(String name)
	{
		Iterator<Horse> horseIterator = horseList.iterator();
		while(horseIterator.hasNext())
		{
			Horse tempHorse = horseIterator.next();
			if (tempHorse.getName().equals(name))
			{
				return tempHorse;
			}
		}
		return null;
	}
	public ArrayList<Horse> getAvailableHorses(int time, Calendar date, LessonType type)
	{
		ArrayList<Horse> horses = new ArrayList<Horse>();
		Iterator<Horse> horseIterator = horseList.iterator();
		while(horseIterator.hasNext())
		{
			Horse tempHorse = horseIterator.next();
			if (tempHorse.canBeRidden(time, date) && tempHorse.getTypes().contains(type))
			{
				horses.add(tempHorse);
			}
		}
		return horses;
	}
	
	public void addPerson(Person person){
		personList.add(person);
	}
	public void addPerson(Person person,  ArrayList<Horse> studentPref, ArrayList<Horse> teacherPref){
		personList.add(person);
		addPersonToHorseList(person, studentPref, teacherPref);
	}
	public void updatePerson(Person person, ArrayList<Horse> studentPref, ArrayList<Horse> teacherPref, Horse notHorse){
		//We are not taking the diff and modifying the changed elements, but rather reassigning all elements.
		//Therefore we simply remove all the previous data.
		for (int i = 0; i < person.getStudentPref().size(); i++)
		{
			person.getStudentPref().get(i).removePerson(person);
		}
		for (int i = 0; i < person.getTeacherPref().size(); i++)
		{
			person.getTeacherPref().get(i).removePerson(person);
		}
		addPersonToHorseList(person, studentPref, teacherPref);
	}
	public void removePerson(Person person){
		for (int i = 0; i < person.getStudentPref().size(); i++)
		{
			person.getStudentPref().get(i).removePerson(person);
		}
		for (int i = 0; i < person.getTeacherPref().size(); i++)
		{
			person.getTeacherPref().get(i).removePerson(person);
		}
		personList.remove(person);
	}	
	public ArrayList<Person> getPersonList(){
		return personList;
	}	
	public Person getPerson(String name)
	{
		Iterator<Person> personIterator = personList.iterator();
		while(personIterator.hasNext())
		{
			Person tempPerson = personIterator.next();
			if (tempPerson.getName().equals(name))
			{
				return tempPerson;
			}
		}
		return null;
	}
	private void addPersonToHorseList(Person person, ArrayList<Horse> studentPref, ArrayList<Horse> teacherPref)
	{
		for (int i = 0; i < studentPref.size(); i++)
		{
			studentPref.get(i).addPerson(person);
		}
		for (int i = 0; i < teacherPref.size(); i++)
		{
			teacherPref.get(i).addPerson(person);
		}
	}
	
	public void addLesson(Lesson lesson){
		lessonList.add(lesson);
		
		//Adding lesson to the list organized by date, used for calendar display 
		Calendar date = lesson.getStartDate();
		CustomDate newDate = new CustomDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		
		if (!lessonListWithDate.containsKey(newDate))
		{
			System.out.println("New Date");
			lessonListWithDate.put(newDate, new ArrayList<Lesson>());
		}
		ArrayList<Lesson> tempLessonList = lessonListWithDate.get(newDate);
		tempLessonList.add(lesson);
		lessonListWithDate.put(newDate, tempLessonList);
	}
	public void removeLesson(Lesson lesson){
		int lessonDuration = lesson.getLessonTime();
		Calendar lessonDate = lesson.getStartDate();
		ArrayList<Pair> pairs = lesson.getPairs();
		for(int i = 0; i < pairs.size(); i++)
		{
			pairs.get(i).getHorse().updateTimeRidden(lessonDuration * -1, lessonDate);
		}
		lessonList.remove(lesson);
		
		CustomDate newDate = new CustomDate(lessonDate.get(Calendar.YEAR), lessonDate.get(Calendar.MONTH), lessonDate.get(Calendar.DAY_OF_MONTH));
		
		//Make sure that this date actually exists in the data base.
		if (lessonListWithDate.containsKey(newDate))
		{
			ArrayList<Lesson> tempLessonList = lessonListWithDate.get(newDate);
			tempLessonList.remove(lesson);
			lessonListWithDate.put(newDate, tempLessonList);
		}

	}	
	public ArrayList<Lesson> getLessonList(){
		return lessonList;
	}
	public Map<CustomDate, ArrayList<Lesson>> getLessonListWithDates()
	{
		return lessonListWithDate;
	}
	public boolean lessonExists(Calendar startDate, Calendar endDate, int lessonNum)
	{
		Iterator<Lesson> lessonIterator = lessonList.iterator();
		while(lessonIterator.hasNext()){
			Lesson curLesson = lessonIterator.next();
			//System.out.println("Checking Lesson");
			if (exactSameDate(startDate, curLesson.getStartDate()) && 
				exactSameDate(endDate, curLesson.getEndDate())){
				return true;
			}
		}
		return false;
	}
	
	public JSONArray serializeHorses(){
		
		JSONArray horseArray = new JSONArray();
		
		Iterator<Horse> horseIterator = horseList.iterator();
		while(horseIterator.hasNext())
		{
			Horse writeHorse = horseIterator.next();
			JSONObject horseInfo = new JSONObject();
			horseInfo.put("Name", writeHorse.getName());
			horseInfo.put("MaxTime", writeHorse.getMaxTime());
			horseInfo.put("MinTime", writeHorse.getMinTime());
			horseInfo.put("BreakTime", writeHorse.getBreakTime());
			horseInfo.put("Jump", writeHorse.canJump());
			horseArray.put(horseInfo);
		}
		return horseArray;
	}
	public void deserializeHorses(JSONArray horses){
		
		for(int i = 0; i < horses.length(); i++)
		{
			JSONObject horse = horses.getJSONObject(i);
			String name = "";
			int maxTime = 0, minTime = 0, breakTime = 0;
			ArrayList<LessonType> types = new ArrayList<LessonType>();
			types.add(LessonType.Normal); //A horse can always be in 'normal' lessons
			if (horse.has("Name"))
			{
				name = horse.getString("Name");
			}
			if (horse.has("MaxTime"))
			{
				maxTime = horse.getInt("MaxTime");
			}
			if (horse.has("MinTime"))
			{
				minTime = horse.getInt("MinTime");
			}
			if (horse.has("BreakTime"))
			{
				breakTime = horse.getInt("BreakTime");
			}
			if (horse.has("Jump"))
			{
				boolean canJump = horse.getBoolean("Jump");
				if (canJump)
				{
					types.add(LessonType.Jump);
				}
			}
			addHorse(new Horse(name, maxTime, minTime, breakTime, types));
		}
	}
	
	public JSONArray serializePersons(){
		
		JSONArray personArray = new JSONArray();
		
		Iterator<Person> personIterator = personList.iterator();
		while(personIterator.hasNext())
		{
			Person writePerson = personIterator.next();
			JSONObject personInfo = new JSONObject();
			personInfo.put("Name", writePerson.getName());
			
			JSONArray studentPrefHorses = new JSONArray();
			Iterator<Horse> studentPrefIt = writePerson.getStudentPref().iterator();
			while(studentPrefIt.hasNext())
			{
				studentPrefHorses.put(studentPrefIt.next().getName());
			}
			personInfo.put("StudentPref", studentPrefHorses);
			
			JSONArray teaherPrefHorses = new JSONArray();
			Iterator<Horse> teacherPrefIt = writePerson.getTeacherPref().iterator();
			while(teacherPrefIt.hasNext())
			{
				teaherPrefHorses.put(teacherPrefIt.next().getName());
			}
			personInfo.put("TeacherPref", teaherPrefHorses);
			
			String notHorseName = "Null";
			if(writePerson.getNotHorse() != null)
			{
				notHorseName = writePerson.getNotHorse().getName();
			}
			personInfo.put("NotPrefHorse", notHorseName);
			personArray.put(personInfo);
		}
		return personArray;
	}
	public void deserializePersons(JSONArray persons){
		
		for (int i = 0; i < persons.length(); i++)
		{
			JSONObject person = persons.getJSONObject(i);
			String name = person.getString("Name");
			
			ArrayList<Horse> studentPrefHorses = new ArrayList<Horse>();
			JSONArray studentPrefHorseNames = person.getJSONArray("StudentPref");
			for(int a = 0; a < studentPrefHorseNames.length(); a++)
			{
				studentPrefHorses.add(getHorse(studentPrefHorseNames.getString(a)));
			}
			
			ArrayList<Horse> teacherPrefHorses = new ArrayList<Horse>();
			JSONArray teacherPrefHorseNames = person.getJSONArray("TeacherPref");
			for(int a = 0; a < teacherPrefHorseNames.length(); a++)
			{
				teacherPrefHorses.add(getHorse(teacherPrefHorseNames.getString(a)));
			}
			

			String notPrefHorseName = person.getString("NotPrefHorse");
			Horse notPrefHorse = getHorse(notPrefHorseName);
			addPerson(new Person(name, studentPrefHorses, teacherPrefHorses, notPrefHorse), studentPrefHorses, teacherPrefHorses);
		}
	}
	
	public JSONArray serializeLessons(){
		JSONArray lessonArray = new JSONArray();
		
		Iterator<Lesson> lessonIterator = lessonList.iterator();
		while(lessonIterator.hasNext())
		{
			Lesson writeLesson = lessonIterator.next();
			JSONObject lessonInfo = new JSONObject();
			
			JSONArray pairArray = new JSONArray();			
			Iterator<Pair> pairIterator =  writeLesson.getPairs().iterator();
			while(pairIterator.hasNext())
			{
				Pair writePair = pairIterator.next();
				JSONObject pairInfo = new JSONObject();
				pairInfo.put("Person", writePair.getPerson().getName());
				pairInfo.put("Horse", writePair.getHorse().getName());
				pairArray.put(pairInfo);
			}
			lessonInfo.put("Pairs", pairArray);
			
			JSONObject lessonStartDate = new JSONObject();
			lessonStartDate.put("Year", writeLesson.getStartDate().get(Calendar.YEAR));
			lessonStartDate.put("Month", writeLesson.getStartDate().get(Calendar.MONTH));
			lessonStartDate.put("Day", writeLesson.getStartDate().get(Calendar.DAY_OF_MONTH));
			lessonStartDate.put("Hour", writeLesson.getStartDate().get(Calendar.HOUR_OF_DAY));
			lessonStartDate.put("Minute", writeLesson.getStartDate().get(Calendar.MINUTE));
			lessonInfo.put("Start-Date", lessonStartDate);
			
			JSONObject lessonEndDate = new JSONObject();
			lessonEndDate.put("Year", writeLesson.getEndDate().get(Calendar.YEAR));
			lessonEndDate.put("Month", writeLesson.getEndDate().get(Calendar.MONTH));
			lessonEndDate.put("Day", writeLesson.getEndDate().get(Calendar.DAY_OF_MONTH));
			lessonEndDate.put("Hour", writeLesson.getEndDate().get(Calendar.HOUR_OF_DAY));
			lessonEndDate.put("Minute", writeLesson.getEndDate().get(Calendar.MINUTE));
			lessonInfo.put("End-Date", lessonEndDate);
			
			lessonInfo.put("Lesson-Num", writeLesson.getLessonNum());
			lessonInfo.put("Lesson-Time", writeLesson.getLessonTime());
			lessonInfo.put("Paired", writeLesson.isPaired());
			lessonInfo.put("Type", writeLesson.getType().toString());
			
			lessonArray.put(lessonInfo);
		}
		return lessonArray;
	}
	public void deserializeLessons(JSONArray lessons){
		
		for (int i = 0; i  < lessons.length(); i++)
		{
			JSONObject lesson = lessons.getJSONObject(i);
			
			ArrayList<Pair> lessonPairs = new ArrayList<Pair>();
			JSONArray pairs = lesson.getJSONArray("Pairs");
			for(int p = 0; p < pairs.length(); p++)
			{
				JSONObject pair = pairs.getJSONObject(p);
				Horse horse = getHorse(pair.getString("Horse"));
				Person person = getPerson(pair.getString("Person"));
				lessonPairs.add(new Pair(person, horse));
			}
			
			Calendar startDay = Calendar.getInstance();
			startDay.setTimeZone(TimeZone.getTimeZone("Europe/Amsterdam"));
			if (lesson.has("Start-Date"))
			{
				JSONObject startDate = new JSONObject();
				startDate = lesson.getJSONObject("Start-Date");
				startDay.set(startDate.getInt("Year"), 
						startDate.getInt("Month"), 
						startDate.getInt("Day"),
						startDate.getInt("Hour"),
						startDate.getInt("Minute"));
			}
			
			Calendar endDay = Calendar.getInstance();
			endDay.setTimeZone(TimeZone.getTimeZone("Europe/Amsterdam"));
			if (lesson.has("End-Date"))
			{
				JSONObject endDate = new JSONObject();
				endDate = lesson.getJSONObject("End-Date");
				endDay.set(endDate.getInt("Year"), 
						endDate.getInt("Month"), 
						endDate.getInt("Day"),
						endDate.getInt("Hour"),
						endDate.getInt("Minute"));
			}
			
			int lessonTime = 0;
			if (lesson.has("Lesson-Time"))
			{
				System.out.println("Getting Lesson Time");
				lessonTime = lesson.getInt("Lesson-Time");
				
			}
			int lessonNum =  0;
			if (lesson.has("Lesson-Num"))
			{
				lessonNum = lesson.getInt("Lesson-Num");
			}
			LessonType type = LessonType.Normal;
			if(lesson.has("Type"))
			{
				String typeString = lesson.getString("Type");
				if (typeString.equals("Normal"))
				{
					type = LessonType.Normal;
				}
				else if (typeString.equals("Jump"))
				{
					type = LessonType.Jump;
				}
			}
			
			addLesson(new Lesson(lessonPairs, startDay, endDay, lessonTime, lessonNum, type));
		}
	}
	
	public void toJSON(){
		JSONObject json = new JSONObject();
		
		JSONArray horseArray = serializeHorses();
		json.put("Horses", horseArray);
		
		JSONArray personArray = serializePersons();
		json.put("Persons", personArray);
		
		JSONArray lessonArray = serializeLessons();
		json.put("Lessons", lessonArray);
		
		System.out.println(json);
		
		try
		{
			FileOutputStream fileOut = new FileOutputStream("Data.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			
			String jsonString = json.toString();
			out.writeObject(jsonString);
	        out.close();
	        fileOut.close();
		}
		catch(IOException i)
		{
			i.printStackTrace();
		}
	}
	
	public void fromJSON()
	{
		FileInputStream fileIn;
		ObjectInputStream in;
		try
		{
			fileIn = new FileInputStream("Data.ser");
	        in = new ObjectInputStream(fileIn);

	        Object o = in.readObject();
	        String jsonString = (String) o;
	        JSONObject json = new JSONObject(jsonString);
	        System.out.println(json);
	        
	        JSONArray horses = new JSONArray();
	        horses = json.getJSONArray("Horses");
	        deserializeHorses(horses);
	        
	        JSONArray persons = new JSONArray();
	        persons = json.getJSONArray("Persons");
	        deserializePersons(persons);
	        
	        JSONArray lessons = new JSONArray();
	        lessons = json.getJSONArray("Lessons");
	        deserializeLessons(lessons);

	        in.close();
	        fileIn.close();
		}
		catch(IOException io)
		{
			System.out.println("File Error");
			io.printStackTrace();
		}
		catch(ClassNotFoundException cls)
		{
			
		}
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("In Model");
		System.out.println("Action");
		System.out.println(e.getActionCommand());
		if(e.getActionCommand().equals("Add Horse"))
		{
			frame.currentPanel.setVisible(false);
			frame.remove(frame.currentPanel);
			HorsePanel hp = new HorsePanel(this, null, "Add");
			hp.setVisible(true);
		}
		else if (e.getActionCommand().equals("Update Horse"))
		{
			frame.currentPanel.setVisible(false);
			frame.remove(frame.currentPanel);
			SelectHorsePanel shp = new SelectHorsePanel(this, "Update");
			shp.setVisible(true);
		}
		else if (e.getActionCommand().equals("Remove Horse"))
		{
			frame.currentPanel.setVisible(false);
			frame.remove(frame.currentPanel);
			SelectHorsePanel shp = new SelectHorsePanel(this, "Remove");
			shp.setVisible(true);
		}
		else if (e.getActionCommand().equals("Add Person"))
		{
			frame.currentPanel.setVisible(false);
			frame.remove(frame.currentPanel);
			PersonPanel pp = new PersonPanel(this, null, "Add");
			pp.setVisible(true);
		}
		else if (e.getActionCommand().equals("Update Person"))
		{
			frame.currentPanel.setVisible(false);
			frame.remove(frame.currentPanel);
			SelectPersonPanel spp = new SelectPersonPanel(this, "Update");
			spp.setVisible(true);
		}
		else if (e.getActionCommand().equals("Remove Person"))
		{
			frame.currentPanel.setVisible(false);
			frame.remove(frame.currentPanel);
			SelectPersonPanel spp = new SelectPersonPanel(this, "Remove");
			spp.setVisible(true);
		}
		else if (e.getActionCommand().equals("Add Lesson"))
		{
			frame.currentPanel.setVisible(false);
			frame.remove(frame.currentPanel);
			LessonPanel lp = new LessonPanel(this, null, "Add");
			lp.setVisible(true);
		}
		else if (e.getActionCommand().equals("Update Lesson"))
		{
			frame.currentPanel.setVisible(false);
			frame.remove(frame.currentPanel);
			SelectLessonPanel slp = new SelectLessonPanel(this, "Update");
			slp.setVisible(true);
		}
		else if (e.getActionCommand().equals("Remove Lesson"))
		{
			frame.currentPanel.setVisible(false);
			frame.remove(frame.currentPanel);
			SelectLessonPanel slp = new SelectLessonPanel(this, "Remove");
			slp.setVisible(true);
		}
		else if (e.getActionCommand().equals("Display Schedule"))
		{
			try {
				frame.currentPanel.setVisible(false);
				frame.remove(frame.currentPanel);
				DisplaySchedulePanel dsp = new DisplaySchedulePanel(this);
				dsp.setVisible(true);
			}
			catch (Exception e1) {}
		}
		else if (e.getActionCommand().equals("Calendar"))
		{
			frame.currentPanel.setVisible(false);
			frame.remove(frame.currentPanel);
			CalendarModel calModel = new CalendarModel(this);
			/*CalendarPanel slp = new CalendarPanel(calModel);
			slp.setVisible(true);*/
		}
		else if(e.getActionCommand().equals("Cancel"))
		{
			toMainMenu();
		}
		else if (e.getActionCommand().equals("Save"))
		{
			toJSON();
		}
		else if (e.getActionCommand().equals("Exit"))
		{
			System.exit(0);
		}
	}
	


	//--------------STARTS OF HELPER FUNCTIONS----------------------------------//
	//Put exactSameDate, sameTime, and sameDate in helper file
	public boolean exactSameDate(Calendar dateOne, Calendar dateTwo)
	{
		return (sameDate(dateOne, dateTwo) && sameTime(dateOne, dateTwo));
	}
	
	public boolean sameTime(Calendar timeOne, Calendar timeTwo)
	{
		int hourOne = timeOne.get(Calendar.HOUR_OF_DAY);
		int minuteOne = timeOne.get(Calendar.MINUTE);
		
		int hourTwo = timeTwo.get(Calendar.HOUR_OF_DAY);
		int minuteTwo = timeTwo.get(Calendar.MINUTE);
		
		System.out.println("time: ");
		System.out.println(hourOne);
		System.out.println(hourTwo);
		System.out.println("");
		System.out.println(minuteOne);
		System.out.println(minuteTwo);
		
		return ((hourOne == hourTwo) && (minuteOne == minuteTwo));
	}

	public boolean sameDate(Calendar dateOne, Calendar dateTwo)
	{	
		int dateOneYear = dateOne.get(Calendar.YEAR);
		int dateOneMonth = dateOne.get(Calendar.MONTH);
		int dateOneDay = dateOne.get(Calendar.DAY_OF_MONTH);
		
		int dateTwoYear = dateTwo.get(Calendar.YEAR);
		int dateTwoMonth = dateTwo.get(Calendar.MONTH);
		int dateTwoDay = dateTwo.get(Calendar.DAY_OF_MONTH);
			
		return ((dateOneYear == dateTwoYear) && (dateOneMonth == dateTwoMonth) && (dateOneDay == dateTwoDay));
	}
	
	public int toTimeOfDayInSeconds(int hour, int minute)
	{
		return ((hour * 3600) + (minute * 60));
	}
	
	//---------------END OF HELPER FUNCTIONS-------------------------------------//
}
