//cook thread is active throughout the simulation
public class Cook implements Runnable{
	
	public int id;
	public Diner help_d;//help_d will be the diner whose order will be prepared by this cook
	public Machine mac;
	public Cook(int cook_id, Machine mac)
	{
		this.id = cook_id;
		this.mac = mac;
	}
	
	public void run()
	{
		while(true)
		{
			try
			{
				//recieve order from order_list
				getOrder();
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			
			System.out.println("Cook : " + this.id + " now cooking for diner : " + this.help_d.id + "\n");
			//order received
			
			try
			{
				//start preparing food as when machines are available
				prepareFood();
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			
			//food ready, serve it to the diner
		}
		
	}
	
	public void getOrder() throws InterruptedException
	{
		synchronized (Restaurant.order_list)
		{
			if(Restaurant.order_list.isEmpty())
			{
				Restaurant.order_list.wait();
			}
			this.help_d = Restaurant.order_list.remove();
			//System.out.println("Received order for : " + this.help_d.id);
		}
	}
	
	public void prepareFood() throws InterruptedException
	{
		boolean flag = false;
		while(this.help_d.o.burgers > 0 || this.help_d.o.fries > 0 || this.help_d.o.drinks > 0 || this.help_d.o.desserts > 0)
		{
			if(this.help_d.o.burgers > 0)
			{
				if(mac.burgers_mach_available())
				{
				//try to acquire burger machine
				synchronized(mac)
				{
					if(mac.burgers_mach_available())
					{
						flag = true;
						mac.set_burgers_mach_available(false);
						//burger machine is free, use the machine to prepare a burger (takes 5 minutes)
						//Restaurant.burgers_machine = 1;	
						long current_time = System.currentTimeMillis()/Restaurant.timeunit;
						long start_time = current_time - Restaurant.startTime;
						System.out.println("At Time : " + start_time + " Burger machine for diner : " + this.help_d.id + " was used\n\n" );
						//System.out.println("Preparing Burger");
						//Restaurant.burgers_machine = 0;
					}
				}
				if(flag == true)
				{
					//use the machine for 5 minutes
					//Thread.sleep(5 * Restaurant.timeunit);
					Thread.sleep(5 * Restaurant.timeunit * this.help_d.o.burgers );
					//this.help_d.o.burgers--;
					this.help_d.o.burgers = 0;
					synchronized(mac)
					{
						mac.set_burgers_mach_available(true);
					}
					flag = false;
				}
				}
				
			}
			
			if(this.help_d.o.fries > 0)
			{
				if(mac.fries_mach_available())
				{
				//try to acquire burger machine
				synchronized(mac)
				{
					if(mac.fries_mach_available())
					{
						flag = true;
						mac.set_fries_mach_available(false);
						//burger machine is free, use the machine to prepare a burger (takes 3 minutes)
						//Restaurant.fries_machine = 1;
						long current_time = System.currentTimeMillis()/Restaurant.timeunit;
						long start_time = current_time - Restaurant.startTime;
						System.out.println("At Time : " + start_time + " Fries machine for diner : " + this.help_d.id + " was used\n" );						//Thread.sleep(3 * Restaurant.timeunit);
						
						//Restaurant.fries_machine = 0;
					}
				}
				if(flag == true)
				{
					Thread.sleep(3 * Restaurant.timeunit * this.help_d.o.fries);
					//this.help_d.o.fries--;
					this.help_d.o.fries = 0;
					synchronized(mac)
					{
						mac.set_fries_mach_available(true);
					}
					flag = false;
				}
				}
			}
			
			if(this.help_d.o.drinks > 0)
			{
				if(mac.drinks_mach_available())
				{
				//try to acquire burger machine
				synchronized(Restaurant.drinks_machine)
				{
					if(mac.drinks_mach_available())
					{
						flag = true;
						mac.set_drinks_mach_available(false);
						//burger machine is free, use the machine to prepare a burger (takes 2 minutes)
						//Restaurant.drinks_machine = 1;	
						long current_time = System.currentTimeMillis()/Restaurant.timeunit;
						long start_time = current_time - Restaurant.startTime;
						System.out.println("At Time : " + start_time + " Drinks machine for diner : " + this.help_d.id + " was used\n" );						//Thread.sleep(2 * Restaurant.timeunit);
						
						//Restaurant.drinks_machine = 0;
					}
				}
				if(flag == true)
				{
					Thread.sleep(2 * Restaurant.timeunit * this.help_d.o.drinks);
					//this.help_d.o.drinks--;
					this.help_d.o.drinks = 0;
					synchronized(mac)
					{
						mac.set_drinks_mach_available(true);
					}
					flag = false;
				}
				}
			}
			
			
			if(this.help_d.o.desserts > 0)
			{
				if(mac.desserts_mach_available())
				{
				//try to acquire burger machine
				synchronized(Restaurant.desserts_machine)
				{
					if(mac.desserts_mach_available())
					{
						flag = true;
						mac.set_desserts_mach_available(false);
						//burger machine is free, use the machine to prepare a burger (takes 1 minutes)
						//Restaurant.desserts_machine = 1;	
						long current_time = System.currentTimeMillis()/Restaurant.timeunit;
						long start_time = current_time - Restaurant.startTime;
						System.out.println("At Time : " + start_time + " Dessert machine for diner : " + this.help_d.id + " was used\n" );						//Thread.sleep(1 * Restaurant.timeunit);
						Thread.sleep(1 * Restaurant.timeunit * this.help_d.o.desserts);
						//this.help_d.o.desserts--;
						this.help_d.o.desserts = 0;
						//Restaurant.desserts_machine = 0;
					}
				}
				if(flag == true)
				{
					Thread.sleep(1 * Restaurant.timeunit * this.help_d.o.desserts);
					//this.help_d.o.desserts--;
					this.help_d.o.desserts = 0;
					synchronized(mac)
					{
						mac.set_desserts_mach_available(true);
					}
					flag = false;
				}
				}
			}
		}
		long current_time = System.currentTimeMillis()/Restaurant.timeunit;
		long end_time = current_time - Restaurant.startTime;
		System.out.println("At time : " + end_time + " order for diner : " + this.help_d.id + " completed\n");
		//food ready.
		synchronized(help_d)
		{
			help_d.notify();
		}
		
	}
}