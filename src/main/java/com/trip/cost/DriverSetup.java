package com.trip.cost;
//Import functions
import java.util.Scanner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;


public class DriverSetup
{

	static WebDriver driver;
	static Scanner sc = new Scanner(System.in);
	ExtentReports reports;
	ExtentTest Logger;

	public static WebDriver getChromeDriver(ExtentReports reports, ExtentTest Logger) 
	{
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\drivers\\chromedriver.exe");	//Fetching driver files
		ChromeOptions options = new ChromeOptions();			//Getting access to chrome options
		options.addArguments("--disable-notifications");		//Disabling notifications
		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		
		Logger = reports.createTest("Chrome Initiated Successfully");
		Logger.log(Status.PASS,"Chrome Initiated");
		
		return driver;
	}
	public static WebDriver getFireFoxDriver(ExtentReports reports, ExtentTest Logger) 
	{
		System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\drivers\\geckodriver.exe");	//Fetching driver files
		FirefoxOptions optionsFire = new FirefoxOptions();		//Getting access to firefox options
		optionsFire.addArguments("--disable-notifications");	//Disabling notifications
		driver = new FirefoxDriver(optionsFire);
		driver.manage().window().maximize();					//Maximizing window
		
		Logger = reports.createTest("FireFox Initiated Successfully");
		Logger.log(Status.PASS,"FireFox Initiated");

		return driver;											//Returning driver
	}
}
