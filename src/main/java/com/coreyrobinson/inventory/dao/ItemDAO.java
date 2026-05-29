/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Data access object for the item model. Handles all database operations for items.
 * DATE: 05/15/26
 */
package com.coreyrobinson.inventory.dao;

import java.sql.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import com.coreyrobinson.inventory.model.Item;
import com.coreyrobinson.inventory.util.DatabaseConnection;

public class ItemDAO {

	/**
	 * Finds an item by item I.D.
	 * @param itemId	The item's I.D.
	 * @return	The item.
	 * @throws SQLException	Error from the database.
	 */
	public Optional<Item> findById(int itemId) throws SQLException {
		String sql = "SELECT * FROM items WHERE itemID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, itemId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapResultSetToItem(rs));
				}
				return Optional.empty();
			}
		}
	}

	/**
	 * Finds an item by its name.
	 * @param itemName	The item's name.
	 * @return	The item.
	 * @throws SQLException	Error from the database.
	 */
	public Optional<Item> findByName(String itemName) throws SQLException {
		String sql = "SELECT * FROM items WHERE itemName = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, itemName);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapResultSetToItem(rs));
				}
				return Optional.empty();
			}
		}
	}

	/**
	 * Creates a list of all items.
	 * @return	The list of items.
	 * @throws SQLException	Error from the database.
	 */
	public List<Item> findAll() throws SQLException {
		String sql = "SELECT * FROM items";
		List<Item> items = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				items.add(mapResultSetToItem(rs));
			}
		}
		return items;
	}

	/**
	 * Either saves a new item to the database, or updates an item's information in the database.
	 * @param item	The item to add or update.
	 * @return	The new or updated item.
	 * @throws SQLException Error from the database.
	 */
	public Item save(Item item) throws SQLException {
		if (item.getItemId() == 0) {
			return insert(item);
		}
		return update(item);
	}

	/**
	 * Deactivates an item.
	 * @param itemId	Item's I.D.
	 * @throws SQLException Error from the database.
	 */
	public void deactivate(int itemId) throws SQLException {
		String sql = "UPDATE items SET is_active = FALSE WHERE itemID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, itemId);
			stmt.executeUpdate();
		}
	}

	/**
	 * Assigns values to an item.
	 * @param rs	A result set request for a given item.
	 * @return	The result set for the requested item.
	 * @throws SQLException	Error accessing the database.
	 */
	private Item mapResultSetToItem(ResultSet rs) throws SQLException {
		Item item = new Item();
		item.setItemId(rs.getInt("itemID"));
		item.setItemName(rs.getString("itemName"));
		item.setUnitId(rs.getInt("unitID"));
		item.setCategoryId(rs.getInt("categoryID"));
		item.setCurrentStock(rs.getBigDecimal("currentStock"));
		item.setParstock(rs.getBigDecimal("parstock"));
		item.setActive(rs.getBoolean("is_active"));
		return item;
	}
	
	/**
	 * Updates an existing item in the database.
	 * @param item	Item to update.
	 * @return	Updated item.
	 * @throws SQLException	Error from the database.
	 */
	private Item update(Item item) throws SQLException {
		String sql = "UPDATE items SET itemName = ?, unitID = ?, currentStock = ?, "
				+ "parstock = ?, categoryID = ?, is_active = ? "
				+ "WHERE itemID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, item.getItemName());
			stmt.setInt(2, item.getUnitId());
			stmt.setBigDecimal(3, item.getCurrentStock());
			stmt.setBigDecimal(4, item.getParstock());
			stmt.setInt(5, item.getCategoryId());
			stmt.setBoolean(6, item.isActive());
			stmt.setInt(7, item.getItemId());
			stmt.executeUpdate();
		}
		return item;
	}
	

	/**
	 * Inserts a new item into the database.
	 * @param item	Item to insert.
	 * @return	Inserted item.
	 * @throws SQLException	Error from the database.
	 */
	private Item insert(Item item) throws SQLException {
		String sql = "INSERT INTO items (itemName, unitID, currentStock, parstock, categoryID, is_active) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, item.getItemName());
			stmt.setInt(2, item.getUnitId());
			stmt.setBigDecimal(3, item.getCurrentStock());
			stmt.setBigDecimal(4, item.getParstock());
			stmt.setInt(5, item.getCategoryId());
			stmt.setBoolean(6, item.isActive());
			stmt.executeUpdate();
			// Retrieve auto-generated itemID
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					item.setItemId(generatedKeys.getInt(1));
				}
			}
		}
		return item;
	}
}
