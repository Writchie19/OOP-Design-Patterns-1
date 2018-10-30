/***
 * Test Contributors:  
 * @author William Ritchie
 * ASSUMPTIONS: 
 *  - This also tests the functionality of the Student class, and CommandProcess class
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random; // Use for random generation of Gpa's and Units for Student Objects

import assign2.CommandProcessor;
import assign2.PluggableCommand;
import assign2.PriorityQueue;
import assign2.Student;
import junit.framework.TestCase; // Used for Junit testing, TestCase provides the structure necessary to be utilized by TestRunner in Assign1PackageTester

// The class name must begin with "Test" in order to be recognized by the Junit framework
public class TestPriorityQueue<E extends Comparable<E>> extends TestCase 
{ 
	private HashMap<String, PriorityQueue<E>> pqsToTest; // Underlying datastructure to be used by this test class, holds different priorityqueues, that may hold different types of objects
	// The following are name after the type of constructor to be used when instantiating the priorityqueue
	private final String defaultStudentPq = "pq1";  // pq1 == priorityqueue1 ... etc...
	private final String collectStudentPq = "pq2";
	private final String compStudentPq = "pq3";
	private final String collectCompStudentPq = "pq4";
	private CommandProcessor cmdProcessor; // provides undo functionality for mutiple commands
	
	// Constants used in the priority algorithm for this test class
	private final float UNITSWEIGHT = 0.7f;
	private final float GPAWEIGHT = 0.3f;
	private final float MAXUNITS = 150.0f;
	private final float MAXGPA = 4.0f;

	// setUp and tearDown are called for each test with in this test class, respectively before the test begins and after the ends
	@SuppressWarnings("unchecked")
	@Override
	protected void setUp()
	{
		cmdProcessor = new CommandProcessor();
		pqsToTest = new HashMap<String, PriorityQueue<E>>();
		pqsToTest.put(defaultStudentPq,  (PriorityQueue<E>) new PriorityQueue<Student>());
		pqsToTest.put(collectStudentPq, (PriorityQueue<E>) new PriorityQueue<Student>(createRandomStudentCollection()));
		// Lambda function used as to supply the algorithm to deytermine priority
		// Priority: unitWeight * (numUnits / maxunits) + gpaWeight * (gpa / maxgpa)
		pqsToTest.put(compStudentPq, (PriorityQueue<E>) new PriorityQueue<Student>((x,y) -> ((Float)((UNITSWEIGHT * ((float)x.getUnitsTaken() / MAXUNITS)) + (GPAWEIGHT * (x.getGpa() / MAXGPA)))).compareTo((Float)((UNITSWEIGHT * ((float)y.getUnitsTaken()) / MAXUNITS) + (GPAWEIGHT * (y.getGpa() / MAXGPA))))));		
		pqsToTest.put(collectCompStudentPq,  (PriorityQueue<E>) new PriorityQueue<Student>(createRandomStudentCollection(), (x,y) -> ((Float)((UNITSWEIGHT * ((float)x.getUnitsTaken() / MAXUNITS)) + (GPAWEIGHT * (x.getGpa() / MAXGPA)))).compareTo((Float)((UNITSWEIGHT * ((float)y.getUnitsTaken()) / MAXUNITS) + (GPAWEIGHT * (y.getGpa() / MAXGPA))))));
	}

	public TestPriorityQueue(String name)
	{
		// TestCase requires that super be called
		super(name);
	}

	public void testDefaultConstructor()
	{
		assertTrue("Default Constructor is empty: ", pqsToTest.get(defaultStudentPq).isEmpty());
	}
	
	public void testComparatorConstructor()
	{
		assertTrue("Collection Constructor is not empty: ", pqsToTest.get(compStudentPq).isEmpty());
		assertTrue("Default Comparator is natural order: ", pqsToTest.get(compStudentPq).comparator() != Comparator.naturalOrder());
	}

	public void testCollectionConstructor_StudentObjects()
	{
		assertFalse("Collection Constructor is not empty: ", pqsToTest.get(collectStudentPq).isEmpty());
		assertTrue("Default Comparator is natural order: ", pqsToTest.get(collectStudentPq).comparator() == Comparator.naturalOrder());
	}
	
	public void testCollectionComparatorConstructor_StudentObjects()
	{
		assertFalse("Collection Constructor is not empty: ", pqsToTest.get(collectCompStudentPq).isEmpty());
		assertTrue("Default Comparator is not natural order: ", pqsToTest.get(collectCompStudentPq).comparator() != Comparator.naturalOrder());
	}
	
	public void testToArray()
	{
		PriorityQueue<E> qToArray = new PriorityQueue<E>(pqsToTest.get(collectStudentPq));
		Object[] tempArray = qToArray.toArray();
		for (int index = 0; index < qToArray.size(); index++)
		{
			assertTrue("PriorityQueue contains the elements returned by toArray", pqsToTest.get(collectStudentPq).contains(tempArray[index]));
		}
	}

	public void testOfferContains_StudentObjects()
	{
		for (E e : pqsToTest.get(collectCompStudentPq))
		{
			pqsToTest.get(defaultStudentPq).offer(e);
			assertTrue("Does the PriorityQueue contain the recently offered element : ", pqsToTest.get(defaultStudentPq).contains(e));
		}
		assertFalse("Does the PriorityQueue contain elements after offer(): ", pqsToTest.get(defaultStudentPq).isEmpty());
	}

	public void testPeekPoll_StudentObjects()
	{
		// prevStudent priority should be greater than the currStudent
		E prevStudent = null;
		E currStudent = null;
		E peekStudent = null; // compared with currStudent in order to verify that peek and poll methods are returning the same object as they should
		Comparator<E> compStudent = pqsToTest.get(collectCompStudentPq).comparator(); // Use the correct comparator to ensure correct priorty when comparing elements
		
		// The size will decrement as the PriorityQueue is continuously polled
		while (0 != pqsToTest.get(collectCompStudentPq).size())
		{
			if (null == prevStudent)
			{
				currStudent = pqsToTest.get(collectCompStudentPq).peek();
				prevStudent = currStudent;
			}
			else
			{
				peekStudent = pqsToTest.get(collectCompStudentPq).peek();
				currStudent = pqsToTest.get(collectCompStudentPq).poll();
				assertTrue("Does Peek return highest priority Student",  peekStudent == currStudent);
				assertTrue("Does Poll return highest priority Student", compStudent.compare(prevStudent, currStudent) >= 0);
				prevStudent = currStudent;
			}
		}
	}
	
	public void testConsecutiveOfferPeekPoll_StudentObjects()
	{
		for (E e : pqsToTest.get(collectCompStudentPq))
		{
			assertTrue("Successful Consecutive Offer: ", pqsToTest.get(defaultStudentPq).offer(e));
			assertTrue("Successful Consecutive Peek: ", pqsToTest.get(defaultStudentPq).peek() == e);
			assertTrue("Successful Consecutive Poll: ", pqsToTest.get(defaultStudentPq).poll() == e);
			assertTrue("After successful offer-peek-poll method calls, the PriorityQueue is empty: ", pqsToTest.get(defaultStudentPq).isEmpty());
		}
		
	}
	
	public void testClear_StudentObjects()
	{
		pqsToTest.get(collectCompStudentPq).clear();
		assertTrue("Testing clear() functionality, Priority Queue is empty: ", pqsToTest.get(collectCompStudentPq).isEmpty());
	}
	
	/*
	 * Test the PriorityQueue iterator which should guarantee objects are returned in priority queue order
	 * Note: this is slightly different from java's iterator which does not guarantee objects to be returned in priority order
	 */
	public void testPriorityOrderIterator_StudentObjects()
	{
		// prevStudent priority should be greater than the currStudent
		E prevStudent = null;
		Comparator<E> compStudent = pqsToTest.get(collectCompStudentPq).comparator(); // Use the correct comparator to ensure correct priorty when comparing elements
		for (E currStudent : pqsToTest.get(collectCompStudentPq))
		{
			if (null == prevStudent)
			{
				prevStudent = currStudent;
			}
			else
			{
				assertTrue("Is the PriorityOrder Iterator returning students in priority order: ", compStudent.compare(prevStudent, currStudent) >= 0);
				prevStudent = currStudent;
			}
		}
	}
	
	// Tests the CommandProcessor holds multiple commands, executes them properly, and then undos them all
	@SuppressWarnings("unchecked")
	public void testCommandProcessor_PriorityQueue_StudentObjects()
	{
		Collection<Student> studentCol = createRandomStudentCollection();
		
		// Populates the PriorityQueue that used the comparator Cosntructor with the elements from the a collection of students, wrapping each offer() call in a pluggableCommand
		// In this case remove is the appropriate inverse to offer, not poll as that may not remove the correct element
		for (Student student : studentCol)
		{
			cmdProcessor.doIt(new PluggableCommand(() -> pqsToTest.get(compStudentPq).offer((E) student), () -> pqsToTest.get(compStudentPq).remove(student)));
		}
		
		assertFalse("PriorityQueue should not be empty", pqsToTest.get(compStudentPq).isEmpty());
		
		// prevStudent priority should be greater than the currStudent
		E prevStudent = null;
		Comparator<E> compStudent = pqsToTest.get(compStudentPq).comparator(); // Use the correct comparator to ensure correct priorty when comparing elements
		
		for (E currStudent : pqsToTest.get(compStudentPq))
		{
			if (null == prevStudent)
			{
				prevStudent = currStudent;
			}
			else
			{
				assertTrue("Is the PriorityOrder Iterator returning students in priority order: ", compStudent.compare(prevStudent, currStudent) >= 0);
				prevStudent = currStudent;
			}
		}
		
		for (int i = 0; i < studentCol.size(); i++)
		{
			cmdProcessor.undoIt();
		}
		assertTrue("PriorityQueue should now be empty: ", pqsToTest.get(compStudentPq).isEmpty());
	}
	
	// Uses the methods randomUnitsGenerator and randomGpaGenerator to create random student objects
	public Collection<Student> createRandomStudentCollection()
	{
		Collection<Student> studentList = new ArrayList<Student>(); //studentList to be returned
		
		String[] name = {"Plato", "Aristotle", "Socrates", "Immanuel Kant", "John Locke", "William Ritchie","Chris Cornell", "Eddie Vedder", "The Beatles", "Red Hot Chile Peppers"};
		String[] redID = {"243232444", "123432344", "234565667", "587654323", "567453354", "815829203","096840322", "154604933", "908500345", "597865409", "456432243"};
		String[] email = {"playToe@gmail.com", "Air_Is_Totle@gmail.com", "So_crates123@yahoo.com", "Kant_Imman2345@gmail.com", "LockJ@gmail.com" , "will.ritchie@yahoo.com","Audioslave@gmail.com", "purlJam@gmail.com", "HeyJude123@yahoo.com", "otherside@gmail.com"};
		
		try
		{
			for (int index = 0; index < name.length - 1; index++)
			{
				studentList.add(new Student(name[index], redID[index], email[index], randomGpaGenerator(), randomUnitsGenerator()));
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		return studentList;
	}

	public float randomGpaGenerator()
	{
		float[] gpa = {2.99f, 3.67f, 3.44f, 1.50f, 3.15f, 4.00f, 3.95f, 3.45f, 3.40f,  3.00f, 2.95f};
		Random rndIndexGen = new Random(); // This is used as a random index to the gpa array
		return gpa[rndIndexGen.nextInt(gpa.length)]; // The gpa.length provides the bound the nextInt method, else any random integer would be selected 
	}

	public int randomUnitsGenerator()
	{
		int[] unitsTaken = {150, 127, 145, 80, 95, 6, 146, 140, 125, 90, 65}; 
		Random rndIndexGen = new Random(); // This is used as a random index to the unitsTaken array
		return unitsTaken[rndIndexGen.nextInt(unitsTaken.length)]; // The unitTaken.length provides the bound the nextInt method, else any random integer would be selected
	}

	@Override
	protected void tearDown()
	{
		pqsToTest = null;
		cmdProcessor = null;
		assertNull(cmdProcessor);
		assertNull(pqsToTest);
	}
}
