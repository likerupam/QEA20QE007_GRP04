package com.trip.cost;

import java.util.*;

public class DateTime 
{
	
	public static String getTimeStamp() 
	{
		Date date = new Date();
		return date.toString().replaceAll(":","_").replaceAll(" ","_");
	}
}
