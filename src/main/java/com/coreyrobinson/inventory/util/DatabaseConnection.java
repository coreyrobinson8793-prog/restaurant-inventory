/*
 *AUTHOR: Corey Robinson
 *PURPOSE: This will read "database.properties", load the driver, and provide a method that returns a database connection object whenever any other class needs one.
 *DATE: 05/02/2026
 */
package com.coreyrobinson.inventory.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
	
	private static String URL;
	private static String USER;
	private static String PASSWORD;
	private static String DRIVER;
	
	private DatabaseConnection() {
		
	}
	
	static {
	    Properties props = new Properties();
	    try {
	        File external = new File("database.properties");
	        if (external.exists()) {
	            try (InputStream input = new FileInputStream(external)) {
	                props.load(input);
	            }
	        } else {
	            try (InputStream input = DatabaseConnection.class.getClassLoader()
	                    .getResourceAsStream("database.properties")) {
	                if (input == null) {
	                    throw new RuntimeException("database.properties not found. "
	                            + "Place it next to the application executable.");
	                }
	                props.load(input);
	            }
	        }
	        URL = props.getProperty("db.url");
	        USER = props.getProperty("db.user");
	        PASSWORD = props.getProperty("db.password");
	        DRIVER = props.getProperty("db.driver");
	        Class.forName(DRIVER);
	    } catch (IOException | ClassNotFoundException e) {
	        throw new RuntimeException("Failed to initialize database connection", e);
	    }
	}
	
	/**
	 * Connects to the database.
	 * @return	Connection to the database.
	 * @throws SQLException	
	 */
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
	
	
}
