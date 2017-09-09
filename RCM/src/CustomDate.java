
public class CustomDate {
	
	private int year, month, day;
	
	private final int PRIME = 17;
	
	public CustomDate (int year, int month, int day)
	{
		this.year = year;
		this.month = month;
		this.day = day;
	}
	
	@Override
	public int hashCode()
	{
		int curTotal = year;
		curTotal = curTotal * PRIME + month;
		curTotal = curTotal * PRIME + day;
		return curTotal;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof CustomDate))
		{
			return false;
		}
		if (o == this)
		{
			return true;
		}
		CustomDate d = (CustomDate) o;
		return ((this.year == d.year) && (this.month == d.month) && (this.day == d.day));
	}

	//Getters
	public int getYear()
	{
		return year;
	}
	public int getMonth()
	{
		return month;
	}
	public int getDay()
	{
		return day;
	}

	public String toString()
	{
		String str = String.valueOf(this.year);
		str += "-" + String.valueOf(this.month);
		str += "-" + String.valueOf(this.day);
		return str;
	}
}
