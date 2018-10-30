
/***
 * 
 * @author William Ritchie
 * Run the appropriate unit tests for the package: assign1
 * assign1 contains:
 * 		Heap.java
 * 		PriorityQueue.java
 * 		Student.java
 * 		UnitsOutOfRangeException.java
 * 		GpaOutOfRange.java
 */

public class Assign2PackageTester
{
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(TestStudent.class);
		junit.textui.TestRunner.run(TestCommandPattern.class);
		junit.textui.TestRunner.run( TestPriorityQueue.class);
	}
}
