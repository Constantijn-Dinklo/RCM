
public class NotEnoughHorses extends Exception {

	int numHorsesNeeded = 0;
	public NotEnoughHorses(){}
	
	public NotEnoughHorses(String message)
	{
		super(message);
	}
	
	public NotEnoughHorses(int num)
	{
		numHorsesNeeded = num;
	}
	
	public NotEnoughHorses(String message, int num)
	{
		super(message);
		numHorsesNeeded = num;
	}
}
