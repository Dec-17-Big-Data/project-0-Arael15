package com.revature.bank.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.logging.log4j.*;


public class ConnectionUtil {
	
	private static Connection connectionInstance = null;
	private static final Logger log = LogManager.getLogger(ConnectionUtil.class);
	
	private ConnectionUtil() {
		
	}
	
	public static Connection getConnection() {
		if (ConnectionUtil.connectionInstance != null) {
			return ConnectionUtil.connectionInstance;
		}
		
		InputStream in = null;
		
		log.debug("Attempting to connect to database");
		try {
			// Load in properties file
			Properties props = new Properties();
			in = new FileInputStream("src\\main\\resources\\connection.properties");
			props.load(in);
			
			//Get the connection object
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = null;
			
			String endpoint = props.getProperty("jdbc.url");
			String username = props.getProperty("jdbc.username");
			String password = props.getProperty("jdbc.password");
			
			connectionInstance = DriverManager.getConnection(endpoint, username, password);
			
			log.debug("Database connection established");
			return log.traceExit(connectionInstance);
		}
		catch(Exception e) {
			log.error("Unable to establish connection to database");
		}
		finally {
			try {
				in.close();
			}
			catch (IOException e) {
				
			}
		}
		
		return null;
	}
}

