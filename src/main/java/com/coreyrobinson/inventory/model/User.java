/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Creates a reference to a User object for each employee.
 * DATE: 05/02/2026
 */
package com.coreyrobinson.inventory.model;

import java.time.LocalDateTime;

public class User {
	
	private int userId;
	private String username;
	private String passwordHash;
	private String position;
	private LocalDateTime createdAt;
	private boolean active;
	private int failedLoginAttempts;
	private LocalDateTime lockedUntil;

	public User() {
		
	}
	
	/**
	 * Constructor for a user's information.
	 * @param userId	I.D. of the user.
	 * @param username	Name for the user.
	 * @param passwordHash	User's hashed password.
	 * @param position	User's position.
	 * @param createdAt		When the user created their account.
	 * @param active	If the user has access to the application.
	 * @param failedLoginAttempts	How many failed attempts the user has for logging in.
	 * @param lockedUntil	If the user is locked out or not.
	 */
	public User(int userId, String username, String passwordHash, String position, LocalDateTime createdAt, boolean active, int failedLoginAttempts, LocalDateTime lockedUntil) {
		
		this.userId = userId;
		this.username = username;
		this.passwordHash = passwordHash;
		this.position = position;
		this.createdAt = createdAt;
		this.active = active;
		this.failedLoginAttempts = failedLoginAttempts;
		this.lockedUntil = lockedUntil;
	}
	
	/**
	 * Constructor for information needed only at registration.
	 * @param username
	 * @param passwordHash
	 * @param position
	 */
	public User(String username, String passwordHash, String position) {
		this.username = username;
		this.passwordHash = passwordHash;
		this.position = position;
		this.active = true;		//new users will be active by default
	}

	public int getUserId() {
		return userId;
	}
	
	
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}
	
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getPosition() {
		return position;
	}
	
	public void setPosition(String position) {
		this.position = position;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public int getFailedLoginAttempts() {
		return failedLoginAttempts;
	}

	public void setFailedLoginAttempts(int failedLoginAttempts) {
		this.failedLoginAttempts = failedLoginAttempts;
	}

	public LocalDateTime getLockedUntil() {
		return lockedUntil;
	}

	public void setLockedUntil(LocalDateTime lockedUntil) {
		this.lockedUntil = lockedUntil;
	}
	
	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", position=" + position + ", createdAt="
				+ createdAt + ", active=" + active + ", failedLoginAttempts=" + failedLoginAttempts + ", lockedUntil="
				+ lockedUntil + "]";
	}

}
