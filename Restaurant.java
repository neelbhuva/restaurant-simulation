import java.io.*;
import java.util.Arrays;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.lang.Thread;
import java.util.Queue;
import java.util.LinkedList;

public class Restaurant
{
	public static long startTime;
	public static Integer num_of_diners_served;
	public static int table_id[];
	public static int num_of_diners, num_of_tables, num_of_cooks;
	public static PriorityQueue<Diner> diner; 
	public static Queue<Diner> order_list;
	public static Integer burgers_machine, fries_machine, drinks_machine, desserts_machine; 
	//main is called by JVM before any object is made. Hence main is static, as it can be directly invoked via class.
	public static void main(String [] args) throws IOException, NullPointerException, InterruptedException
	{
		//input file name is passed as argument
		String input_file = args[0];
		//FileReader reads directly from the file, which is costly if you want to make multiple read requests
		FileReader iReader = new FileReader(input_file);
		//BufferedReader buffers the contents of input file. Default buffer length is large enough. 
		BufferedReader reader = new BufferedReader(iReader);
		
		//get the number of diners, tables and cooks from the input file
		num_of_diners = Integer.parseInt(reader.readLine().trim());
		num_of_tables =Integer.parseInt(reader.readLine().trim());
		num_of_cooks = Integer.parseInt(reader.readLine().trim());
		
		num_of_diners_served = 0;
		
		//assign ids from 1 to num_of_tables to tables
		table_id = new int[num_of_tables];
		for(int i = 0; i < num_of_tables; i++)
		{
			table_id[i] = 0; //0 means table is unoccupied
		}
		
		diner = new PriorityQueue<Diner>(num_of_diners, new Comparator<Diner>(){
			public int compare(Diner d1, Diner d2) {                         
				if(d1.arr_time == d2.arr_time)
					return 0;
				else if(d1.arr_time > d2.arr_time)
					return 1;
				else
					return -1;
            }      
		});
		
		order_list = new LinkedList<Diner>();
		//0 means machine is not in use
		burgers_machine = new Integer(0);
		fries_machine = new Integer(0);
		drinks_machine = new Integer(0);
		desserts_machine = new Integer(0);
		//get current time in minutes
		
		//System.out.println(startTime);
		
		//get the arrivals of diners. 
		//(time of arrival(minutes),number of burgers,fries,drinks,desserts)
		String[] arrival_info = new String[5];
		long arrival_time;
		int num_of_burgers, num_of_fries, num_of_drinks, num_of_desserts;
		for(int i = 0; i < num_of_diners; i++)
		{
			arrival_info = reader.readLine().split("\\s+");
			//System.out.println(Arrays.toString(arrival_info));
			//covert the details(string) to integer values
			arrival_time = Long.parseLong(arrival_info[0]);
			num_of_burgers = Integer.parseInt(arrival_info[1]);
			num_of_fries = Integer.parseInt(arrival_info[2]);
			num_of_drinks = Integer.parseInt(arrival_info[3]);
			num_of_desserts = Integer.parseInt(arrival_info[4]);
			Order o = new Order(num_of_burgers, num_of_fries, num_of_drinks, num_of_desserts);
			//o.printOrder();
			Diner d = new Diner(arrival_time,o,i+1);
			diner.offer(d);
			//d.printDiner();
		}
		
		startTime = System.currentTimeMillis()/100;
		
		//active all cook threads
		for(int i = 0; i < num_of_cooks; i++)
		{
			Cook c = new Cook(i+1);
			Thread cT = new Thread(c);
			cT.start();
		}
		
		long prev_arrival = 0; //arrival time of previous diner
		long wait_time; //time current diner waits.
		
		for(int i = 0; i < num_of_diners; i++)
		{
			Diner d = diner.poll();//get the diner from priority queue
			//d.printDiner();
			wait_time = d.arr_time - prev_arrival; //in minutes
			Thread dT = new Thread(d);
			Thread.sleep(wait_time * 100);
			dT.start();
			prev_arrival = d.arr_time;
			
		}
		reader.close();
		iReader.close();
	}
}