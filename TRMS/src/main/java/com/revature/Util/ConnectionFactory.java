package com.revature.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class ConnectionFactory {
	private static Logger log = Logger.getRootLogger();
	/** The hostname of the Amazon RDS database instance.*/
	private static String url;
	/** The username that was configured for the Amazon RDS database*/
	private static String username;
	/** The password that was configured for the Amazon RDS database.*/
	private static String password;
	/** The Amazon RDS database name*/
	private static String dbName;
	/** The port on which the Amazon RDS database instance accepts connections.*/
	private static String port;
	/** A ConnectionFacotry Singleton object */
	private static ConnectionFactory cf;
	
	/**
	 * Instantiates all of the System Environment variables necessary to establish
	 * a connection with the AWS RDS database.
	 */
	private ConnectionFactory() {
		
		dbName = System.getenv("RDS_DBNAME"); 
		port = System.getenv("RDS_PORT");
		url = System.getenv("RDS_HOSTNAME");
		url = "jdbc:postgresql://" + url + ":"+ port +"/" + dbName + "?";
		username = System.getenv("RDS_USERNAME");
		password = System.getenv("RDS_PASSWORD");
		
	}
	
	/**
	 * Creates a connection to a AWS RDS PostgreSQL database and 
	 * returns a Connection object
	 * 
	 * @return a Connection object that represents a connection to a PostgreSQL database
	 */
	private Connection createConnection() {
		Connection conn = null;

		try {
			/** Loads JDBC Driver*/
			Class.forName("org.postgresql.Driver");
			
			log.info("createConnection: Loaded Driver");
		} catch (ClassNotFoundException e) {
			
			log.fatal(e.getMessage());
			System.exit(1);
		}

		try {
			/** A Connection that is created by establishing a connection with a PostgreSQL database*/
			conn = DriverManager.getConnection(url,username, password);
			
			log.info("createConnection: Connected to database");
		} catch (SQLException e) {
			
			log.fatal(e.getMessage());
			System.exit(1);
		}

		return conn;

	}
	
	/**
	 * Closes an established connection to a AWS RDS PostgreSQL database
	 * 
	 * @param conn a Connection object that represents a connection to a PostgreSQL database
	 */
	public static void closeConnection(Connection conn) {
	    try {
	      conn.close();
	      cf = null;
	      
	      log.info("closeConnection: Connection closed.");
	    } catch (SQLException e) {
	    	
	      log.error("closeConnection: Can't close connection.");
	      System.exit(1);
	    }
	  }
	
	/**
	 * Gets a Connection object that represents an established connection to 
	 * an AWS RDS PostgreSQL database and returns it 
	 * 
	 * @return  a Connection object that represents an established connection to 
	 * an AWS RDS PostgreSQL database
	 */
	public static Connection getConnection() {

		if (cf == null) {
			cf = new ConnectionFactory();
		}

		return cf.createConnection();
	}
	
}
