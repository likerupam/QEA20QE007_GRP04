package com.trip.cost;
//Import functions
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

public class HotelFunctionality 
{
	WebDriver driver;
	Date currentDate;
	ExtentTest Logger;
	ExtentReports reports;
	String[] location = new String[4];
	
	Properties prop;	
	WebDriverWait wait ; 
	
public HotelFunctionality(WebDriver driver, ExtentTest Logger, ExtentReports reports) 	//Accepting driver from main file
{
	this.driver = driver;						//Assigning driver
	this.Logger = Logger;
	this.reports = reports;
}

public void dataread() 							//Reading data from properties file
{
	if (prop == null) 
	{
		prop = new Properties();
		try 
			{
			FileInputStream file = new FileInputStream(System.getProperty("user.dir") + "\\Property\\xpaths.properties"); 	//Property file location
			prop.load(file); 					//Loading property file
			} 
		catch (Exception e)
			{
				e.printStackTrace();			//Printing actual error message
			}
	}
}
	
public void takeSnapShot(WebDriver webdriver, String fileWithPath) throws Exception		//ScreenShot method
{
	 TakesScreenshot scrShot = ((TakesScreenshot)webdriver);							//Convert web driver object to TakeScreenshot
	 File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);							//Call getScreenshotAs method to create image file
	 File DestFile = new File(fileWithPath);											//Move image file to new destination
	 FileUtils.copyFile(SrcFile, DestFile);												//Copy file at destination
}
	
public void openURL() throws Exception													//Directing to the Website
{
	Logger = reports.createTest("Site Verifying Test");
	Logger.assignCategory("Functional Testing");
		
	Logger.log(Status.PASS, MarkupHelper.createLabel("Hotel Functionality initiated", ExtentColor.BLUE));
	Logger.log(Status.INFO,"Started taking screenshots...");
	
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);					//Invoking implicit wait
	driver.get(prop.getProperty("siteLink"));											//Fetching link from properties file
		
	Thread.sleep(1000);
	this.takeSnapShot(driver, System.getProperty("user.dir")+"\\Screenshots\\"+DateTime.getTimeStamp()+".png") ;	//Taking screenshot
	Logger.log(Status.INFO,"TripAdvisor is opened...");
		
		
}
	
	
public void selectCity() throws Exception 													//Passing city name to input field 
{
	Logger = reports.createTest("City Input Test Function");
	Logger.assignCategory("Functional Testing");
		
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);						//Invoking implicit wait
	location = ExcelData.readExcel("Input");												//Fetching value from excel file

	driver.findElement(By.xpath(prop.getProperty("inputField"))).sendKeys(location[1]);		//Passing values to input field
	Logger.log(Status.INFO,"City fetched successfully from excel...");
		
	driver.findElement(By.xpath(prop.getProperty("inputField"))).sendKeys(Keys.ENTER);		//Confirming value by entering it 
	Logger.log(Status.INFO,"City entered successfully...");
		
	Thread.sleep(3000);
	this.takeSnapShot(driver, System.getProperty("user.dir")+"\\Screenshots\\"+DateTime.getTimeStamp()+".png") ;	//Taking screenshot
		
	Logger = reports.createTest("City Verification");
	Logger.assignCategory("Regression Testing");
		
	String s = driver.findElement(By.xpath(prop.getProperty("result"))).getText();
		
							//Invoking implicit wait
	if(location[1].equalsIgnoreCase(s))
		Logger.log(Status.INFO,"Entered city found successfully...");
	else
	{
		Logger.log(Status.FAIL,"No such city found...");
		System.exit(0);
	}
		
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	driver.findElement(By.xpath(prop.getProperty("point"))).click();						//Clicking 1st option from search results
	Logger.log(Status.INFO,"Search result clicked successfully...");
		
		
	for(String winHandle : driver.getWindowHandles())									
	{
		driver.switchTo().window(winHandle);												//Switching control to new window
	}
		
	Logger = reports.createTest("Resultant Window Verification");
	Logger.assignCategory("Sanity Testing");
		
	Logger.log(Status.PASS,"Window handle switched successfully...");
		
	Thread.sleep(1000);
	this.takeSnapShot(driver, System.getProperty("user.dir")+"\\Screenshots\\"+DateTime.getTimeStamp()+".png") ;	//Taking screenshot

	driver.findElement(By.xpath(prop.getProperty("holidayHome"))).click();				//Clicking holiday home option
	Logger.log(Status.INFO,"Holiday Home selected successfully...");
		
	Thread.sleep(2000);
	this.takeSnapShot(driver, System.getProperty("user.dir")+"\\Screenshots\\"+DateTime.getTimeStamp()+".png") ;	//Taking screenshot
	Logger.log(Status.PASS, MarkupHelper.createLabel("City selected Successfully", ExtentColor.BLUE));
		
}
	
public void inDate() throws Exception													//Selecting check in date
{
	Logger = reports.createTest("CheckIn-Date Verification");
	Logger.assignCategory("Sanity Testing");
		
	location = ExcelData.readExcel("Input");											//Fetching value from excel file
		
	if((location[2].equals(location[3])) || (location[2].equals(null) || location[3].equals(null) ))	//Checking input and output days
	{
		Logger.log(Status.FAIL,"Invalid Date Input");
		System.out.println("Invalid Date Input");		//Error message
		System.exit(0);
	}
		
	Thread.sleep(2000);
	//wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("checkIn"))));
	
	driver.findElement(By.xpath(prop.getProperty("checkIn"))).click();					//Clicking on check in date field
	
	Logger.log(Status.INFO,"CheckIn-Date option selected...");
		
	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");				  			//Selecting date format
	Calendar cal = Calendar.getInstance();												//Creating calendar instance
	String currentDate = sdf.format(cal.getTime());										//Fetching current date
		
	if(location[2].equals(currentDate))
	{
		cal.add(Calendar.DAY_OF_MONTH, 1);												//Selecting 1st date from given date
		String newDateIn = sdf.format(cal.getTime());									//Storing date in string

		cal.add(Calendar.DAY_OF_MONTH, 5);												//Selecting 5th date from given date
		String newDateOut = sdf.format(cal.getTime());									//Storing date in string

		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@class=\"_2baLkbS8\"]/descendant::div[contains(@aria-label,'"+ newDateIn +"')]")).click();	//Selecting in-date in calendar
			
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@class='_2baLkbS8']/descendant::div[contains(@aria-label,'"+ newDateOut +"')]")).click();	//Selecting out-date in calendar
	}
		
		
		
	
}
					
public void selectDateCheckIn() throws Exception		   //Selecting Check-In date
{ 
			
			
	location = ExcelData.readExcel("Input");				//Fetching data from excel file
			
	String inDate = location[2];							//Storing check in date
	String splitter[] = inDate.split(" ");					//Splitting date format
	String mon = splitter[1];									//Fetching month from array 
	
	String month = prop.getProperty(mon);						//Fetching month abbreviation from properties file
	String day = splitter[2]; 								//Fetching date from array
	String yr = splitter[5];								//Fetching year from array
	String myr = month+" "+yr;								//Merging month and year

	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);							 //Invoking implicit wait
			
	List<WebElement> elements = driver.findElements(By.xpath(prop.getProperty("monthCatcher"))); //Fetching month details
	Logger.log(Status.INFO,"Months fetched successfully...");
			
	String fetchMonth[] = new String[2];								//Creating array to store months
	try
	{
		fetchMonth = elements.get(0).getText().split(" ");			//Fetching 1st month
	}
	catch(IndexOutOfBoundsException e)
	{
		return;
	}
		
	String c = prop.getProperty(fetchMonth[0]);						//Storing current month
	int currentMonth = Integer.parseInt(c);					//Converting current month from string to integer
		
	String c1 = fetchMonth[1];										//Storing current year
	int currentYear = Integer.parseInt(c1);					//Converting current year from string to integer
			
	String w = prop.getProperty(month);						//Storing last month
	int lastMonth = Integer.parseInt(w);					//Converting last month from string to integer
			
	String w1 = yr;											//Storing last year
	int lastYear = Integer.parseInt(w1);					//Converting last year from string to integer
		
	if(lastYear == currentYear)								//Check if current year is same as last year
	{
		int diff = lastMonth - currentMonth;				//Calculating difference in between months
		if(diff < 0)
		{
			diff = 12 - diff;								//Calculation of number of times to click arrow button
		} 
		for(int i = 0; i < diff; i++)
		{
			driver.findElement(By.xpath(prop.getProperty("arrowRight"))).click();	//Clicking the right arrow to change months
		}
	}
	else if(lastYear > currentYear)							//Check if last year is greater than current year
	{
		int diff = lastMonth - currentMonth;
		if(diff < 0)
		{
			diff = 12 + diff;								//Calculation of number of times to click arrow button
		} 
		for(int i = 0; i < diff; i++)
		{
			driver.findElement(By.xpath(prop.getProperty("arrowRight"))).click();	//Clicking the right arrow to change months

		}
	}
		else
		{
			System.out.println("Invalid dates");					//Error message for invalid date input
			Logger.log(Status.FAIL,"Invalid dates, please enter valid Check In, Check Out Date.");
			System.exit(0);											//Forceful termination of program
		}
			
		boolean staleElement = true;								//Initializing stale element as true
		while(staleElement)											//Enter while true
		{
		try {
			for (int i = 0; i < elements.size(); i++)				//Loop to traverse months
				{
				if(elements.get(i).getText().equals(myr))			//Matching input month with site data
				{ 
							
					List<WebElement> days = driver.findElements(By.xpath(prop.getProperty("dayCatcher")));		//Fetching the days 
					Logger.log(Status.INFO,"Dates fetched successfully...");
					for (WebElement d:days)
					{ 
						if(d.getText().equals(day))					//Matching input day with site data
						{
							d.click();								//Clicking the required day
						}
					} 
				}
						
				}
			}
			catch(StaleElementReferenceException e)					//Catching Stale Element 
			{
					Thread.sleep(1000);
					this.takeSnapShot(driver, System.getProperty("user.dir")+"\\Screenshots\\"+DateTime.getTimeStamp()+".png") ;	//Taking screenshot
					
					Logger.log(Status.INFO,"In Date entered successfully...");
					
					return;											//Returning to main 
				}
			}
			Logger.log(Status.PASS, MarkupHelper.createLabel("CheckIn-Date Verification Successfull", ExtentColor.BLUE));
		}
	public void selectDateCheckOut() throws Exception				//Selecting Check-Out date
	{ 	
		Logger = reports.createTest("CheckOut-Date Verification");
		Logger.assignCategory("Sanity Testing");
			
		String outDate = location[3];								//Storing check out date

		String splitter[] = outDate.split(" ");						//Splitting date format
		String mon1 = splitter[1];									//Fetching month from array
		String month = prop.getProperty(mon1);						//Fetching month abbreviation from properties file 
		String day = splitter[2]; 									//Fetching date from array
		String yr = splitter[5];									//Fetching year from array
		String myr = month+" "+yr;									//Merging month and year
			
		List<WebElement> elements = driver.findElements(By.xpath(prop.getProperty("monthCatcher")));	//Fetching month details
		Logger.log(Status.INFO,"Months fetched successfully...");
			
		String fetchMonth[] = new String[2];									//Creating array to store months
		try
		{
			fetchMonth = elements.get(0).getText().split(" ");				//Fetching 1st month
		}
		catch(IndexOutOfBoundsException e)
		{ 
			return;			
		}
			
		String c = prop.getProperty(fetchMonth[0]);							//Storing current month
		int currentMonth = Integer.parseInt(c);						//Converting current month from string to integer
			
		String w = prop.getProperty(month);							//Storing last month
		int lastMonth = Integer.parseInt(w);						//Converting last month from string to integer
			
		int diff = lastMonth - currentMonth;						//Calculating difference in between months
			
		for(int i = 0; i < diff; i++)
		{
			driver.findElement(By.xpath(prop.getProperty("arrowRight"))).click();		//Clicking the right arrow to change months
		}
			
		boolean staleElement = true;								//Initializing stale element as true
		while(staleElement)											//Enter while true
		{
			try {
				for (int i = 0; i < elements.size(); i++)			//Loop to traverse months
				{
					if(elements.get(i).getText().equals(myr))		//Matching input month with site data
					{ 
						
					List<WebElement> days1 = driver.findElements(By.xpath(prop.getProperty("dayCatcher")));		//Fetching the days 
					Logger.log(Status.INFO,"Dates fetched successfully...");
					for (WebElement d:days1)
					{ 
						if(d.getText().equals(day))					//Matching input day with site data
						{
							d.click(); 								//Clicking the required day
						}
					} 
					   } 

				}
			}
			catch(StaleElementReferenceException e)					//Catching Stale Element 
			{
				Thread.sleep(1000);
				this.takeSnapShot(driver, System.getProperty("user.dir")+"\\Screenshots\\"+DateTime.getTimeStamp()+".png") ;	//Taking screenshot
					
				Logger.log(Status.INFO,"Out Date entered successfully...");
					
				return;
			}
		}	
			Logger.log(Status.PASS, MarkupHelper.createLabel("CheckOut-Date Verification Successfull", ExtentColor.BLUE));
			
		}			
		
	public void selectNoOfPeople() throws Exception 							//Selecting guests
	{
		Logger = reports.createTest("Guest Verification");
		Logger.assignCategory("Regression Testing");
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);		//Invoking implicit wait
		
		location = ExcelData.readExcel("Input");								//Fetching data from excel file
		Logger.log(Status.INFO,"Data fetched sucessfully from excel...");
		String m1 = location[0];												//Storing input value from excel to string
		int m = Integer.parseInt(m1);											//Converting string value to integer
		
		driver.findElement(By.xpath(prop.getProperty("guest"))).click();		//Clicking guest option 
		Logger.log(Status.INFO,"Guested option clicked...");
		
		int defaultValue = 2;													//Default guests in input field
		
		if(m > defaultValue)
		{
			int diff = m - defaultValue;										//Calculating number of times to click (+) option
			for(int i = 0; i < diff; i++)
			{
				driver.findElement(By.xpath(prop.getProperty("plusIcon"))).click(); //Clicking the (+) option
			}
		}
		Logger.log(Status.INFO,"Number of Guested updated...");
		driver.findElement(By.xpath(prop.getProperty("apply"))).click();		//Clicking on apply
		Logger.log(Status.INFO,"Clicked on Apply...");
		Thread.sleep(1000);
		this.takeSnapShot(driver, System.getProperty("user.dir")+"\\Screenshots\\"+DateTime.getTimeStamp()+".png") ;	//Taking screenshot
		
		Logger.log(Status.PASS, MarkupHelper.createLabel("Guest Verification Successfull", ExtentColor.BLUE));
	}
		
	public void selectExtras() throws Exception 								//Selecting extras
	{
		Logger = reports.createTest("Traveller Rating Functionality");
		Logger.assignCategory("Functional Testing");
		
		Thread.sleep(2000);
		driver.findElement(By.xpath(prop.getProperty("options"))).click();		//Clicking on rating option
		Logger.log(Status.INFO,"Sorting Options selected...");
		
		Thread.sleep(1000);
		driver.findElement(By.xpath(prop.getProperty("rating"))).click();		//Clicking traveler rating option
		Logger.log(Status.INFO,"Traveller rating selected...");
		Logger.log(Status.PASS, MarkupHelper.createLabel("Traveller Rating Test Functionality Successfull", ExtentColor.BLUE));
		
		Thread.sleep(1000);
		this.takeSnapShot(driver, System.getProperty("user.dir")+"\\Screenshots\\"+DateTime.getTimeStamp()+".png") ;	//Taking screenshot
		
		Logger = reports.createTest("Elevator Testing Functionality");
		Logger.assignCategory("Functional Testing");
		
		driver.findElement(By.xpath(prop.getProperty("showMore"))).click();		//Clicking show more option in Amenities
		Logger.log(Status.INFO,"Show more in Amenities clicked...");

		driver.findElement(By.xpath(prop.getProperty("elev"))).click();			//Clicking on elevator option
		Logger.log(Status.INFO,"Elevator option selected...");
		
		Thread.sleep(1000);
		this.takeSnapShot(driver, System.getProperty("user.dir")+"\\Screenshots\\"+DateTime.getTimeStamp()+".png") ;	//Taking screenshot
		
		Logger.log(Status.PASS, MarkupHelper.createLabel("Elevator Testing Functionality Successfull", ExtentColor.BLUE));
	}
	
	public void printOutput() throws Exception																					//Transferring data to excel sheet
	{
		Logger = reports.createTest("Output data transfer Functionality");
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);											//Invoking Implicit wait
	
		List<WebElement> titles = driver.findElements(By.xpath(prop.getProperty("listOfHotels")));			//Fetching list of hotels
		Logger.log(Status.INFO,"List of hotels fetched...");
		List<WebElement> price = driver.findElements(By.xpath(prop.getProperty("listOfPricePerNight")));	//Fetching list of price per night
		Logger.log(Status.INFO,"List of price per night fetched...");
		List<WebElement> total = driver.findElements(By.xpath(prop.getProperty("listOfTotalPrice")));		//Fetching list of total price for 5 nights
		Logger.log(Status.INFO,"List of total price fetched...");
		
		String[] name = new String[3];																		//Creating arrays to store lists
		String[] price1=new String[3];																		//Creating arrays to store lists
		String[] totalPrice=new String[3];																	//Creating arrays to store lists
		
		for (int i = 0; i < 3; i++) 
		{
			name[i] = titles.get(i).getText();																//Transferring values to arrays
			price1[i] = price.get(i).getText();																//Transferring values to arrays
			totalPrice[i] = total.get(i).getText();															//Transferring values to arrays
		
		}

	ExcelData.writeExcel(name, price1, totalPrice);													//Passing values to excel sheet
	Logger.log(Status.PASS, MarkupHelper.createLabel("Output data transfer Functionality Successfull", ExtentColor.BLUE));

	}
}