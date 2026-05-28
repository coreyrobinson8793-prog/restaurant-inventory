/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Data access object for suppliers. Handles database operations.
 * DATE: 05/23/26
 */
package com.coreyrobinson.inventory.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import com.coreyrobinson.inventory.model.Supplier;
import com.coreyrobinson.inventory.util.DatabaseConnection;

public class SupplierDAO {

	/**
	 * Finds a supplier by supplier I.D.
	 * @param supplierId	Supplier's I.D.
	 * @return	The supplier.
	 * @throws SQLException	Error from the database.
	 */
	public Optional<Supplier> findById(int supplierId)  throws SQLException {
		String sql = "SELECT supplierID, supplierName, repName, phone, email, address, is_active "
				+ "FROM suppliers WHERE supplierID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, supplierId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapResultSetToSupplier(rs));
				}
				return Optional.empty();
			}
		}		
	}

	/**
	 * Finds a supplier by its name.
	 * @param supplierName	The supplier's name.
	 * @return	The supplier.
	 * @throws SQLException	Error from the database.
	 */
	public Optional<Supplier> findByName(String supplierName) throws SQLException {
		String sql = "SELECT supplierID, supplierName, repName, phone, email, address, is_active "
				+ "FROM suppliers WHERE supplierName = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, supplierName);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapResultSetToSupplier(rs));
				}
				return Optional.empty();
			}
		}		
	}

	/**
	 * Creates a list of all suppliers.
	 * @return	The list of suppliers.
	 * @throws SQLException Error from the database.
	 */
	public List<Supplier> findAll() throws SQLException {
		String sql = "SELECT supplierID, supplierName, repName, phone, email, address, is_active FROM suppliers";
		List<Supplier> suppliers = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				suppliers.add(mapResultSetToSupplier(rs));
			}
		}
		return suppliers;
	}

	/**
	 * Either saves a new supplier to the database, or updates a supplier's information in the database.
	 * @param supplier	The supplier to add or updated.
	 * @return	The new or updated supplier.
	 * @throws SQLException	Error from the database.
	 */
	public Supplier save(Supplier supplier) throws SQLException {
		if (supplier.getSupplierId() == 0) {
			return insert(supplier);
		}
		return update(supplier);
	}

	/**
	 * Deactivates a supplier.
	 * @param supplierId	The supplier to deactivate.
	 * @throws SQLException	Error from the database.
	 */
	public void deactivate(int supplierId) throws SQLException {
		String sql = "UPDATE suppliers SET is_active = FALSE WHERE supplierID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, supplierId);
			stmt.executeUpdate();
		}
	}

	/**
	 * Updates an existing supplier in the database.
	 * @param supplier	Supplier to update.
	 * @return	Updated supplier.
	 * @throws SQLException	Error from the database.
	 */
	private Supplier update(Supplier supplier)  throws SQLException {
		String sql = "UPDATE suppliers SET supplierName = ?, repName = ?, phone = ?, email = ?, address = ?, is_active = ? WHERE supplierID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, supplier.getSupplierName());
			stmt.setString(2, supplier.getRepName());
			stmt.setString(3, supplier.getPhone());
			stmt.setString(4, supplier.getEmail());
			stmt.setString(5, supplier.getAddress());
			stmt.setBoolean(6, supplier.isActive());
			stmt.setInt(7, supplier.getSupplierId());
			stmt.executeUpdate();
		}
		return supplier;
	}

	/**
	 * Inserts a new supplier into the database.
	 * @param supplier	Supplier to insert.
	 * @return	Inserted supplier.
	 * @throws SQLException	Error from the database.
	 */
	private Supplier insert(Supplier supplier) throws SQLException {
		String sql = "INSERT INTO suppliers (supplierName, repName, phone, email, address, is_active) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, supplier.getSupplierName());
			stmt.setString(2, supplier.getRepName());
			stmt.setString(3, supplier.getPhone());
			stmt.setString(4, supplier.getEmail());
			stmt.setString(5, supplier.getAddress());
			stmt.setBoolean(6, supplier.isActive());
			stmt.executeUpdate();
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					supplier.setSupplierId(generatedKeys.getInt(1));
				}
			}
		}
		return supplier;
	}

	/**
	 * Assigns values to a supplier.
	 * @param rs	A result set request for a given supplier.
	 * @return	The result set for the requested item.
	 * @throws SQLException	Error from the database.
	 */
	private Supplier mapResultSetToSupplier(ResultSet rs) throws SQLException {
		Supplier supplier = new Supplier();
		supplier.setSupplierId(rs.getInt("supplierID"));
		supplier.setSupplierName(rs.getString("supplierName"));
		supplier.setRepName(rs.getString("repName"));
		supplier.setPhone(rs.getString("phone"));
		supplier.setEmail(rs.getString("email"));
		supplier.setAddress(rs.getString("address"));
		supplier.setActive(rs.getBoolean("is_active"));
		return supplier;
	}

}
