package com.revature.App;

import java.util.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.revature.App.Driver;
import com.revature.Objects.*;

public class Driver {
	/** Root logger for the entire application*/
	private static Logger log = Logger.getLogger(Driver.class);
	/** A user of the Audi Dealership Application*/
	static Employee user = new Employee(); // Instantiate a new user
	
	public static void main(String[] args) {
		/** Allows the configuration of log4j from an external file*/
		PropertyConfigurator.configure("log4j.properties");
	}

}
