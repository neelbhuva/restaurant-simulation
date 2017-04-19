import java.io.*;

public class Machine
{
	public boolean burgers_machine;
	public boolean fries_machine;
	public boolean drinks_machine;
	public boolean desserts_machine;
	
	public Machine()
	{
		burgers_machine = true;
		fries_machine = true;
		drinks_machine = true;
		desserts_machine = true;
	}
	
	public boolean burgers_mach_available()
	{
		boolean b;
		synchronized(this)
		{
			b = burgers_machine;
		}
		return b;
	}
	
	public boolean fries_mach_available()
	{
		boolean b;
		synchronized(this)
		{
			b = fries_machine;
		}
		return b;
	}
	
	public boolean drinks_mach_available()
	{
		boolean b;
		synchronized(this)
		{
			b = drinks_machine;
		}
		return b;
	}
	
	public boolean desserts_mach_available()
	{
		boolean b;
		synchronized(this)
		{
			b = desserts_machine;
		}
		return b;
	}
	
	public void set_burgers_mach_available(boolean b)
	{
			burgers_machine = b;
	}
	
	public void set_drinks_mach_available(boolean b)
	{
			drinks_machine = b;
	}
	
	public void set_fries_mach_available(boolean b)
	{
			fries_machine = b;
	}
	
	public void set_desserts_mach_available(boolean b)
	{
			desserts_machine = b;
	}
	
	
}