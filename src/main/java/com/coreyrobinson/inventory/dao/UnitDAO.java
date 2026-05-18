/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Data access object for the unit model. Handles all database operations for units of measurement.
 * DATE: 05/16/26
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

import com.coreyrobinson.inventory.model.Unit;
import com.coreyrobinson.inventory.util.DatabaseConnection;

public class UnitDAO {
	
	/**
	 * Finds a unit of measurement by I.D.
	 * @param unitId	The unit of measurement's I.D.
	 * @return	The unit of measurement.
	 * @throws SQLException	Error from the database.
	 */
	public Optional<Unit> findById(int unitId) throws SQLException {
		String sql = "SELECT unitID, unitName FROM units_of_measurement WHERE unitID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, unitId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapResultSetToUnit(rs));
				}
				return Optional.empty();
			}
		}
	}
	
	/**
	 * Finds a unit of measurement by its name.
	 * @param unitName	The unit of measurement's name.
	 * @return	The unit of measurement.
	 * @throws SQLException	Error from the database.
	 */
	public Optional<Unit> findByName(String unitName) throws SQLException {
		String sql = "SELECT unitID, unitName FROM units_of_measurement WHERE unitName = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, unitName);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapResultSetToUnit(rs));
				}
				return Optional.empty();
			}
		}
	}
	
	/**
	 * Creates a list of all units of measurement.
	 * @return	The list of units of measurement.
	 * @throws SQLException	Error from the database.
	 */
	public List<Unit> findAll() throws SQLException {
		String sql = "SELECT unitID, unitName FROM units_of_measurement";
		List<Unit> units = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				units.add(mapResultSetToUnit(rs));
			}
		}
		return units;
	}
	
	/**
	 * Either saves a new unit to the database, or updates a unit's information in the database.
	 * @param unit	The unit to add or update.
	 * @return	The new or updated unit.
	 * @throws SQLException Error from the database.
	 */
	public Unit save(Unit unit) throws SQLException {
		if (unit.getUnitId() == 0) {
			return insert(unit);
		}
		return update(unit);
	}

	/**
	 * Updates an existing unit of measurement in the database.
	 * @param unit	Unit of measurement to update.
	 * @return	The updated unit of measurement.
	 * @throws SQLException	Error from the database.
	 */
	private Unit update(Unit unit) throws SQLException{
		String sql = "UPDATE units_of_measurement SET unitName = ? WHERE unitID = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, unit.getUnitName());
			stmt.setInt(2, unit.getUnitId());
			stmt.executeUpdate();
		}
		return unit;
	}

	/**
	 * Inserts a new unit of measure into the database.
	 * @param unit	New unit of measurement to insert.
	 * @return	Inserted unit of measurement.
	 * @throws SQLException	Error from the database.
	 */
	private Unit insert(Unit unit) throws SQLException {
		String sql = "INSERT INTO units_of_measurement (unitName) VALUES (?)";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, unit.getUnitName());
			stmt.executeUpdate();
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
			    if (generatedKeys.next()) {
			        unit.setUnitId(generatedKeys.getInt(1));
			    }
			}
		}
		return unit;
	}

	/**
	 * Assigns values to a unit of measurement.
	 * @param rs	A result set request for a given unit of measurement.
	 * @return	The result set for the requested unit of measurement.
	 * @throws SQLException	Error from the database.
	 */
	private Unit mapResultSetToUnit(ResultSet rs) throws SQLException {
		Unit unit = new Unit();
		unit.setUnitId(rs.getInt("unitID"));
		unit.setUnitName(rs.getString("unitName"));
		return unit;
	}

}
