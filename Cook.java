//cook thread is active throughout the simulation
public class Cook implements Runnable{
	
	public int id;
	public Diner help_d; //help_d will be the diner whose order will be prepared by this cook
	public Cook(int cook_id)
	{
		this.id = cook_id;
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
		}
	}
	
	public void prepareFood() throws InterruptedException
	{
		while(this.help_d.o.burgers > 0 || this.help_d.o.fries > 0 || this.help_d.o.drinks > 0 || this.help_d.o.desserts > 0)
		{
			if(this.help_d.o.burgers > 0)
			{
				//try to acquire burger machine
				synchronized(Restaurant.burgers_machine)
				{
					if(Restaurant.burgers_machine.intValue() == 0)
					{
						//burger machine is free, use the machine to prepare a burger (takes 5 minutes)
						Restaurant.burgers_machine = 1;	
						long current_time = System.currentTimeMillis()/100;
						long start_time = current_time - Restaurant.startTime;
						System.out.println("At Time : " + start_time + " Burger machine for diner : " + this.help_d.id + " was used\n\n" );
						//System.out.println("Preparing Burger");
						//use the machine for 5 minutes
						//Thread.sleep(5 * 100);
						Thread.sleep(5 * 100 * this.help_d.o.burgers );
						//this.help_d.o.burgers--;
						this.help_d.o.burgers = 0;
						Restaurant.burgers_machine = 0;
					}
				}
				
			}
			
			if(this.help_d.o.fries > 0)
			{
				//try to acquire burger machine
				synchronized(Restaurant.fries_machine)
				{
					if(Restaurant.fries_machine.intValue() == 0)
					{
						//burger machine is free, use the machine to prepare a burger (takes 3 minutes)
						Restaurant.fries_machine = 1;
						long current_time = System.currentTimeMillis()/100;
						long start_time = current_time - Restaurant.startTime;
						System.out.println("At Time : " + start_time + " Fries machine for diner : " + this.help_d.id + " was used\n" );						//Thread.sleep(3 * 100);
						Thread.sleep(3 * 100 * this.help_d.o.fries);
						//this.help_d.o.fries--;
						this.help_d.o.fries = 0;
						Restaurant.fries_machine = 0;
					}
				}
				
			}
			
			if(this.help_d.o.drinks > 0)
			{
				//try to acquire burger machine
				synchronized(Restaurant.drinks_machine)
				{
					if(Restaurant.drinks_machine.intValue() == 0)
					{
						//burger machine is free, use the machine to prepare a burger (takes 2 minutes)
						Restaurant.drinks_machine = 1;	
						long current_time = System.currentTimeMillis()/100;
						long start_time = current_time - Restaurant.startTime;
						System.out.println("At Time : " + start_time + " Drinks machine for diner : " + this.help_d.id + " was used\n" );						//Thread.sleep(2 * 100);
						Thread.sleep(2 * 100 * this.help_d.o.drinks);
						//this.help_d.o.drinks--;
						this.help_d.o.drinks = 0;
						Restaurant.drinks_machine = 0;
					}
				}
				
			}
			
			
			if(this.help_d.o.desserts > 0)
			{
				//try to acquire burger machine
				synchronized(Restaurant.desserts_machine)
				{
					if(Restaurant.desserts_machine.intValue() == 0)
					{
						//burger machine is free, use the machine to prepare a burger (takes 1 minutes)
						Restaurant.desserts_machine = 1;	
						long current_time = System.currentTimeMillis()/100;
						long start_time = current_time - Restaurant.startTime;
						System.out.println("At Time : " + start_time + " Dessert machine for diner : " + this.help_d.id + " was used\n" );						//Thread.sleep(1 * 100);
						Thread.sleep(1 * 100 * this.help_d.o.desserts);
						//this.help_d.o.desserts--;
						this.help_d.o.desserts = 0;
						Restaurant.desserts_machine = 0;
					}
				}							
			}
		}
		long current_time = System.currentTimeMillis()/100;
		long end_time = current_time - Restaurant.startTime;
		System.out.println("At time : " + end_time + " order for diner : " + this.help_d.id + " completed\n");
		//food ready.
		synchronized(help_d)
		{
			help_d.notify();
		}
		
	}
}