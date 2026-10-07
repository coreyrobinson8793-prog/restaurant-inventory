/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Handles the operations for a unit of measurement.
 * DATE: 09/25/2026
 */
package com.coreyrobinson.inventory.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.coreyrobinson.inventory.dao.UnitDAO;
import com.coreyrobinson.inventory.model.Unit;

public class UnitService {

	private final UnitDAO unitDao = new UnitDAO();

	/**
	 * Finds all units of measurement in the database.
	 * @return	A list of all units of measurement.
	 * @throws SQLException	Error from the database.
	 */
	public List<Unit> findAllUnits() throws SQLException {
		return unitDao.findAll();
	}

	/**
	 * Adds a new unit of measurement to the database.
	 * @param unitName	The name of the unit of measurement.
	 * @return	The added unit of measurement.
	 * @throws SQLException	Error from the database.
	 * @throws IllegalArgumentException If the unit name is empty or already exists.
	 */
	public Unit addUnit(String unitName) throws SQLException {
		if (unitName == null || unitName.isBlank()) {
			throw new IllegalArgumentException("Unit name cannot be empty");
		}
		unitName = unitName.trim();
		if (unitDao.findByName(unitName).isPresent()) {
			throw new IllegalArgumentException("Unit name '" + unitName + "' already exists");
		}
		Unit unit = new Unit(unitName);
		return unitDao.save(unit);
	}

	/**
	 * Updates a unit of measurement's name in the database.
	 * @param unitId	The unit of measurement's I.D.
	 * @param newName	The new name for the unit of measurement.
	 * @return	The updated unit of measurement.
	 * @throws SQLException	Error from the database.
	 * @throws IllegalArgumentException If the new name is empty or already exists.
	 */
	public Unit updateUnit(int unitId, String newName) throws SQLException {
		if (newName == null || newName.isBlank()) {
			throw new IllegalArgumentException("Unit name cannot be empty");
		}
		newName = newName.trim();
		Optional<Unit> existingUnit = unitDao.findByName(newName);
		if (existingUnit.isPresent() && existingUnit.get().getUnitId() != unitId) {
			throw new IllegalArgumentException("Unit name '" + newName + "' already exists");
		}
		Unit unit = unitDao.findById(unitId).orElseThrow(() -> new IllegalArgumentException("Unit with ID " + unitId + " not found"));
		unit.setUnitName(newName);
		return unitDao.save(unit);
	}
}
