/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Keeps track of the current user during a log in session.
 * DATE: 05/10/2026
 */
package com.coreyrobinson.inventory.session;

import com.coreyrobinson.inventory.model.User;

public class Session {
	
	private static User currentUser;
	
	private Session() {
		
	}
	
	/**
	 * Gets current user for the session.
	 * @return	Current user in the session.
	 */
	public static User getCurrentUser() {
		return currentUser;
	}
	
	/**
	 * Sets the user for the current session.
	 * @param user	The user to set to the current session.
	 */
	public static void setCurrentUser(User user) {
		currentUser = user;
	}
	// Clears the current user.
	public static void clear() {
		currentUser = null;
	}
}