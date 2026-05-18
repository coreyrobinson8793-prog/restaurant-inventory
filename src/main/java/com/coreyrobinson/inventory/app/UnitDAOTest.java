package com.coreyrobinson.inventory.app;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.coreyrobinson.inventory.dao.UnitDAO;
import com.coreyrobinson.inventory.model.Unit;

public class UnitDAOTest {

	public static void main(String[] args) {
		
		UnitDAO unitDao = new UnitDAO();
		try {
			// Insert
			String testName = "test_unit_" + System.currentTimeMillis();
			Unit newUnit = new Unit();
			newUnit.setUnitName(testName);
			Unit saved = unitDao.save(newUnit);
			System.out.println("Inserted unit ID: " + saved.getUnitId());
			// find by Id
			Optional<Unit> fetched = unitDao.findById(saved.getUnitId()); 
			if (fetched.isPresent()) {
				Unit f = fetched.get();
				System.out.println("Fetched unit name: " + f.getUnitName() + " | I.D.: " + f.getUnitId());
			} else {
				System.out.println("FAIL: Item not found after insert");
			}
			// find by name
			Optional<Unit> fetchedName = unitDao.findByName(saved.getUnitName());
			if (fetchedName.isPresent()) {
				Unit fn = fetchedName.get();
				System.out.println("Fetched unit name: " + fn.getUnitName());
			} else {
				System.out.println("Unit not found after insert");
			}
			// Find all
			List<Unit> units = unitDao.findAll();
			System.out.println("All units: " + units.size());
			//update
			saved.setUnitName("upd_" + System.currentTimeMillis());
			unitDao.save(saved);
			Optional<Unit> updated = unitDao.findById(saved.getUnitId());
			if (updated.isPresent()) {
			    Unit u = updated.get();
			    System.out.println("Updated unit -- ID: " + u.getUnitId() + " | name: " + u.getUnitName());
			} else {
			    System.out.println("FAIL: Unit not found after update");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
