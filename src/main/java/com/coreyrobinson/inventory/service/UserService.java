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
		if (plainPassword.length() < 8 || plainPassword == null) {
			throw new IllegalArgumentException("Password must be at least 8 characters");
		}
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
	
}
