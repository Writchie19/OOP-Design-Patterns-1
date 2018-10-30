import junit.framework.TestCase;
import java.util.ArrayList;

import WaitlistProj.CommandProcessor;
import WaitlistProj.PluggableCommand;

//Tests CommandProcessors ability to execute and undo PluggableCommands
public class TestCommandPattern extends TestCase
{
	CommandProcessor cmdProcessor;
	ArrayList<Integer> arrayToUndo; // Datastructure that command executions are tested on
	
	
	public TestCommandPattern(String name)
	{
		super(name);
	}
	
	protected void setUp()
	{
		cmdProcessor = new CommandProcessor();
		arrayToUndo = new ArrayList<Integer>();
	}
	
	public void testPluggableCommands()
	{
		arrayToUndo.add(7);
		PluggableCommand removeCmd = new PluggableCommand(() -> arrayToUndo.remove(0), () -> arrayToUndo.add(7));
		removeCmd.execute(); //Should remove 7 from the array
		assertFalse("Execute Command: ", arrayToUndo.contains(7));
		removeCmd.undo(); //Add 7 back to the array 
		assertTrue("Undo Command: ", arrayToUndo.contains(7));
	}
	
	public void testCommandProcessor()
	{
		assertTrue("Array is empty: ", arrayToUndo.size() == 0);
		PluggableCommand add1 = new PluggableCommand(() -> arrayToUndo.add(1), () -> arrayToUndo.remove(0));
		cmdProcessor.doIt(add1);
		PluggableCommand add2 = new PluggableCommand(() -> arrayToUndo.add(2), () -> arrayToUndo.remove(1));
		cmdProcessor.doIt(add2);
		PluggableCommand add3 = new PluggableCommand(() -> arrayToUndo.add(3), () -> arrayToUndo.remove(2));
		cmdProcessor.doIt(add3);
		PluggableCommand add4 = new PluggableCommand(() -> arrayToUndo.add(4), () -> arrayToUndo.remove(3));
		cmdProcessor.doIt(add4);
		PluggableCommand add5 = new PluggableCommand(() -> arrayToUndo.add(5), () -> arrayToUndo.remove(4));
		cmdProcessor.doIt(add5);
		PluggableCommand add6 = new PluggableCommand(() -> arrayToUndo.add(6), () -> arrayToUndo.remove(5));
		cmdProcessor.doIt(add6);
		PluggableCommand add7 = new PluggableCommand(() -> arrayToUndo.add(7), () -> arrayToUndo.remove(6));
		cmdProcessor.doIt(add7);
		
		int arraySize = arrayToUndo.size();
		int element = 7;
		assertTrue("CommandProcessor doIt -> execute() added 7 elements to array: ", arraySize == 7);
		for(int i = 0; i < arraySize; i++)
		{
			assertTrue("Array Contains the element at the bottom of the array", arrayToUndo.contains(element));
			cmdProcessor.undoIt(); //undo the most recent command
			assertFalse("Array no longer Contains the element at the bottom of the array", arrayToUndo.contains(element));
			element--;
		}
	}
	
	protected void tearDown()
	{
		cmdProcessor = null;
		arrayToUndo = null;
		assertNull(arrayToUndo);
		assertNull(cmdProcessor);
	}

}
