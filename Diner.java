//Diner object has arrival time and order information for each diner
public class Diner implements Runnable{
	
	public long arr_time;
	public Order o;
	public int table_acquired; //id of table where the diner will be seated
	public int id;
	
	public Diner(long arrival_time, Order o, int diner_id)
	{
		this.arr_time = arrival_time;
		this.o = o;
		this.id = diner_id;
	}
	
	public void printDiner()
	{
		System.out.println("Arrival time : " + arr_time);
		System.out.println("Order :");
		this.o.printOrder();
	}
	
	public void run()
	{
		try 
		{
			//try to get a table. If not available, then wait.
			getTable();
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		//table acquired
		try 
		{
			//order food
			placeOrder();
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		//o.printOrder();
		
		try 
		{
			waitForFood();
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		//food arrived at the table
		
		
		
		try
		{
			//diner takes 30 minutes to finish food
			Thread.sleep(30 * 100);
			//Leave the table in 30 minutes after food has arrived
			exitRestaurant();
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		try 
		{
			isLastDiner();
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void getTable() throws InterruptedException
	{
		synchronized (Restaurant.table_id)
		{
			int id,i; //table id found
			for(i = 0; i < Restaurant.num_of_tables; i++)
			{
				if(Restaurant.table_id[i] == 0)
				{
					this.table_acquired = i + 1;
					Restaurant.table_id[i] = 1;
					long current_time = System.currentTimeMillis()/100;
					long entered_time = current_time - Restaurant.startTime;
					System.out.println("At Time : " + entered_time + " Diner : " + this.id + " seated at table : " + this.table_acquired + "\n");
					break;
				}
				if(i == Restaurant.num_of_tables - 1)
				{
					i = 0;
					Restaurant.table_id.wait();
				}
			}
			
		}			
	}
	
	
	public void placeOrder() throws InterruptedException
	{
		synchronized (Restaurant.order_list)
		{
			Restaurant.order_list.offer(this);
			Restaurant.order_list.notify();
		}
	}
	
	
	public void waitForFood() throws InterruptedException
	{
		synchronized (this)
		{
			this.wait();
		}
	}
	
	
	public void exitRestaurant() throws InterruptedException
	{
		synchronized (Restaurant.diner)
		{
			Restaurant.diner.remove(this);
		}
		synchronized (Restaurant.table_id)
		{
			Restaurant.table_id[this.table_acquired-1] = 0;
			Restaurant.table_id.notify();
		}
		
		
	}
	
	
	public void isLastDiner() throws InterruptedException
	{
		synchronized (Restaurant.num_of_diners_served)
		{
			Restaurant.num_of_diners_served++;
			if(Restaurant.num_of_diners_served.intValue() == Restaurant.num_of_diners)
			{
				long current_time = System.currentTimeMillis()/100;
				long exit_time = current_time - Restaurant.startTime;
				//all diners served, close restaurant
				System.out.println("Last diner left at time : " + exit_time);
				System.exit(0);
			}
		}
	}
}