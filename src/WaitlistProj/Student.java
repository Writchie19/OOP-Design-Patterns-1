/***
 * @author William Ritchie
 * ASSUMPTIONS:
 * 	 - getters and setters were provided for: students name, red id, email, GPA, and the number of units taken; the assumption is that we want
 * 		users to be able alter such information within this object
 */

package assign2;

/**
 * The implementation of Comparable is important for Student Objects use with the priority queue, 
 */
public class Student implements Comparable<Student>
{
	// Keep the internal data private and encapsulated with in this student object
	private String studentName; 
	private String redId; 
	private String email; 
	private float gpa;
	private int unitsTaken; 
	
	// CONSTANTS:
	private final float MAXIMUMUNITS = 150.0F; // Maximum number of units a student can take
	private final float MAXIMUMGPA = 4.0F; // Maximum GPA a studetnt can have
	
	// The student class throw GpaOutOfRangeException and UnitsOutOfRangeException in order to handle when a user inputs a GPA or number of units taken that
	// are not with in a valid range
	public Student(String studentName, String redId, String email, float gpa, int unitsTaken) throws GpaOutOfRangeException, UnitsOutOfRangeException 
	{
		this.studentName = studentName;
		this.redId = redId;
		this.email = email;
		
		// Check that the GPA is in the valid range; if false then throw the GpaOutOfRangeException
		if (checkGpaInValidRange(gpa))
		{
			this.gpa = gpa;
		}
		else
		{
			throw new GpaOutOfRangeException("GPA is not valid! It must reside between 0 and 4.0");
		}
		
		// Check that the number of units is in the valid range; if false then throw UnitsOutOfRangeException
		if (checkUnitsInValidRange(unitsTaken))
		{
			this.unitsTaken = unitsTaken;
		}
		else
		{
			throw new UnitsOutOfRangeException("Number of units is not valid! It must reside between 0 and 150");
		}
		
	}

	public String getStudentName()
	{
		return studentName;
	}

	public String getRedId()
	{
		return redId;
	}

	public String getEmail()
	{
		return email;
	}

	public float getGpa()
	{
		return gpa;
	}

	public int getUnitsTaken()
	{
		return unitsTaken;
	}

	public void setStudentName(String newName)
	{
		this.studentName = newName;
	}

	public void setRedId(String newRedId)
	{
		this.redId = newRedId;
	}

	public void setEmail(String newEmail)
	{
		this.email = newEmail;
	}

	public void setGpa(float newGpa)
	{
		this.gpa = newGpa;
	}

	public void setUnitsTaken(int newUnitsTaken)
	{
		this.unitsTaken = newUnitsTaken;
	}

	@Override
	public int compareTo(Student otherStudent)
	{
		return ((Float)this.gpa).compareTo(otherStudent.getGpa()); // compareTo defaults to comparing gpas to each other
	}

	@Override
	public String toString() {
		return "Name: " + studentName + "   Red ID: " + redId;
	}

	private boolean checkGpaInValidRange(float gpa)
	{
		if (gpa >= 0.0f && gpa <= MAXIMUMGPA)
		{
			return true;
		}
		return false;
	}

	private boolean checkUnitsInValidRange(int unitsTaken)
	{
		if (unitsTaken >= 0 && unitsTaken <= MAXIMUMUNITS)
		{
			return true;
		}
		return false;
	}
}
