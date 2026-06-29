/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Data access object for purchase orders. Handles database operations.
 * DATE: 06/26/26
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

import com.coreyrobinson.inventory.model.PurchaseOrder;
import com.coreyrobinson.inventory.util.DatabaseConnection;

public class PurchaseOrderDAO {
	
	/**
	 * Finds a purchase order by its I.D.
	 * @param poId The purchase order's I.D.
	 * @return Purchase order if found, otherwise empty.
	 * @throws SQLException Error from the database.
	 */
	public Optional<PurchaseOrder> findById(int poId) throws SQLException {
		String sql = "SELECT poID, supplierID, created_by, created_date, current_status, total, notes "
				+ "FROM purchase_orders WHERE poID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, poId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapResultSetToPurchaseOrder(rs));
				}
				return Optional.empty();
			}
		}
	}
	
	/**
	 * Creates a list of all purchase orders.
	 * @return List of purchase orders.
	 * @throws SQLException Error from the database.
	 */
	public List<PurchaseOrder> findAll() throws SQLException {
		String sql = "SELECT poID, supplierID, created_by, created_date, current_status, total, notes "
				+ "FROM purchase_orders";
		List<PurchaseOrder> purchaseOrders = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				purchaseOrders.add(mapResultSetToPurchaseOrder(rs));
			}
		}
		return purchaseOrders;
	}
	
	/**
	 * Creates a list of all purchase orders for a specific supplier.
	 * @param supplierId The supplier's I.D.
	 * @return List of purchase orders for the supplier.
	 * @throws SQLException Error from the database.
	 */
	public List<PurchaseOrder> findBySupplierId(int supplierId) throws SQLException {
		String sql = "SELECT poID, supplierID, created_by, created_date, current_status, total, notes "
				+ "FROM purchase_orders WHERE supplierID = ?";
		List<PurchaseOrder> purchaseOrders = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, supplierId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					purchaseOrders.add(mapResultSetToPurchaseOrder(rs));
				}
			}
		}
		return purchaseOrders;
	}
	
	/**
	 * Creates a list of all purchase orders with a specific status.
	 * @param status The status of the purchase orders to find.
	 * @return List of purchase orders with the specified status.
	 * @throws SQLException Error from the database.
	 */
	public List<PurchaseOrder> findByStatus(String status) throws SQLException {
		String sql = "SELECT poID, supplierID, created_by, created_date, current_status, total, notes "
				+ "FROM purchase_orders WHERE current_status = ?";
		List<PurchaseOrder> purchaseOrders = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, status);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					purchaseOrders.add(mapResultSetToPurchaseOrder(rs));
				}
			}
		}
		return purchaseOrders;
	}
	
	/**
	 * Either saves a new purchase order to the database, or updates a purchase order's information in the database.
	 * @param po The purchase order to save or update.
	 * @return The saved or updated purchase order.
	 * @throws SQLException Error from the database.
	 */
	public PurchaseOrder save(PurchaseOrder po) throws SQLException {
		if (po.getPoId() == 0) {
			return insert(po);
		}
		return update(po);
	}

	/**
	 * Updates a purchase order's information in the database.
	 * @param po The purchase order to update.
	 * @return The updated purchase order.
	 * @throws SQLException Error from the database.
	 */
	private PurchaseOrder update(PurchaseOrder po) throws SQLException {
		LocalDateTime createdDate = po.getCreatedDate();
		String sql = "UPDATE purchase_orders SET supplierID = ?, created_by = ?, created_date = ?, current_status = ?, total = ?, notes = ? "
				+ "WHERE poID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, po.getSupplierId());
			stmt.setInt(2, po.getCreatedBy());
			stmt.setTimestamp(3, createdDate == null ? null : Timestamp.valueOf(createdDate));
			stmt.setString(4, po.getCurrentStatus());
			stmt.setBigDecimal(5, po.getTotal());
			stmt.setString(6, po.getNotes());
			stmt.setInt(7, po.getPoId());
			stmt.executeUpdate();
		}
		return po;
	}

	/**
	 * Inserts a new purchase order into the database.
	 * @param po The purchase order to insert.
	 * @return The inserted purchase order.
	 * @throws SQLException Error from the database.
	 */
	private PurchaseOrder insert(PurchaseOrder po) throws SQLException{
		LocalDateTime createdDate = po.getCreatedDate();
		String sql = "INSERT INTO purchase_orders (supplierID, created_by, created_date, current_status, total, notes) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, po.getSupplierId());
			stmt.setInt(2, po.getCreatedBy());
			stmt.setTimestamp(3, createdDate == null ? null : Timestamp.valueOf(createdDate));
			stmt.setString(4, po.getCurrentStatus());
			stmt.setBigDecimal(5, po.getTotal());
			stmt.setString(6, po.getNotes());
			stmt.executeUpdate();
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					po.setPoId(generatedKeys.getInt(1));
				}
			}
		}
		return po;
	}

	/**
	 * Assigns values to a purchase order.
	 * @param rs A result set request for a given purchase order.
	 * @return The result set for the requested item linked to its supplier.
	 * @throws SQLException Error from the database.
	 */
	private PurchaseOrder mapResultSetToPurchaseOrder(ResultSet rs) throws SQLException {
		PurchaseOrder po = new PurchaseOrder();
		Timestamp ts = rs.getTimestamp("created_date");
		po.setPoId(rs.getInt("poID"));
		po.setSupplierId(rs.getInt("supplierID"));
		po.setCreatedBy(rs.getInt("created_by"));
		po.setCreatedDate(ts == null ? null : ts.toLocalDateTime());
		po.setCurrentStatus(rs.getString("current_status"));
		po.setTotal(rs.getBigDecimal("total"));
		po.setNotes(rs.getString("notes"));
		return po;
	}

}
