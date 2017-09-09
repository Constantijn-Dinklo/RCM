
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JOptionPane;

public class Lesson
{
	private Calendar startDate, endDate;
	private int lessonTime, lessonNum;
	private ArrayList<Pair> pairs = new ArrayList<Pair>();
	private boolean paired;
	private LessonType type;
	
	//Help variables when setting up lesson initially
	ArrayList<Person> students = new ArrayList<Person>();
	ArrayList<Horse> availableHorses = new ArrayList<Horse>();
	ArrayPair arrayPair = new ArrayPair();

	public Lesson(){}
	
	public Lesson (ArrayList<Person> students, ArrayList<Horse> horses, Calendar startDate, Calendar endDate, int lessonTime, int lessonNum, LessonType type)
	{
		Iterator<Person> studentIterator = students.iterator();
		while(studentIterator.hasNext())
		{
			this.students.add(studentIterator.next());
		}
		Iterator<Horse> horseIterator = horses.iterator();
		while(horseIterator.hasNext())
		{
			this.availableHorses.add(horseIterator.next());
		}
		this.startDate = startDate;
		this.endDate = endDate;
		this.lessonTime = lessonTime;
		this.lessonNum = lessonNum;
		this.paired = false;
		this.type = type;
	}
	
	//Creates a new lesson with the pairs already initialized.
	//It modifies all fields as if originally creating a lesson and creating pairs, eg, it modifies horse ridden time.
	//This should only be used on the initial setup of the program.
	public Lesson (ArrayList<Pair> pairs, Calendar startDate, Calendar endDate, int lessonTime, int lessonNum, LessonType type)
	{
		Iterator<Pair> pairsIterator = pairs.iterator();
		while(pairsIterator.hasNext())
		{
			Pair tempPair = pairsIterator.next();
			Horse tempHorse = tempPair.getHorse();
			tempHorse.updateTimeRidden(lessonTime, startDate);
			this.pairs.add(tempPair);
		}
		this.startDate = startDate;
		this.endDate = endDate;
		this.lessonTime = lessonTime;
		this.lessonNum = lessonNum;
		this.paired = true;
		this.type = type;
	}

	private void updateList(Person p)
	{
		for (int i = 0; i < arrayPair.getSize(); i++)
		{
			if (arrayPair.getByIndex(i).hasPerson(p))
			{
				arrayPair.updatePair(i, -1, p, true);
				i--;
			}
		}
	}

	private void updateData(Person p, Horse h)
	{
		updateList(p);
		h.updateTimeRidden(lessonTime, startDate);
		students.remove(p);
		availableHorses.remove(h);
	}
	
	//EXPLAIN
	private void buildList()
	{
		//Builds up the List
		for (int i = 0; i < students.size(); i++)
		{
			ArrayList<Horse> studentPref = students.get(i).getStudentPref();
			int importance = 3; //The first horse that a student prefers to ride is added in 3 times, the second one twice and the third horse once.
			for (int l = 0; l < studentPref.size(); l++, importance--)
			{
				if(availableHorses.contains(studentPref.get(l)))
				{
					if (arrayPair.hasKey(studentPref.get(l).getName()))
					{
						System.out.println("Updating ");
						System.out.println(studentPref.get(l).getName() + ", " + students.get(i).getName() + " to the list");
						arrayPair.updatePair(studentPref.get(l).getName(), importance, students.get(i), false);
					}
					else
					{
						System.out.print("Adding ");
						System.out.println(studentPref.get(l).getName() + ", " + students.get(i).getName() + " to the list");
						arrayPair.addPair(studentPref.get(l), importance, students.get(i));
					}
				}
			}
			ArrayList<Horse> teacherPref = students.get(i).getTeacherPref();
			importance = 3; //The first horse that the teacher wants the student to ride is added in 3 times, the second one twice and the third horse once.
			for (int l = 0; l < teacherPref.size(); l++, importance--)
			{
				if(availableHorses.contains(teacherPref.get(l)))
				{
					if (arrayPair.hasKey(teacherPref.get(l).getName()))
					{
						System.out.println("Updating ");
						System.out.println(teacherPref.get(l).getName() + ", " + students.get(i).getName());
						arrayPair.updatePair(teacherPref.get(l).getName(), importance, students.get(i), false);
					}
					else
					{
						System.out.print("Adding ");
						System.out.println(teacherPref.get(l).getName() + ", " + students.get(i).getName());
						arrayPair.addPair(teacherPref.get(l), importance, students.get(i));
					}
				}
			}
		}
	}
	
	//Creates Horse,Person pairs for this lesson. 
	//Uses the proffered horses of a person as a base guideline, but mostly uses randomness to avoid
	//  getting similar pairs multiple weeks in a row (if a lesson is held multiple weeks in a row, is is normally the case)
	public void pair() throws NotEnoughHorses
	{
		buildList();
		arrayPair.print();
		//Organizes and creates the pairs.
		Random rn = new Random();
		while(arrayPair.getSize() > 0)
		{
			
			//This loops runs through the list and determines if only 1 person wants a specific horse and if so, pairs them together. 
// --------------------------------------------------------------------------------------------------------------------------------------------			
//          **KEEP**
			
			/*for(int i = 0; i < arrayPair.getSize(); i++)
			{
				//If only 1 person wants to ride a specific horse, pair them together.
				if ((arrayPair.getByIndex(i).getNumberOfOccurences() == 1) && 
						(availableHorses.contains(arrayPair.getByIndex(i).getHorse())))
				{
					Person student = arrayPair.getByIndex(i).getFirstPerson();
					Horse horse = arrayPair.getByIndex(i).getHorse();
					pairs.add(new Pair(student, horse));
					arrayPair.removePair(i);
					updateData(student, horse);
					i = -1; // starts the loop again, the update of the list might have caused this situation earlier.
				}
			}*/
//----------------------------------------------------------------------------------------------------------------------------------------------			
			
			//Randomly selects a horse and than randomly selects a person that wants to ride that horse and pairs them together.
			if (arrayPair.getSize() > 0)
			{
				int randomNumHorses = rn.nextInt(arrayPair.getSize());
				ArrayList<Person> people = arrayPair.getByIndex(randomNumHorses).getPeople();
				int randomNumPeople = rn.nextInt(people.size());
				Person student = people.get(randomNumPeople);
				Horse horse = arrayPair.getByIndex(randomNumHorses).getHorse();
				pairs.add(new Pair(student, horse));
				arrayPair.removePair(randomNumHorses);
				updateData(student, horse);
				arrayPair.print();
			}
		}
		//Any student that did not get one of their preffered horses is randomly assigned one of the horses that is still available.
		for (int i = 0; i < students.size(); i++)
		{
			if (availableHorses.size() > 0)
			{
				int randNum = rn.nextInt(availableHorses.size());
				pairs.add(new Pair(students.get(i), availableHorses.get(randNum)));
			}
			else{
				throw new NotEnoughHorses(students.size());
			}
		}
		paired = true;
	}
	
	//Getters
	public ArrayList<Pair> getPairs ()
	{
		return pairs;
	}
	public Calendar getStartDate ()
	{
		return startDate;
	}	
	public Calendar getEndDate ()
	{
		return endDate;
	}	
	public int getLessonNum()
	{
		return lessonNum;
	}	
	public int getLessonTime()
	{
		return lessonTime;
	}
	public boolean isPaired()
	{
		return paired;
	}
	public LessonType getType()
	{
		return type;
	}

	
	//Output methods
	public String toString()
	{
		String str = "" + lessonNum + ": ";
		str += startDate.get(Calendar.HOUR_OF_DAY) + ":" + startDate.get(Calendar.MINUTE);
		str += "-";
		str += endDate.get(Calendar.HOUR_OF_DAY) + ":" + endDate.get(Calendar.MINUTE);
		return str;
	}	
	public void display()
	{
		System.out.println("Start Time: ");
		System.out.println("\t" + "Year: " + startDate.get(Calendar.YEAR));
		System.out.println("\t" + "Month: " + startDate.get(Calendar.MONTH));
		System.out.println("\t" + "Day: " + startDate.get(Calendar.DAY_OF_MONTH));
		System.out.println("\t" + "Time: " + startDate.get(Calendar.HOUR_OF_DAY) + ":" + startDate.get(Calendar.MINUTE));
		System.out.println("End Time: ");
		System.out.println("\t" + "Year: " + endDate.get(Calendar.YEAR));
		System.out.println("\t" + "Month: " + endDate.get(Calendar.MONTH));
		System.out.println("\t" + "Day: " + endDate.get(Calendar.DAY_OF_MONTH));
		System.out.println("\t" + "Time: " + endDate.get(Calendar.HOUR_OF_DAY) + ":" + endDate.get(Calendar.MINUTE));
		System.out.println("Pairs:");
		for (int i = 0; i < pairs.size(); i++)
		{
			System.out.println(pairs.get(i).toString());
		}
	}
}