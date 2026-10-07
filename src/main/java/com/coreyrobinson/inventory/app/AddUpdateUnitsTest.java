package com.coreyrobinson.inventory.app;

import java.sql.SQLException;

import com.coreyrobinson.inventory.service.UnitService;

public class AddUpdateUnitsTest {

	public static void main(String[] args) {
		UnitService unitService = new UnitService();
		// test adding a new unit with a name that exists
		try {
			unitService.addUnit("gallons");
			System.out.println("FAIL: no exception thrown for existing unit name");
		} catch (IllegalArgumentException e) {
			System.out.println("Success: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Error from the database: " + e.getMessage());
		}
		// test adding a new unit with a name that is empty
		try {
			unitService.addUnit("");
			System.out.println("FAIL: no exception thrown for empty unit name");
		} catch (IllegalArgumentException e) {
			System.out.println("Success: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Error from the database: " + e.getMessage());
		}
		// test updating a unit with its own current name
		try {
			unitService.updateUnit(3, "gallons");
			System.out.println("Success: no exception thrown for updating unit with its own name");
		} catch (IllegalArgumentException e) {
			System.out.println("FAIL: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Error from the database: " + e.getMessage());
		}
		// test updating a unit with a name that exists for another unit
		try {
			unitService.updateUnit(3, "liters");
			System.out.println("FAIL: no exception thrown for existing unit name");
		} catch (IllegalArgumentException e) {
			System.out.println("Success: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Error from the database: " + e.getMessage());
		}
		// test adding a new unit with a valid name
		try {
			unitService.addUnit("pints");
			System.out.println("Success: unit added successfully");
		} catch (IllegalArgumentException e) {
			System.out.println("FAIL: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Error from the database: " + e.getMessage());
		}
	}
}
