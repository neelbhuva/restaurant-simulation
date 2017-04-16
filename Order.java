import java.util.Arrays;

//class Order represents each order by a diner.
public class Order {
	
	public int burgers, fries, drinks, desserts, num_of_items;
	
	public Order(int num_of_burgers, int num_of_fries, int num_of_drinks, int num_of_desserts)
	{
		this.burgers = num_of_burgers;
		this.fries = num_of_fries;
		this.drinks = num_of_drinks;
		this.desserts = num_of_desserts;
		this.num_of_items = 4;
	}
	
	public void printOrder()
	{
		int temp[] = new int[num_of_items];
		temp[0] = this.burgers;
		temp[1] = this.fries;
		temp[2] = this.drinks;
		temp[3] = this.desserts;
		System.out.println(Arrays.toString(temp));
	}
}