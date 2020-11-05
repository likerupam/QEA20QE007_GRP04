package com.trip.cost;

//Import functions
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
//import java.util.Scanner;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;


public class MainTest 
{
	ExtentReports reports;
	ExtentHtmlReporter htmlReporter;
	ExtentTest Logger;

	WebDriver driver;
	int c = 0;
	//static Scanner sc = new Scanner(System.in);
	int option;
	
	@BeforeTest
	public void startTest()											//Starting extent report
	{
		//System.out.println("Available Browers :\n1. Chrome\n2. Firefox\n\nSelect Option :");	//Suggesting user to choose a browser
		//option = sc.nextInt();										//User input stored 
		
		reports = new ExtentReports();								//Creating extent report object
		
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir")+"//test-output//"+DateTime.getTimeStamp()+".html");	//Fetching file from directory
		reports.attachReporter(htmlReporter);						//attaching report object to existing html file
		
		// Set the system info of the report
		reports.setSystemInfo("OS","Windows 10");
		reports.setSystemInfo("Environment","UAT");
		reports.setSystemInfo("Build number","10.8.1");
		reports.setSystemInfo("Browser","Firefox and Chrome");
		
		// Set the htmlReporter properties
		htmlReporter.config().setDocumentTitle("Calculate Trip Cost");
		htmlReporter.config().setReportName("Automation Test Report");
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
		
	}
	
	@Test
	public void controller() throws Exception
	{
		//if(option == 1)							//Opting for chrome
			chromeTest();						
		//else if(option == 2)					//Opting for firefox
			//fireFoxTest();						
	}

	public void hotelFunc() throws Exception
	{
		HotelFunctionality func = new HotelFunctionality(driver, Logger, reports);		//Passing driver to class and logger
		
		System.out.println("HotelFunctionality initiated");
		
		func.dataread();													//Calling properties file
		System.out.println("dataread initiated");
		func.openURL();													//Opening site
		func.selectCity();													//Providing city in input field	
		func.inDate();														//Clicking check in date and entering current date	
		func.selectDateCheckIn();										//Providing manual in-date	
		func.selectDateCheckOut();									//Providing manual out-date 
		func.selectNoOfPeople();										//Selecting number of guests	
		func.selectExtras();												//Selecting filters for search
		func.printOutput();												//Printing output in excel
	
		System.out.println("HotelFunctionality terminated");
	}
	
	public void cruiseFunc() throws Exception
	{
		CruiseFunctionality func1 = new CruiseFunctionality(driver, Logger, reports);	//Passing driver to class
		
		System.out.println("CruiseFunctionality initiated");	
		
		func1.dataread();												//Calling properties file	
		func1.getCruiseDetails();										//Selecting desired cruise 
		func1.getDetails();												//Fetching cruise details
	
		System.out.println("CruiseFunctionality terminated");	
	}
	
	public void chromeTest() throws Exception
	{		
		driver = DriverSetup.getChromeDriver(reports, Logger);
		hotelFunc();
		cruiseFunc();
	}
	
	public void fireFoxTest() throws Exception			
	{		
		driver = DriverSetup.getFireFoxDriver(reports, Logger);
		
		hotelFunc();
		cruiseFunc();
	}
	
	@AfterTest
	public void browserQuit()											//Quit process
	{
		driver.quit();
		//Logger.log(Status.INFO,MarkupHelper.createLabel("Automated windows closed successfully.", ExtentColor.GREEN));	//Confirmation message
		reports.flush();												//Closing extent report
	}
	
	
}

