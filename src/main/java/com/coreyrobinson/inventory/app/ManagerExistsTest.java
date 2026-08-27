package com.coreyrobinson.inventory.app;

import java.sql.SQLException;

import com.coreyrobinson.inventory.dao.UserDAO;

public class ManagerExistsTest {

	public static void main(String[] args) {
		UserDAO userDao = new UserDAO();
		// Test if manager exists
		try {
			boolean managerUser = userDao.managerExists();
			System.out.println("managerExists() returned: " + managerUser);
		} catch(SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
