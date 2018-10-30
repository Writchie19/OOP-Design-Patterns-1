/***
 * @auther William Ritchie
 * This class is used within the student object to handle when someone attempts to instantiate a student object
 *  with a number of units that is not within the valid range: 0 - 150 units
 */
package assign2;

public class UnitsOutOfRangeException extends Exception
{
	/**
	 * Default Constructor
	 */
	public UnitsOutOfRangeException()
	{
		
	}
	
	/**
	 * 
	 * @param message - the message you want this exception to throw as defined by the user of this class
	 */
	public UnitsOutOfRangeException(String message)
	{
		super(message);
	}
}
