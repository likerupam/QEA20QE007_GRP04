package com.trip.cost;
//Import functions
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties; 
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

public class CruiseFunctionality 
{
	WebDriver driver;
	Properties prop;
	ExtentTest Logger;
	ExtentReports reports;
	
public CruiseFunctionality(WebDriver driver, ExtentTest Logger, ExtentReports reports) //Accepting driver from main file
{
	this.driver = driver;										//Assigning driver
	this.Logger = Logger;										//Assigning print 
	this.reports = reports;
}
	
public void dataread() 											// Reading data from properties file
{
	if (prop == null) 
	{
		prop = new Properties();
		try 
			{
			FileInputStream file = new FileInputStream(System.getProperty("user.dir")+"\\Property\\xpaths.properties"); //Property location
			prop.load(file); 									//Loading property file
				
			} 
		catch (Exception e)
			{
				e.printStackTrace();							//Printing actual error message
			}
	}
}
	
public void takeSnapShot(WebDriver webdriver, String fileWithPath) throws Exception			//ScreenShot method
{
	TakesScreenshot scrShot =((TakesScreenshot)webdriver);			//Convert web driver object to TakeScreenshot
	File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);			//Call getScreenshotAs method to create image file
	File DestFile=new File(fileWithPath);							//Move image file to new destination
	FileUtils.copyFile(SrcFile, DestFile);							//Copy file at destination
}
	
public void getCruiseDetails() throws Exception 								//Selecting cruise 
{
	Logger = reports.createTest("Cruise Option Verification");
	Logger.assignCategory("Functional Testing");
		
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);			//Invoking implicit wait
		
	driver.findElement(By.xpath(prop.getProperty("cruiseOption"))).click();		//Clicking cruise option
	Logger.log(Status.INFO,"Selected cruise option successfully...");
		
	Thread.sleep(1000);
	this.takeSnapShot(driver, System.getProperty("user.dir")+"\\Screenshots\\"+DateTime.getTimeStamp()+".png") ;	//Taking screenshot
		
	Logger = reports.createTest("Cruise Type Selection Functionality");
	Logger.assignCategory("Functional Testing");
		
	driver.findElement(By.xpath(prop.getProperty("dropMenu1"))).click();		//Clicking on 1st drop down menu
	Logger.log(Status.INFO,"Clicked on 1st Drop down menu...");
		
	driver.findElement(By.xpath(prop.getProperty("option1"))).click();			//Selecting cruise from drop down menu
	Logger.log(Status.INFO,"Selected cruise...");
		
	Thread.sleep(1000);
	this.takeSnapShot(driver, System.getProperty("user.dir")+"\\Screenshots\\"+DateTime.getTimeStamp()+".png") ;	//Taking screenshot

	Logger = reports.createTest("Cruise Type Variant Functionality");
	Logger.assignCategory("Functional Testing");
		
	driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);			//Invoking implicit wait
	driver.findElement(By.xpath(prop.getProperty("dropMenu2"))).click();		//Clicking on 2nd drop down menu
	Logger.log(Status.INFO,"Clicked on 2nd Drop down menu...");

	driver.findElement(By.xpath(prop.getProperty("option2"))).click();			//Selecting cruise from drop down menu
	Logger.log(Status.INFO,"Selected cruise...");
		
	Thread.sleep(1000);
	this.takeSnapShot(driver, System.getProperty("user.dir")+"\\Screenshots\\"+DateTime.getTimeStamp()+".png") ;	//Taking screenshot
		
	Logger = reports.createTest("Search Button Functionality");
	Logger.assignCategory("Regression Testing");
		
	Thread.sleep(500);
	driver.findElement(By.xpath(prop.getProperty("search"))).click();			//Confirming values and applying it
	Logger.log(Status.INFO,"Clicked on search button...");	
		
	Logger.log(Status.PASS, MarkupHelper.createLabel("Cruise selection Functionality Successfull", ExtentColor.BLUE));
		
	}
	
public void getDetails() throws Exception 										//Fetching cruise details
{	
	Logger = reports.createTest("Output data transfer Functionality");
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);			//Invoking implicit wait			
		
	for(String winHandle : driver.getWindowHandles())
	{
		driver.switchTo().window(winHandle);													//Switching control to new window
	}		

	String passenger = driver.findElement(By.xpath(prop.getProperty("details1"))).getText();	//Storing passenger details
	String[] list = passenger.split("\\|   ",2);  												//Separating passenger details
	Logger.log(Status.INFO,"Fetched passenger details...");
		
	String launched = driver.findElement(By.xpath(prop.getProperty("details2"))).getText();		//Storing launch details
	Logger.log(Status.INFO,"Fetched launch details...");
	
	String lang[] = new String[3];																
	for(int i = 0; i < 3; i++)
	{
		lang[i] = driver.findElement(By.xpath("//div[@class=\"ui_column  is-4-tablet is-shown-at-tablet \"]/ul/li["+(i+2)+"]/label/span")).getText();	//Storing language details
	}
		
	Thread.sleep(1000);
	this.takeSnapShot(driver, System.getProperty("user.dir")+"\\Screenshots\\"+DateTime.getTimeStamp()+".png") ;	//Taking screenshot
	ExcelData.writeExcelCruise(list,launched,lang);								//Passing fetched data to excel
	Logger.log(Status.PASS, MarkupHelper.createLabel("Output data transfer Functionality Successfull", ExtentColor.BLUE));
	}
}
