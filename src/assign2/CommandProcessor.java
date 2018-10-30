package assign2;

import java.util.Stack;

public class CommandProcessor 
{
	Stack<PluggableCommand> commandStack; //underlying datastructure to hold commands
	
	public CommandProcessor()
	{
		commandStack = new Stack<PluggableCommand>();
	}
	
	public void doIt(PluggableCommand doIt)
	{
		commandStack.push(doIt);
		doIt.execute();
	}
	
	public void undoIt()
	{
		commandStack.pop().undo();
	}
}
