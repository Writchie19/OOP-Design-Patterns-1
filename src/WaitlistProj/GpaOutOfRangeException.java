/***
 * @auther William Ritchie
 * This class is used within the student object to handle when someone attempts to instantiate a student object
 *  with a GPA that is not within the valid range: 0 - 4.0
 */
package WaitlistProj;

public class GpaOutOfRangeException extends Exception 
{
	/**
	 * Default Contructor
	 */
	public GpaOutOfRangeException()
	{
		
	}
	
	/**
	 * 
	 * @param message - the message you want this exception to throw as defined by the user of this class
	 */
	public GpaOutOfRangeException(String message)
	{
		super(message);
	}
}
