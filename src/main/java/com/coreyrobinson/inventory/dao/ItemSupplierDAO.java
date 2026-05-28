/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Data access object for relating items to their suppliers. Handles database operations.
 * DATE: 05/25/26
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

import com.coreyrobinson.inventory.model.ItemSupplier;
import com.coreyrobinson.inventory.util.DatabaseConnection;

public class ItemSupplierDAO {

	/**
	 * Finds the item linked to the supplier by I.D.
	 * @param itemSupplierId	The linked item/supplier I.D.
	 * @return	The linked item/supplier.
	 * @throws SQLException	Error from the database.
	 */
	public Optional<ItemSupplier> findById(int itemSupplierId) throws SQLException {
		String sql = "SELECT itemSupplierID, itemID, supplierID, supplier_sku, price, pack_size, pack_unit_id, is_preferred, is_active, last_price_update "
				+ "FROM item_suppliers WHERE itemSupplierID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, itemSupplierId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapResultSetToItemSupplier(rs));
				}
				return Optional.empty();
			}
		}		
	}

	/**
	 * Creates a list of all items linked to their supplier by  their item I.D.
	 * @param itemId	The item's I.D.	
	 * @return	The list of item/suppliers.
	 * @throws SQLException	Error from the database.
	 */
	public List<ItemSupplier> findByItemId(int itemId) throws SQLException {
		String sql = "SELECT itemSupplierID, itemID, supplierID, supplier_sku, price, pack_size, pack_unit_id, is_preferred, is_active, last_price_update "
				+ "FROM item_suppliers WHERE itemID = ?";
		List<ItemSupplier> itemSuppliers = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, itemId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					itemSuppliers.add(mapResultSetToItemSupplier(rs));
				}
			}
			return itemSuppliers;
		}
	}

	/**
	 * Creates a list of all items linked to their supplier by their supplier I.D.
	 * @param supplierId	The supplier's I.D.
	 * @return	The list of item/suppliers.
	 * @throws SQLException
	 */
	public List<ItemSupplier> findBySupplierId(int supplierId) throws SQLException {
		String sql = "SELECT itemSupplierID, itemID, supplierID, supplier_sku, price, pack_size, pack_unit_id, is_preferred, is_active, last_price_update "
				+ "FROM item_suppliers WHERE supplierID = ?";
		List<ItemSupplier> itemSuppliers = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, supplierId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					itemSuppliers.add(mapResultSetToItemSupplier(rs));
				}
			}
			return itemSuppliers;
		}
	}

	/**
	 * Finds if an item is linked to a supplier.
	 * @param itemId	The item's I.D.
	 * @param supplierId	The supplier's I.D.
	 * @return	The linked item to its supplier.
	 * @throws SQLException	Error from the database.
	 */
	public Optional<ItemSupplier> findByItemAndSupplier(int itemId, int supplierId) throws SQLException {
		String sql = "SELECT itemSupplierID, itemID, supplierID, supplier_sku, price, pack_size, pack_unit_id, is_preferred, is_active, last_price_update "
				+ "FROM item_suppliers WHERE itemID = ? AND supplierID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, itemId);
			stmt.setInt(2, supplierId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapResultSetToItemSupplier(rs));
				}
				return Optional.empty();
			}
		}		
	}

	/**
	 * Creates a list of all items linked to their suppliers.
	 * @return	The list of linked items to their suppliers.
	 * @throws SQLException	Error from the database.
	 */
	public List<ItemSupplier> findAll() throws SQLException {
		String sql = "SELECT itemSupplierID, itemID, supplierID, supplier_sku, price, pack_size, pack_unit_id, is_preferred, is_active, last_price_update "
				+ "FROM item_suppliers";
		List<ItemSupplier> itemSuppliers = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				itemSuppliers.add(mapResultSetToItemSupplier(rs));
			}
		}		
		return itemSuppliers;
	}

	/**
	 * Either saves a new supplier to the database, or updates a supplier's information in the database.
	 * @param itemSupplier	The item linked to their supplier to add or update.
	 * @return	The new or updated item linked to their supplier.
	 * @throws SQLException	Error from the database.
	 */
	public ItemSupplier save(ItemSupplier itemSupplier) throws SQLException {
		if (itemSupplier.getItemSupplierId() == 0) {
			return insert(itemSupplier);
		}
		return update(itemSupplier);
	}

	/**
	 * Deactivates an item linked to its supplier.
	 * @param itemSupplierId	The item linked to its supplier to deactivate.
	 * @throws SQLException	Error from the database.
	 */
	public void deactivate(int itemSupplierId) throws SQLException {
		String sql = "UPDATE item_suppliers SET is_active = FALSE WHERE itemSupplierID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, itemSupplierId);
			stmt.executeUpdate();
		}
	}

	/**
	 * Updates an existing item linked to their supplier.
	 * @param itemSupplier	Item linked to the supplier to update.
	 * @return	The updated item linked to their supplier.
	 * @throws SQLException	Error from the database.
	 */
	private ItemSupplier update(ItemSupplier itemSupplier) throws SQLException {
		LocalDateTime lpu = itemSupplier.getLastPriceUpdate();
		String sql = "UPDATE item_suppliers SET itemID = ?, supplierID = ?, supplier_sku = ?, price = ?, pack_size = ?,"
				+ " pack_unit_id = ?, is_preferred = ?, is_active = ?, last_price_update = ?"
				+ " WHERE itemSupplierID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, itemSupplier.getItemId());
			stmt.setInt(2, itemSupplier.getSupplierId());
			stmt.setString(3, itemSupplier.getSupplierSku());
			stmt.setBigDecimal(4, itemSupplier.getPrice());
			stmt.setBigDecimal(5, itemSupplier.getPackSize());
			stmt.setInt(6, itemSupplier.getPackUnitId());
			stmt.setBoolean(7, itemSupplier.isPreferred());
			stmt.setBoolean(8, itemSupplier.isActive());
			stmt.setTimestamp(9, lpu == null ? null : Timestamp.valueOf(lpu));
			stmt.setInt(10, itemSupplier.getItemSupplierId());
			stmt.executeUpdate();
		}
		return itemSupplier;
	}

	/**
	 * Inserts a new item linked to their supplier.
	 * @param itemSupplier	The item linked to their supplier to insert.
	 * @return	Inserted item linked to their supplier.
	 * @throws SQLException	Error from the database.
	 */	
	private ItemSupplier insert(ItemSupplier itemSupplier) throws SQLException {
		LocalDateTime lpu = itemSupplier.getLastPriceUpdate();
		String sql = "INSERT INTO item_suppliers (itemID, supplierID, supplier_sku, price, pack_size, pack_unit_id, "
				+ "is_preferred, is_active, last_price_update) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, itemSupplier.getItemId());
			stmt.setInt(2, itemSupplier.getSupplierId());
			stmt.setString(3, itemSupplier.getSupplierSku());
			stmt.setBigDecimal(4, itemSupplier.getPrice());
			stmt.setBigDecimal(5, itemSupplier.getPackSize());
			stmt.setInt(6, itemSupplier.getPackUnitId());
			stmt.setBoolean(7, itemSupplier.isPreferred());
			stmt.setBoolean(8, itemSupplier.isActive());
			stmt.setTimestamp(9, lpu == null ? null : Timestamp.valueOf(lpu));
			stmt.executeUpdate();
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					itemSupplier.setItemSupplierId(generatedKeys.getInt(1));
				}
			}
		}
		return itemSupplier;
	}

	/**
	 * Assigns values to an item linked to its supplier.
	 * @param rs	A result set request for a given item linked to its supplier.
	 * @return	The result set for the requested item linked to its supplier.
	 * @throws SQLException	Error from the database.
	 */
	private ItemSupplier mapResultSetToItemSupplier(ResultSet rs) throws SQLException {
		ItemSupplier itemSupplier = new ItemSupplier();
		Timestamp ts = rs.getTimestamp("last_price_update");
		itemSupplier.setItemSupplierId(rs.getInt("itemSupplierID"));
		itemSupplier.setItemId(rs.getInt("itemID"));
		itemSupplier.setSupplierId(rs.getInt("supplierID"));
		itemSupplier.setSupplierSku(rs.getString("supplier_sku"));
		itemSupplier.setPrice(rs.getBigDecimal("price"));
		itemSupplier.setPackSize(rs.getBigDecimal("pack_size"));
		itemSupplier.setPackUnitId(rs.getInt("pack_unit_id"));
		itemSupplier.setPreferred(rs.getBoolean("is_preferred"));
		itemSupplier.setActive(rs.getBoolean("is_active"));
		itemSupplier.setLastPriceUpdate(ts == null ? null : ts.toLocalDateTime());
		return itemSupplier;
	}

}
