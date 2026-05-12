/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Data access object for the User model. handles all database operations on the users table.
 * DATE: 05/06/2026
 */
package com.coreyrobinson.inventory.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.coreyrobinson.inventory.model.User;
import com.coreyrobinson.inventory.util.DatabaseConnection;

public class UserDAO {

	/**
	 * Finds a user by user I.D.
	 * @param userId			User's I.D.
	 * @return					The user.
	 * @throws SQLException
	 */
	public Optional<User> findByUserId(int userId) throws SQLException {
		String sql = "SELECT userID, username, password_hash, position, created_at, is_active, failed_login_attempts, locked_until "
				+ "FROM users "
				+ "WHERE userID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapResultSetToUser(rs));
				}
				return Optional.empty();
			}
		}
	}

	/**
	 * Find a user by user name.
	 * @param username		User's user name.
	 * @return				The user.
	 * @throws SQLException
	 */
	public Optional<User> findByUsername(String username) throws SQLException {
		String sql = "SELECT userID, username, password_hash, position, created_at, is_active, failed_login_attempts, locked_until "
				+ "FROM users "
				+ "WHERE username = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapResultSetToUser(rs));
				}
				return Optional.empty();
			}
		}
	}

	/**
	 * Creates a list of all users.
	 * @return The list of users.
	 * @throws SQLException
	 */
	public List<User> findAll() throws SQLException {
		String sql = "SELECT userID, username, password_hash, position, created_at, is_active, failed_login_attempts, locked_until "
				+ "FROM users ";
		List<User> users = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				users.add(mapResultSetToUser(rs));
			}
		}
		return users;
	}

	/**
	 * Either saves a new user to the database, or updates a user's information in the database.
	 * @param user	The user to add or update.
	 * @return	The new or updated user.
	 * @throws SQLException
	 */
	public User save(User user) throws SQLException {
		if (user.getUserId() == 0) {
			return insert(user);
		}
		return update(user);
	}

	/**
	 * Deactivates a user.
	 * @param userId	User's I.D.
	 * @throws SQLException
	 */
	public void deactivate(int userId) throws SQLException {
		String sql = "UPDATE users SET is_active = ? WHERE userID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setBoolean(1, false);
			stmt.setInt(2, userId);
			stmt.executeUpdate();
		}
	}


	/**
	 * Increments the counter for failed log-ins.
	 * @param userId	User's I.D.
	 * @throws SQLException
	 */
	public void incrementFailedAttempts(int userId) throws SQLException {
		String sql = "UPDATE users SET failed_login_attempts = failed_login_attempts + 1 "
				+ "WHERE userID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			stmt.executeUpdate();
		}
	}

	/**
	 * Resets the counter for failed log-in attempts.
	 * @param userId	User's I.D.
	 * @throws SQLException
	 */
	public void resetFailedAttempts(int userId) throws SQLException {
		String sql = "UPDATE users SET failed_login_attempts = 0, locked_until = NULL "
				+ "WHERE userID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			stmt.executeUpdate();
		}
	}

	/**
	 * If the user exceeds allowed failed log-in attempts, locks the user for a period of time.
	 * @param userId	User's I.D.
	 * @param lockedUntil	Amount of time the user is locked out.
	 * @throws SQLException
	 */
	public void setLockedUntil(int userId, LocalDateTime lockedUntil) throws SQLException {
		String sql = "UPDATE users SET locked_until = ? "
				+ "WHERE userID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			if (lockedUntil != null) {
				stmt.setTimestamp(1, Timestamp.valueOf(lockedUntil));
			} else {
				stmt.setNull(1, java.sql.Types.TIMESTAMP);
			}
			stmt.setInt(2, userId);
			stmt.executeUpdate();
		}
	}

	/**
	 * Counts and displays active managers.
	 * @return	Total active managers.
	 * @throws SQLException
	 */
	public int countActiveManagers() throws SQLException {
		String sql = "SELECT COUNT(*) "
				+ "FROM users "
				+ "WHERE position = 'Manager' AND is_active = true";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			rs.next();
			return rs.getInt(1);
		}
	}

	/**
	 * Assigns values to a user.
	 * @param rs			A result set request for a given user.
	 * @return				The result set for the requested user.
	 * @throws SQLException
	 */
	private User mapResultSetToUser(ResultSet rs) throws SQLException {
		User user = new User();
		user.setUserId(rs.getInt("userID"));
		user.setUsername(rs.getString("username"));
		user.setPasswordHash(rs.getString("password_hash"));
		user.setPosition(rs.getString("position"));

		// Timestamp to LocalDateTime conversion
		Timestamp createdAtTs = rs.getTimestamp("created_at");
		if (createdAtTs != null) {
			user.setCreatedAt(createdAtTs.toLocalDateTime());
		}
		user.setActive(rs.getBoolean("is_active"));
		user.setFailedLoginAttempts(rs.getInt("failed_login_attempts"));
		Timestamp lockedUntilTs = rs.getTimestamp("locked_until");
		if (lockedUntilTs != null) {
			user.setLockedUntil(lockedUntilTs.toLocalDateTime());
		}
		return user;
	}

	/**
	 * Updates an existing user in the database.
	 * @param user	User to be updated.
	 * @return	Updated user.
	 * @throws SQLException
	 */
	private User update(User user) throws SQLException {
		String sql = "UPDATE users SET username = ?, password_hash = ?, position = ?, "
				+ "is_active = ?, failed_login_attempts = ?, locked_until = ? "
				+ "WHERE userID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPasswordHash());
			stmt.setString(3, user.getPosition());
			stmt.setBoolean(4, user.isActive());
			stmt.setInt(5, user.getFailedLoginAttempts());
			if (user.getLockedUntil() != null) {
				stmt.setTimestamp(6, Timestamp.valueOf(user.getLockedUntil()));
			} else {
				stmt.setNull(6,  java.sql.Types.TIMESTAMP);
			}
			stmt.setInt(7, user.getUserId());
			stmt.executeUpdate();
		}
		return user;
	}

	/**
	 * Inserts a new user into the database.
	 * @param user	User to insert.
	 * @return	Inserted user.
	 * @throws SQLException
	 */
	private User insert(User user) throws SQLException {
		String sql = "INSERT INTO users (username, password_hash, position, is_active, "
				+ "failed_login_attempts, locked_until) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPasswordHash());
			stmt.setString(3, user.getPosition());
			stmt.setBoolean(4, user.isActive());
			stmt.setInt(5, user.getFailedLoginAttempts());
			// Nullable timestamp handling.
			if (user.getLockedUntil() != null) {
				stmt.setTimestamp(6, Timestamp.valueOf(user.getLockedUntil()));
			} else {
				stmt.setNull(6,  java.sql.Types.TIMESTAMP);
			}
			stmt.executeUpdate();
			// Retrieve auto-generated userID
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					user.setUserId(generatedKeys.getInt(1));
				}
			}
		}
		return user;
	}

}
