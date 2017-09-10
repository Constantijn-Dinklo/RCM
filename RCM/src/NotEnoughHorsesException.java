
public class NotEnoughHorsesException extends Exception {

	int numHorsesNeeded = 0;
	public NotEnoughHorsesException(){}
	
	public NotEnoughHorsesException(String message)
	{
		super(message);
	}
	
	public NotEnoughHorsesException(int num)
	{
		numHorsesNeeded = num;
	}
	
	public NotEnoughHorsesException(String message, int num)
	{
		super(message);
		numHorsesNeeded = num;
	}
}
