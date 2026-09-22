package com.coreyrobinson.inventory.app;

import java.sql.SQLException;

import com.coreyrobinson.inventory.service.UserService;

public class ChangePasswordTest {

	public static void main(String[] args) {
		UserService userService = new UserService();
		// test wrong current password
		try {
			userService.changePassword(1, "wrongpassword", "newpassword");
			System.out.println("FAIL: no exception thrown for wrong current password");
		} catch (IllegalArgumentException e) {
			System.out.println("Rejected: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Error from the database: " + e.getMessage());
		}
		// test correct current password
		try {
			userService.changePassword(1, "Password123", "short");
			System.out.println("FAIL: no exception thrown for short new password");
		} catch (IllegalArgumentException e) {
			System.out.println("Rejected: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Error from the database: " + e.getMessage());
		}
		// test correct password with identical password
		try {
			userService.changePassword(1, "Password123", "Password123");
			System.out.println("FAIL: no exception thrown for identical new password");
		} catch (IllegalArgumentException e) {
			System.out.println("Rejected: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Error from the database: " + e.getMessage());
		}
		// test current password change
		try {
			userService.changePassword(1, "Password123", "NewPassword123");
			System.out.println("Password changed successfully");
		} catch (IllegalArgumentException e) {
			System.out.println("Rejected: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Error from the database: " + e.getMessage());
		}
	}
}
