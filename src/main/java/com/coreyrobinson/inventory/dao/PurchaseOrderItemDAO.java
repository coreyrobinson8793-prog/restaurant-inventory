/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Data access object for relating purchase orders to items. 
 * DATE: 06/26/26
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

import com.coreyrobinson.inventory.model.PurchaseOrderItem;
import com.coreyrobinson.inventory.util.DatabaseConnection;

public class PurchaseOrderItemDAO {

	/**
	 * Finds the purchase order linked to the item I.D. 
	 * @param poItemId	The linked purchase order/item I.D.
	 * @return	The linked purchase order/item.
	 * @throws SQLException	Error from the database.
	 */
	public Optional<PurchaseOrderItem> findById(int poItemId) throws SQLException {
		String sql = "SELECT po_itemID, poID, itemID, quantityOrdered, price_at_time "
				+ "FROM purchase_order_items WHERE po_itemID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, poItemId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapResultSetToPurchaseOrderItem(rs));
				}
				return Optional.empty();
			}
		}		
	}

	/**
	 * Creates a list of all purchase orders linked to their items by their poID.
	 * @param poId	The PO's I.D.
	 * @return	The list of PO/items.
	 * @throws SQLException	Error from the database.
	 */
	public List<PurchaseOrderItem> findByPurchaseOrderId(int poId) throws SQLException {
		String sql = "SELECT po_itemID, poID, itemID, quantityOrdered, price_at_time "
				+ "FROM purchase_order_items WHERE poID = ?";
		List<PurchaseOrderItem> poItems = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, poId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					poItems.add(mapResultSetToPurchaseOrderItem(rs));
				}
			}
		}
		return poItems;
	}
	
	/**
	 * Creates a list of all purchase orders linked to their items.
	 * @return	The linked list.
	 * @throws SQLException	Error from the database.
	 */
	public List<PurchaseOrderItem> findAll() throws SQLException {
		String sql = "SELECT po_itemID, poID, itemID, quantityOrdered, price_at_time "
				+ "FROM purchase_order_items";
		List<PurchaseOrderItem> poItems = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				poItems.add(mapResultSetToPurchaseOrderItem(rs));
			}
		}
		return poItems;
	}
	
	/**
	 * Either saves a new purchase order's line items to the database, or updates a purchase order's line items information.
	 * @param poItem	The purchase order item.	
	 * @return	The new or updated purchase order item.
	 * @throws SQLException	Error from the database.
	 */
	public PurchaseOrderItem save(PurchaseOrderItem poItem) throws SQLException {
		if (poItem.getPoItemId() == 0) {
			return insert(poItem);
		}
		return update(poItem);
	}
	
	/**
	 * Deletes line item on purchase order.
	 * @param poItemId	The line item to delete.
	 * @throws SQLException	Error from the database.
	 */
	public void delete(int poItemId) throws SQLException {
		String sql = "DELETE FROM purchase_order_items WHERE po_itemID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, poItemId);
			stmt.executeUpdate();
		}
	}

	/**
	 * Updates existing purchase order line item.
	 * @param poItem	The line item to update.
	 * @return	The updated line item.
	 * @throws SQLException	Error from the database.
	 */
	private PurchaseOrderItem update(PurchaseOrderItem poItem) throws SQLException{
		String sql = "UPDATE purchase_order_items SET poID = ?, itemID = ?, quantityOrdered = ?, price_at_time = ? "
				+ "WHERE po_itemID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, poItem.getPoId());
			stmt.setInt(2, poItem.getItemId());
			stmt.setBigDecimal(3, poItem.getQuantityOrdered());
			stmt.setBigDecimal(4, poItem.getPriceAtTime());
			stmt.setInt(5, poItem.getPoItemId());
			stmt.executeUpdate();
		}
		return poItem;
	}

	/**
	 * Inserts a new line item into a purchase order.
	 * @param poItem	New line item to insert.
	 * @return	Inserted line item.
	 * @throws SQLException	Error from the database.
	 */
	private PurchaseOrderItem insert(PurchaseOrderItem poItem) throws SQLException{
		String sql = "INSERT INTO purchase_order_items (poID, itemID, quantityOrdered, price_at_time) "
				+ "VALUES (?, ?, ?, ?)";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, poItem.getPoId());
			stmt.setInt(2, poItem.getItemId());
			stmt.setBigDecimal(3, poItem.getQuantityOrdered());
			stmt.setBigDecimal(4, poItem.getPriceAtTime());
			stmt.executeUpdate();
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					poItem.setPoItemId(generatedKeys.getInt(1));
				}
			}
		}
		return poItem;
	}

	/**
	 * Assigns values to a line item in a purchase order.
	 * @param rs	A result set request for a given line item.
	 * @return	The result set for the line item.
	 * @throws SQLException	Error from the database.
	 */
	private PurchaseOrderItem mapResultSetToPurchaseOrderItem(ResultSet rs) throws SQLException{
		PurchaseOrderItem poItem = new PurchaseOrderItem();
		poItem.setPoItemId(rs.getInt("po_itemID"));
		poItem.setPoId(rs.getInt("poID"));
		poItem.setItemId(rs.getInt("itemID"));
		poItem.setQuantityOrdered(rs.getBigDecimal("quantityOrdered"));
		poItem.setPriceAtTime(rs.getBigDecimal("price_at_time"));
		return poItem;
	}

}
