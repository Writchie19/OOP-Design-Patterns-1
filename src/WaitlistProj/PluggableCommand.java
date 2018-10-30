package WaitlistProj;

// Expected to be used in conjunction with lambda expressions
public class PluggableCommand
{
	Command doo; // the command to execute
	Command undo; // the command to undo doo (generally is the inverse of doo)
	
	public PluggableCommand(Command doo, Command undo)
	{
		this.doo = doo;
		this.undo = undo;
	}
	
	public void execute()
	{
		doo.execute();
	}
	
	public void undo()
	{
		undo.execute();
	}
}
