
import java.util.ArrayList;
public class HNPList
{

  private Horse horse;
  private int numberOfPeople;
  private ArrayList<Person> people = new ArrayList<Person>();

  public HNPList(Horse horse, int numberOfPeople, Person person) {
    this.horse = horse;
    this.numberOfPeople = numberOfPeople;
    for (int i = 0; i < numberOfPeople; i++)
    {
    	this.people.add(person);
    }
  }

  public Horse getHorse() { return horse; }
  public int getNumberOfPeople() { return numberOfPeople; }
  public ArrayList<Person> getPeople() { return people; }
  
  public Person getFirstPerson() { return people.get(0); }
  
  public Person getPerson(Person p){
    for (int i = 0; i < people.size(); i++)
    {
      if (people.get(i).getName().equals(p.getName()))
        return people.get(i);
    }
    return null;
  }
  
  public boolean hasPerson(Person p) { return people.contains(p); }
  
  public void setHorse(Horse horse) { this.horse = horse; } 
  public void setNumberOfPeople(int numberOfPeople) { this.numberOfPeople = numberOfPeople; }
  public void addPerson(Person person) { people.add(person); }
  
  public void removePerson(Person person) { people.remove(person); }
  
  public void print()
  {
    System.out.print("[ " + horse.getName() + ", " + numberOfPeople);
    for (int i = 0; i < people.size(); i++)
    {
      System.out.print(", " + people.get(i).getName());
    }
    System.out.println(" ]");
  }
}