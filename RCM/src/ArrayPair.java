import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import java.util.*;

public class ArrayPair implements java.io.Serializable
{
  
  List<HNPList> horsePersonList = new ArrayList<HNPList>();
  HNPList lastChecked;
  
  public ArrayPair(){}
  
  public void addPair (Horse horse, int numberOfOccurences, Person person)
  {
    horsePersonList.add(new HNPList(horse, numberOfOccurences, person));
  }
  
  public HNPList getByIndex(int i)
  {
    return horsePersonList.get(i);
  }
  
  public boolean hasKey(String key)
  {
    Iterator<HNPList> horsePersonListIterator = horsePersonList.iterator();
    while(horsePersonListIterator.hasNext())
    {
    	HNPList curPair = horsePersonListIterator.next();
    	if (curPair.getHorse().getName().equals(key))
    	{
    		lastChecked = curPair;
    		return true;
    	}
    }
    return false;
  }
  
  public HNPList getByKey(String key)
  {
	//Generally we will want to get the pair that we last checked for in the hasKey section.
	//And this speeds up the process.
	if ((lastChecked != null) && (lastChecked.getHorse().getName().equals(key)))
	{
		return lastChecked;
	}
	Iterator<HNPList> horsePersonListIterator = horsePersonList.iterator();
	while(horsePersonListIterator.hasNext())
	{
	  HNPList tempPair = horsePersonListIterator.next();
	  if (tempPair.getHorse().getName().equals(key))
	    return tempPair;
	}
	return null;
  }
  
   public void updatePair(int index, int amount, Person person, boolean removePerson)
  {
    horsePersonList.get(index).setNumberOfPeople(horsePersonList.get(index).getNumberOfPeople() + amount);
    for (int i = 0; i < Math.abs(amount); i++)
    {
	    if (removePerson)
	    {
	      horsePersonList.get(index).removePerson(person);
	    }
	    else
	    {
	      horsePersonList.get(index).addPerson(person);
	    }
    }
    
    if (horsePersonList.get(index).getNumberOfPeople() == 0)
    {
      horsePersonList.remove(index);
    }
  }
   
  public void updatePair(String key, int amount, Person person, boolean remove)
  {
    for (int i = 0; i < horsePersonList.size(); i++)
    {
      if (horsePersonList.get(i).getHorse().getName().equals(key))
      {
        updatePair(i, amount, person, remove);
      }
    }
  }
  
  public void removePair(int index)
  {
    horsePersonList.remove(index);
  }
  public int getSize()
  {
    return horsePersonList.size();
  }
  
  public void print()
  {
    for (int i =0; i < horsePersonList.size(); i++)
    {
      horsePersonList.get(i).print();
    }
  }
}