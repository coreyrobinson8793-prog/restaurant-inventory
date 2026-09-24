package com.coreyrobinson.inventory.app;

import java.sql.SQLException;

import com.coreyrobinson.inventory.service.UserService;

public class UserServiceManagerChangePasswordTest {

	public static void main(String[] args) throws SQLException {
		UserService userService = new UserService();
		try {
			// test a staff user trying to change the manager's password
			userService.resetPassword(3, 4, "NewPassword123");
			System.out.println("FAIL: no exception thrown for staff user trying to change manager's password");
		} catch (IllegalArgumentException e) {
			System.out.println("Rejected: " + e.getMessage());
		}
		// test a deactivated manager as managerId trying to change the password
		try {
			userService.resetPassword(2, 4, "NewPassword123");
			System.out.println("FAIL: no exception thrown for deactivated manager trying to change password");
		} catch (IllegalArgumentException e) {
			System.out.println("Rejected: " + e.getMessage());
		}
		// test a manager resetting their own password
		try {
			userService.resetPassword(1, 1, "NewPassword123");
			System.out.println("FAIL: Manager reset their own password");
		} catch (IllegalArgumentException e) {
			System.out.println("Rejected: " + e.getMessage());
		}
		// test a non-existent targetUserId
		try {
			userService.resetPassword(1, 999, "NewPassword123");
			System.out.println("FAIL: no exception thrown for non-existent targetUserId");
		} catch (IllegalArgumentException e) {
			System.out.println("Rejected: " + e.getMessage());
		}
		// test a manager resetting a staff user's password
		try {
			userService.resetPassword(1, 4, "NewPassword123");
			System.out.println("Password reset successfully by manager");
		} catch (IllegalArgumentException e) {
			System.out.println("Rejected: " + e.getMessage());
		}
	}
}


