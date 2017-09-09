
public class Pair
{
  private Person person;
  private Horse horse;
  
  public Pair(Person p, Horse h)
  {
    this.person = p;
    this.horse = h;
  }
  
  public String toString()
  {
    return "(" + person.getName() + "," + horse.getName() + ")";
  }
  
  public Person getPerson()
  {
    return person;
  }
  
  public Horse getHorse()
  {
    return horse;
  }
  
  public void setPerson(Person p)
  {
    this.person = p;
  }
  
  public void setHorse(Horse h)
  {
    this.horse = h;
  }
}