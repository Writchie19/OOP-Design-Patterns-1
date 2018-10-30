import java.util.ArrayList;

import assign2.GpaOutOfRangeException;
import assign2.Student;
import assign2.UnitsOutOfRangeException;
import junit.framework.TestCase;

public class TestStudent extends TestCase
{
	ArrayList<Student> studentArray;
	String studentName;
	String redId;
	String email;
	float gpa;
	int unitsTaken;
	
	//Constants:
	private final int ILLEGALUNITS = 155; // Should cause a unitsOutOfRangeException when instantiating a student
	private final float ILLEGALGPA = 4.5f; // Should cause an gpaOutOfRangeException when instantiating a student
	
	public TestStudent(String name) 
	{
		super(name);
	}
	
	protected void setUp()
	{
		studentArray = new ArrayList<Student>();
		studentName = "Will Ritchie";
		redId = "815829203";
		email = "will.ritchie@yahoo.com";
		gpa = 4.0f;
		unitsTaken = 12;
	}
	
	public void testStudentConstructor() throws GpaOutOfRangeException, UnitsOutOfRangeException
	{
		Student testStudent = new Student(studentName, redId, email, gpa, unitsTaken);
		assertTrue("Name", testStudent.getStudentName().equals(studentName));
		assertTrue("Red ID", testStudent.getRedId().equals(redId));
		assertTrue("email", testStudent.getEmail().equals(email));
		assertTrue("GPA", testStudent.getGpa() == gpa);
		assertTrue("Units", testStudent.getUnitsTaken() == unitsTaken);
	}
	
	public void testGpaOutOfRangeException() throws UnitsOutOfRangeException
	{
		try
		{
			@SuppressWarnings("unused")
			Student testStudent = new Student(studentName, redId, email, ILLEGALGPA, unitsTaken);
			fail("Should raise GpaOutOfRangeException");
		}
		catch(GpaOutOfRangeException success)
		{
			
		}
	}
	
	public void testUnitsOutOfRangeException() throws GpaOutOfRangeException
	{
		try
		{
			@SuppressWarnings("unused")
			Student testStudent = new Student(studentName, redId, email, gpa, ILLEGALUNITS);
			fail("Should raise UnitsOutOfRangeException");
		}
		catch(UnitsOutOfRangeException success)
		{
			
		}
	}
	
	protected void tearDown()
	{
		studentArray = null;
		assertNull(studentArray);
	}
}
