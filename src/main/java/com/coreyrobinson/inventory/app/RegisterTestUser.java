/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Tests the log in screen.
 * DATE: 05/10/2026
 */
package com.coreyrobinson.inventory.app;

import java.sql.SQLException;

import com.coreyrobinson.inventory.model.User;
import com.coreyrobinson.inventory.service.UserService;

public class RegisterTestUser {

	public static void main(String[] args) {
		
		UserService userService = new UserService();
		
		try {
			User regTestUser = userService.registerUser("corey", "Password123", "Manager");
			System.out.println("Registered: " + regTestUser.getUsername() + " (ID " + regTestUser.getUserId() + ")");
		} catch (SQLException | IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
		// TODO Auto-generated method stub

	}

}
