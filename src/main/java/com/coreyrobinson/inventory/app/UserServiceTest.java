/*
 * AUTHROR: Corey Robinson
 * PURPOSE: Tests the user service class.
 * DATE: 05/09/2026
 */
package com.coreyrobinson.inventory.app;

import java.sql.SQLException;
import java.util.Optional;

import com.coreyrobinson.inventory.model.User;
import com.coreyrobinson.inventory.service.UserService;

public class UserServiceTest {

	public static void main(String[] args) {
		
		UserService userService = new UserService();
		String testUsername = "login_test_" + System.currentTimeMillis();
		String correctPass = "TestPass123";
		String wrongPass = "WrongPass123";
		
		try {
			// Register
			User registered = userService.registerUser(testUsername, correctPass, "Manager");
			System.out.println("Registered User ID: " + registered.getUserId());
			// Authenticate with wrong password
			Optional<User> wrongAttempt = userService.authenticate(testUsername, wrongPass);
			System.out.println("Incorrect Password: " + (wrongAttempt.isPresent() ? "Logged In (BUG)" : "Rejected (correct)"));
			// Authenticate with correct password
			Optional<User> correctAttempt = userService.authenticate(testUsername, correctPass);
			System.out.println("Correct Password: " + (correctAttempt.isPresent() ? "Logged In (correct)" : "Rejected (BUG)"));
			// Force lockout with 5 wrong attempts.
			for (int i = 1; i <= 5; i++) {
				userService.authenticate(testUsername, wrongPass);
				System.out.println("Failed attempt #" + i);
			}
			// Try correct password while locked out.
			Optional<User> lockedOut = userService.authenticate(testUsername, correctPass);
			System.out.println("Correct password after lockout: " + (lockedOut.isPresent() ? "Logged In (BUG)" : "Rejected (correct)"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
