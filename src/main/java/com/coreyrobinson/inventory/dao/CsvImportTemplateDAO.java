/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Data access object for CSV imports. Handles database operations.
 * DATE: 07/13/26
 */
package com.coreyrobinson.inventory.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.coreyrobinson.inventory.model.CsvImportTemplate;
import com.coreyrobinson.inventory.util.DatabaseConnection;

public class CsvImportTemplateDAO {
	
	/**
	 * Finds CSV import template by template I.D.
	 * @param templatedId	Template's I.D.	
	 * @return	The imported CSV
	 * @throws SQLException	Error from the database.
	 */
	public Optional<CsvImportTemplate> findById(int templateId) throws SQLException {
		String sql = "SELECT templateID, supplierID, sku_column_name, price_column_name, header_row_number, created_by, created_at "
				+ "FROM csv_import_templates WHERE templateID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, templateId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapResultSetToCsvImportTemplate(rs));
				}
				return Optional.empty();
			}
		}
	}
	
	/**
	 * Finds CSV import template by supplier I.D.
	 * @param supplierId	Supplier's I.D.
	 * @return	The imported CSV
	 * @throws SQLException	Error from the database.
	 */
	public Optional<CsvImportTemplate> findBySupplierId(int supplierId) throws SQLException {
		String sql = "SELECT templateID, supplierID, sku_column_name, price_column_name, header_row_number, created_by, created_at "
				+ "FROM csv_import_templates WHERE supplierID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, supplierId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapResultSetToCsvImportTemplate(rs));
				}
				return Optional.empty();
			}
		}
	}
	
	/**
	 * Creates list of all CSV import templates.
	 * @return	The list of CSV import templates.
	 * @throws SQLException	Error from the database.
	 */
	public List<CsvImportTemplate> findAll() throws SQLException {
		String sql = "SELECT templateID, supplierID, sku_column_name, price_column_name, header_row_number, created_by, created_at "
				+ "FROM csv_import_templates";
		List<CsvImportTemplate> csvImports = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				csvImports.add(mapResultSetToCsvImportTemplate(rs));
			}
		}
		return csvImports;
	}
	
	/**
	 * Either saves a new CSV import template to the database, or updates an existing CSV import template.
	 * @param csvImport	The CSV import template to insert or update.
	 * @return	The inserted or updated CSV import template.
	 * @throws SQLException	Error from the database.
	 */
	public CsvImportTemplate save(CsvImportTemplate csvImport) throws SQLException {
		if (csvImport.getTemplateId() == 0) {
			return insert(csvImport);
		}
		return update(csvImport);
	}
	
	/**
	 * Deletes a CSV import template.
	 * @param templateId	The CSV to delete.
	 * @throws SQLException	Error from the database.
	 */
	public void delete(int templateId) throws SQLException {
		String sql = "DELETE FROM csv_import_templates WHERE templateID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, templateId);
			stmt.executeUpdate();
		}
	}

	/**
	 * Updates an existing CSV template.
	 * @param csvImport	The CSV to update.
	 * @return	The updated CSV.
	 * @throws SQLException	Error from the database.
	 */
	private CsvImportTemplate update(CsvImportTemplate csvImport) throws SQLException {
		String sql = "UPDATE csv_import_templates SET supplierID = ?, sku_column_name = ?, price_column_name = ?, header_row_number = ? "
				+ "WHERE templateID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, csvImport.getSupplierId());
			stmt.setString(2, csvImport.getSkuColumnName());
			stmt.setString(3, csvImport.getPriceColumnName());
			stmt.setInt(4, csvImport.getHeaderRowNumber());
			stmt.setInt(5, csvImport.getTemplateId());
			stmt.executeUpdate();
		}
		return csvImport;
	}

	/**
	 * Inserts a CSV template into the database.
	 * @param csvImport	The CSV to insert.
	 * @return	The inserted CSV.
	 * @throws SQLException	Error from the database.
	 */
	private CsvImportTemplate insert(CsvImportTemplate csvImport) throws SQLException {
		String sql = "INSERT INTO csv_import_templates (supplierID, sku_column_name, price_column_name, header_row_number, created_by) "
				+ "VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, csvImport.getSupplierId());
			stmt.setString(2, csvImport.getSkuColumnName());
			stmt.setString(3, csvImport.getPriceColumnName());
			stmt.setInt(4, csvImport.getHeaderRowNumber());
			stmt.setInt(5, csvImport.getCreatedBy());
			stmt.executeUpdate();
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					csvImport.setTemplateId(generatedKeys.getInt(1));
				}
			}
		}
		return csvImport;
	}

	/**
	 * Assigns values to a CSV template.
	 * @param rs	A result set request for a given CSV template.
	 * @return	The result set for the requested CSV template.
	 * @throws SQLException	Error from the database.
	 */
	private CsvImportTemplate mapResultSetToCsvImportTemplate(ResultSet rs) throws SQLException {
		CsvImportTemplate csvImport = new CsvImportTemplate();
		Timestamp ts = rs.getTimestamp("created_at");
		csvImport.setTemplateId(rs.getInt("templateID"));
		csvImport.setSupplierId(rs.getInt("supplierID"));
		csvImport.setSkuColumnName(rs.getString("sku_column_name"));
		csvImport.setPriceColumnName(rs.getString("price_column_name"));
		csvImport.setHeaderRowNumber(rs.getInt("header_row_number"));
		csvImport.setCreatedBy(rs.getInt("created_by"));
		csvImport.setCreatedAt(ts == null ? null : ts.toLocalDateTime());
		return csvImport;
	}

}
