package com.coreyrobinson.inventory.app;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import com.coreyrobinson.inventory.dao.UserDAO;
import com.coreyrobinson.inventory.model.User;

public class UserDAOTest {

	public static void main(String[] args) {

		UserDAO testUser = new UserDAO();

		try {
			// Test manager count
			int managerCount = testUser.countActiveManagers();
			System.out.println("Active managers: " + managerCount);
			// Test failed attempts incrementing
			testUser.incrementFailedAttempts(1);
			testUser.incrementFailedAttempts(1);
			Optional<User> afterInc = testUser.findByUserId(1);
			if (afterInc.isPresent()) {
				System.out.println("After 2 increments: " + afterInc.get().getFailedLoginAttempts());
			}
			// Test reset failed attempts
			testUser.resetFailedAttempts(1);
			Optional<User> afterReset = testUser.findByUserId(1);
			if (afterReset.isPresent()) {
				System.out.println("After reset: " + afterReset.get().getFailedLoginAttempts());
			}
			// test lock out
			testUser.setLockedUntil(1, LocalDateTime.now().plusMinutes(10));
			Optional<User> afterLock = testUser.findByUserId(1);
			if (afterLock.isPresent()) {
				System.out.println("Locked Until: " + afterLock.get().getLockedUntil());
			}
			// Clear lock
			testUser.setLockedUntil(1, null);
		} catch (SQLException e) {
			System.out.println("Database error");
			e.printStackTrace();
		}
	}

}
