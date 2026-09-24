/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Handles the operations for a user.
 * DATE: 05/08/2026
 */
package com.coreyrobinson.inventory.service;

import com.coreyrobinson.inventory.dao.UserDAO;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.mindrot.jbcrypt.BCrypt;
import com.coreyrobinson.inventory.model.User;


public class UserService {

	private final UserDAO userDao = new UserDAO();	// will change to to dependency injection
	private static final int MAX_FAILED_ATTEMPTS = 5;
	private static final int LOCKOUT_DURATION_MINUTES = 10;

	/**
	 * Registers a new user for the application.
	 * @param username	User's requested username.
	 * @param plainPassword	User's requested password.
	 * @param position	User's position.
	 * @return	The registered user.
	 * @throws SQLException	If user cannot be saved to the database.
	 * @throws IllegalArgumentException If username is empty, password is too short, position is invalid, or username already exists.
	 */
	public User registerUser(String username, String plainPassword, String position) throws SQLException {
		if (username == null || username.isBlank()) {
			throw new IllegalArgumentException("Username cannot be empty");
		}
		validatePassword(plainPassword);
		if (!"Manager".equals(position) && !"Staff".equals(position)) {
			throw new IllegalArgumentException("Position must be 'Manager' or 'Staff'");
		}
		Optional<User> existing = userDao.findByUsername(username);
		if (existing.isPresent()) {
			throw new IllegalArgumentException("Username '" + username + "' already exists");
		}
		String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
		User user = new User(username, hashedPassword, position);
		return userDao.save(user);
	}

	/**
	 * Changes a user's password.
	 * @param userId	The user changing their password.
	 * @param currentPassword	The user's current password.
	 * @param newPassword	The user's new password.
	 * @return	The updated user.
	 * @throws SQLException	If the user cannot be updated in the database.
	 * @throws IllegalArgumentException If the user is not found, the current password is incorrect, or the new password is too short.
	 * @throws IllegalArgumentException If the new password is the same as the current password.
	 */
	public User changePassword(int userId, String currentPassword, String newPassword) throws SQLException {
		Optional<User> optUser = userDao.findByUserId(userId);
		if (!optUser.isPresent()) {
			throw new IllegalArgumentException("User not found");
		}
		User user = optUser.get();
		if (!BCrypt.checkpw(currentPassword, user.getPasswordHash())) {
			throw new IllegalArgumentException("Current password is incorrect");
		}
		validatePassword(newPassword);
		if (BCrypt.checkpw(newPassword, user.getPasswordHash())) {
			throw new IllegalArgumentException("New password cannot be the same as the current password");
		}
		String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
		user.setPasswordHash(hashedPassword);
		return userDao.save(user);
	}
	
	/**
	 * Resets a user's password by an admin or manager.
	 * @param managerId	The user resetting the password (must be a manager).
	 * @param targetUserId	The user whose password is being reset.
	 * @param newPassword	The new password for the target user.
	 * @return	The updated user.
	 * @throws SQLException	If the user cannot be updated in the database.
	 * @throws IllegalArgumentException If the manager is not found or a user that isn't a manager tries to reset a password, manager tries to reset their own password, the target user is not found, or the new password is too short.
	 */
	public User resetPassword(int managerId, int targetUserId, String newPassword) throws SQLException {
		
		Optional<User> optManager = userDao.findByUserId(managerId);
		if (!optManager.isPresent() || !optManager.get().isActive() || !"Manager".equals(optManager.get().getPosition())) {
			throw new IllegalArgumentException("Only active managers can reset passwords");
		}
		if (managerId == targetUserId) {
			throw new IllegalArgumentException("Managers cannot reset their own password");
		}
		Optional<User> optTargetUser = userDao.findByUserId(targetUserId);
		if (!optTargetUser.isPresent()) {
			throw new IllegalArgumentException("Target user not found");
		}
		validatePassword(newPassword);
		String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
		User targetUser = optTargetUser.get();
		targetUser.setPasswordHash(hashedPassword);
		targetUser.setFailedLoginAttempts(0); // Reset failed login attempts on password reset
		targetUser.setLockedUntil(null); // Unlock the user if they were locked out
		return userDao.save(targetUser);
	}

	/**
	 * Authenticates a user attempting to log in.
	 * @param username	The username trying to log in.
	 * @param plainPassword	The password for the user trying to log in.
	 * @return	Optional if the user successfully logged in, and empty if the authentication failed to log the user in.
	 * @throws SQLException	
	 */
	public Optional<User> authenticate(String username, String plainPassword) throws SQLException {
		Optional<User> optUser = userDao.findByUsername(username);
		if (!optUser.isPresent()) {
			return Optional.empty();
		}
		User user = optUser.get();
		if (!user.isActive()) {
			return Optional.empty();
		}
		if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(LocalDateTime.now())) {
			return Optional.empty();
		}
		if (!BCrypt.checkpw(plainPassword, user.getPasswordHash())) {
			userDao.incrementFailedAttempts(user.getUserId());
			int failedAttempts = user.getFailedLoginAttempts() + 1; // Current amount of failed log in attempts after most recent attempt.
			if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
				userDao.setLockedUntil(user.getUserId(), LocalDateTime.now().plusMinutes(LOCKOUT_DURATION_MINUTES));
			}
			return Optional.empty();
		}
		userDao.resetFailedAttempts(user.getUserId());
		return Optional.of(user);
	}

	/**
	 * Allows a manager to deactivate a user.
	 * @param deactivateById	The user to deactivate.
	 * @param loggedInUserId	The user doing the deactivating.
	 * @throws SQLException		User cannot be deactivated due to a database error.
	 */
	public void deactivateUser(int deactivateById, int loggedInUserId) throws SQLException {
		if (deactivateById == loggedInUserId) {
			throw new IllegalArgumentException("Cannot deactivate your own account");
		}
		Optional<User> optUser = userDao.findByUserId(deactivateById);
		if (!optUser.isPresent()) {
			throw new IllegalArgumentException("No user found");
		}
		User user = optUser.get();
		if ("Manager".equals(user.getPosition()) && user.isActive()) {
			int activeManagers = userDao.countActiveManagers();
			if (activeManagers <= 1) {
				throw new IllegalArgumentException("Cannot deactivate the last active manager.");
			}
		}
		userDao.deactivate(deactivateById);
	}

	/**
	 * Finds all users in the application.
	 * @return	All users.
	 * @throws SQLException	If a database error occurs.
	 */
	public List<User> findAllUsers() throws SQLException {
		return userDao.findAll();		
	}

	/**
	 * Validates a password to ensure it meets the minimum requirements.
	 * @param password	The password to validate.
	 * @throws IllegalArgumentException If the password is null or less than 8 characters.
	 */
	private void validatePassword(String password) {
		if (password == null || password.length() < 8) {
			throw new IllegalArgumentException("Password must be at least 8 characters");
		}
	}

}
