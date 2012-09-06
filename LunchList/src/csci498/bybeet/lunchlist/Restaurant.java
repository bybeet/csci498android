package csci498.bybeet.lunchlist;

import java.util.Calendar;

public class Restaurant {
	private String name = "";
	private String address = "";
	private String type="";
	private Calendar date = Calendar.getInstance();
	
	public void setDate ( int year, int month, int day )
	{
		date.set(year, month , day);
	}
	
	public Calendar getDate ()
	{
		return date;
	}
	
	public String getName ()
	{
		return name;
	}
	
	public void setName (String name)
	{
		this.name = name;
	}
	
	public String getAddress ()
	{
		return address;
	}
	
	public void setAddress (String address)
	{
		this.address = address;
	}
	
	public String getType()
	{
		return(type);
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public String toString()
	{
		return(getName());
	}
}
