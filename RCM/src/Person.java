
import java.util.ArrayList;

public class Person
{
  String name;
  ArrayList<Horse> studentPref; 
  ArrayList<Horse> teacherPref;
  Horse notHorse;
  
  public Person(){
	  this.studentPref = new ArrayList<Horse>();
	  this.teacherPref = new ArrayList<Horse>();
  };
    
  public Person(String name, ArrayList<Horse> studentPref, ArrayList<Horse> teacherPref, Horse notHorse)
  {
	  this.studentPref = new ArrayList<Horse>();
	  this.teacherPref = new ArrayList<Horse>();
	  this.name = name;
	  for (int i = 0; i < studentPref.size(); i++)
	  {
		  this.studentPref.add(studentPref.get(i));
	  }
	  for (int i = 0; i < teacherPref.size(); i++)
	  {
		  this.teacherPref.add(teacherPref.get(i));
	  }
	  this.notHorse = notHorse;
  }
  
  public void update(ArrayList<Horse> studentPref, ArrayList<Horse> teacherPref, Horse notHorse)
  {
	  this.studentPref.clear();
	  this.teacherPref.clear();
	  for (int i = 0; i < studentPref.size(); i++)
	  {
		  this.studentPref.add(studentPref.get(i));
	  }
	  for (int i = 0; i < teacherPref.size(); i++)
	  {
		  this.teacherPref.add(teacherPref.get(i));
	  }
	  this.notHorse = notHorse;
  }
  
  public String getName()
  {
    return name;
  }
  
  public ArrayList<Horse> getStudentPref()
  {
    return this.studentPref;
  }
  
  public ArrayList<Horse> getTeacherPref()
  {
	  return this.teacherPref;
  }
  
  public Horse getNotHorse()
  {
	  return notHorse;
  }
  
  public String toString()
  {
	  return getName();
  }
  
  public void print()
  {
    System.out.println(name);
    System.out.println("Num student pref: " + studentPref.size());
    for(int i = 0; i < studentPref.size(); i++)
    {
      System.out.println(studentPref.get(i).getName());
    }
    System.out.println("Num teacher pref: " + teacherPref.size());
    for(int i = 0; i < teacherPref.size(); i++)
    {
    	System.out.println(teacherPref.get(i).getName());
    }
    try
    {
    	if (notHorse != null){
    		System.out.println(notHorse.getName());
    	}
    	else{
    		System.out.println("no none prefference Horse");
    	}
    }
    catch(Exception e)
    {
    	System.out.println("No none prefference Horse");
    }
  }
}