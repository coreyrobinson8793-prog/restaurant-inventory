/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Data access object for a category. Handles database operations for separating items into categories.
 * DATE: 05/17/2026
 */
package com.coreyrobinson.inventory.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.coreyrobinson.inventory.model.Category;
import com.coreyrobinson.inventory.util.DatabaseConnection;

public class CategoryDAO {

	/**
	 * Finds a category by the category I.D.
	 * @param catId	Categories I.D.
	 * @return	The category.
	 * @throws SQLException	Error from the database.
	 */
	public Optional<Category> findById(int catId) throws SQLException {
		String sql = "SELECT categoryID, categoryName, description, is_active FROM categories WHERE categoryID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, catId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapResultSetToCategory(rs));
				}
				return Optional.empty();
			}
		}		
	}

	/**
	 * Finds a category by its name.
	 * @param catName	Name of the category.
	 * @return	The category.
	 * @throws SQLException	Error from the database.
	 */
	public Optional<Category> findByName(String catName) throws SQLException {
		String sql = "SELECT categoryID, categoryName, description, is_active FROM categories WHERE categoryName = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, catName);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapResultSetToCategory(rs));
				}
				return Optional.empty();
			}
		}
	}

	/**
	 * Creates a list of all categories.
	 * @return	The list of categories.
	 * @throws SQLException	Error from  the database.
	 */
	public List<Category> findAll() throws SQLException {
		String sql = "SELECT categoryID, categoryName, description, is_active FROM categories";
		List<Category> categories = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				categories.add(mapResultSetToCategory(rs));
			}
		}
		return categories;
	}

	/**
	 * Either saves a new category to the database, or updates a categories information in the database.
	 * @param cat	The category to add or update.
	 * @return	The new or updated category.
	 * @throws SQLException Error from the database.
	 */
	public Category save(Category cat) throws SQLException {
		if (cat.getCategoryId() == 0) {
			return insert(cat);
		}
		return update(cat);
	}

	/**
	 * Deactivates a category.
	 * @param catId	Categories I.D.
	 * @throws SQLException Error from the database.
	 */
	public void deactivate(int catId) throws SQLException {
		String sql = "UPDATE categories SET is_active = FALSE WHERE categoryID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, catId);
			stmt.executeUpdate();
		}
	}

	/**
	 * Updates an existing category in the database.
	 * @param cat	 Category to update.
	 * @return	The updated category.
	 * @throws SQLException	Error from the database.
	 */
	private Category update(Category cat) throws SQLException{
		String sql = "UPDATE categories SET categoryName = ?, description = ?, is_active = ? WHERE categoryID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, cat.getCategoryName());
			stmt.setString(2, cat.getDescription());
			stmt.setBoolean(3, cat.isActive());
			stmt.setInt(4, cat.getCategoryId());
			stmt.executeUpdate();
		}
		return cat;
	}

	/**
	 * Inserts a new category into the database.
	 * @param cat	New category to insert.
	 * @return	Inserted category.
	 * @throws SQLException	Error from the database.
	 */
	private Category insert(Category cat) throws SQLException {
		String sql = "INSERT INTO categories (categoryName, description, is_active) VALUES (?, ?, ?)";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, cat.getCategoryName());
			stmt.setString(2, cat.getDescription());
			stmt.setBoolean(3, cat.isActive());
			stmt.executeUpdate();
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					cat.setCategoryId(generatedKeys.getInt(1));
				}
			}
		}
		return cat;
	}

	/**
	 * Assigns values to a category.
	 * @param rs	A result set request for a given category.
	 * @return	The result set for the requested category.
	 * @throws SQLException	Error from the database.
	 */
	private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
		Category cat = new Category();
		cat.setCategoryId(rs.getInt("categoryID"));
		cat.setCategoryName(rs.getString("categoryName"));
		cat.setDescription(rs.getString("description"));
		cat.setActive(rs.getBoolean("is_active"));
		return cat;
	}

}
