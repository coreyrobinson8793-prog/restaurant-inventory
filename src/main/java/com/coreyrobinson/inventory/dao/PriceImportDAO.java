/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Data access object for importing prices. Handles database operations.
 * DATE: 07/20/26
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

import com.coreyrobinson.inventory.model.PriceImport;
import com.coreyrobinson.inventory.util.DatabaseConnection;

public class PriceImportDAO {

	/**
	 * Finds an imported price by import I.D.
	 * @param importId	The imported price's I.D.
	 * @return	The imported price.
	 * @throws SQLException	Error from the database.
	 */
	public Optional<PriceImport> findById (int importId) throws SQLException {
		String sql = "SELECT importID, supplierID, imported_by, import_date, file_name, rows_processed, rows_updated, rows_skipped, notes "
				+ "FROM price_imports WHERE importID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, importId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapResultSetToPriceImport(rs));
				}
				return Optional.empty();
			}
		}		
	}

	/**
	 * Creates a list of all imported prices.
	 * @return	The imported price list.
	 * @throws SQLException	Error from the database.
	 */
	public List<PriceImport> findAll() throws SQLException {
		String sql = "SELECT importID, supplierID, imported_by, import_date, file_name, rows_processed, rows_updated, rows_skipped, notes "
				+ "FROM price_imports ORDER BY import_date DESC";
		List<PriceImport> importedPrice = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				importedPrice.add(mapResultSetToPriceImport(rs));
			}
		}
		return importedPrice;
	}

	/**
	 * Create's a list of price imports from a specific supplier.
	 * @param supplierId	The supplier.
	 * @return	The supplier's price imports.
	 * @throws SQLException	Error from the database.
	 */
	public List<PriceImport> findBySupplierId(int supplierId) throws SQLException {
		String sql = "SELECT importID, supplierID, imported_by, import_date, file_name, rows_processed, rows_updated, rows_skipped, notes "
				+ "FROM price_imports WHERE supplierID = ? ORDER BY import_date DESC";
		List<PriceImport> suppliers = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, supplierId);
			try	(ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					suppliers.add(mapResultSetToPriceImport(rs));
				}
			}
		}
		return suppliers;
	}

	/**
	 * Inserts a new imported price, or updates an existing imported price.
	 * @param importedPrice	The price import.
	 * @return	The inserted or updated price import.
	 * @throws SQLException	Error from the database.
	 */
	public PriceImport save(PriceImport importedPrice) throws SQLException {
		if (importedPrice.getImportId() == 0) {
			return insert(importedPrice);
		}
		return update(importedPrice);
	}

	/**
	 * Updates an imported price in the database.
	 * @param importedPrice	The price import to update.
	 * @return	The updated price import.
	 * @throws SQLException	Error from the database.
	 */
	private PriceImport update(PriceImport importedPrice) throws SQLException{
		LocalDateTime createdDate = importedPrice.getImportDate();
		String sql = "UPDATE price_imports SET supplierID = ?, imported_by = ?, import_date = ?, file_name = ?, rows_processed = ?, rows_updated = ?, "
				+ "rows_skipped = ?, notes = ? WHERE importID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, importedPrice.getSupplierId());
			stmt.setInt(2, importedPrice.getImportedBy());
			stmt.setTimestamp(3, createdDate == null ? null : Timestamp.valueOf(createdDate));
			stmt.setString(4, importedPrice.getFileName());
			stmt.setInt(5, importedPrice.getRowsProcessed());
			stmt.setInt(6, importedPrice.getRowsUpdated());
			stmt.setInt(7, importedPrice.getRowsSkipped());
			stmt.setString(8, importedPrice.getNotes());
			stmt.setInt(9,  importedPrice.getImportId());
			stmt.executeUpdate();
		}
		return importedPrice;
	}

	/**
	 * Inserts a new imported price into the database.
	 * @param importedPrice	The price import to insert.
	 * @return	The new imported price.
	 * @throws SQLException	Error from the database.
	 */
	private PriceImport insert(PriceImport importedPrice) throws SQLException{
		String sql = "INSERT INTO price_imports (supplierID, imported_by, file_name, rows_processed, rows_updated, rows_skipped, notes) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, importedPrice.getSupplierId());
			stmt.setInt(2, importedPrice.getImportedBy());
			stmt.setString(3, importedPrice.getFileName());
			stmt.setInt(4, importedPrice.getRowsProcessed());
			stmt.setInt(5, importedPrice.getRowsUpdated());
			stmt.setInt(6, importedPrice.getRowsSkipped());
			stmt.setString(7, importedPrice.getNotes());
			stmt.executeUpdate();
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					importedPrice.setImportId(generatedKeys.getInt(1));
				}
			}
		}
		return importedPrice;
	}

	/**
	 * Assigns values to a price import.
	 * @param rs	A result set request for a given price import. 
	 * @return	The result set for the requested price import.
	 * @throws SQLException	Error from the database.
	 */
	private PriceImport mapResultSetToPriceImport(ResultSet rs) throws SQLException{
		PriceImport importedPrice = new PriceImport();
		Timestamp ts = rs.getTimestamp("import_date");
		importedPrice.setImportId(rs.getInt("importID"));
		importedPrice.setSupplierId(rs.getInt("supplierID"));
		importedPrice.setImportedBy(rs.getInt("imported_by"));
		importedPrice.setImportDate(ts == null ? null : ts.toLocalDateTime());
		importedPrice.setFileName(rs.getString("file_name"));
		importedPrice.setRowsProcessed(rs.getInt("rows_processed"));
		importedPrice.setRowsUpdated(rs.getInt("rows_updated"));
		importedPrice.setRowsSkipped(rs.getInt("rows_skipped"));
		importedPrice.setNotes(rs.getString("notes"));
		return importedPrice;
	}

}
