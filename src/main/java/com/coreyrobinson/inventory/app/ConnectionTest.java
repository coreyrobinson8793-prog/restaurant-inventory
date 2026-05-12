/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Tests the connection to the database
 * DATE: 05/02/2026
 */
package com.coreyrobinson.inventory.app;

import java.sql.Connection;
import java.sql.SQLException;

import com.coreyrobinson.inventory.util.DatabaseConnection;

public class ConnectionTest {

	public static void main(String[] args) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			System.out.println("Connection Successful");
			System.out.println("Connected to: " + conn.getCatalog());
		} catch (SQLException e) {
			System.out.println("Connection failed:");
			e.printStackTrace();
		}
	}
}
