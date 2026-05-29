package com.coreyrobinson.inventory.app;

import java.sql.SQLException;

import com.coreyrobinson.inventory.model.Supplier;
import com.coreyrobinson.inventory.service.SupplierService;

public class SupplierServiceTest {

	public static void main(String[] args) {
		
		SupplierService supplierService = new SupplierService();
		String testName = "service_test_" + System.currentTimeMillis();
		// Happy Path
		try {
			Supplier added = supplierService.addSupplier(testName, "Rep Name", "555-555-5555", "test@email.com", "123 Test St");
			System.out.println("PASS: Added supplier ID " + added.getSupplierId());
		} catch (Exception e) {
			System.out.println("FAIL: " + e.getMessage());
		}
		// duplicate name
		try {
			supplierService.addSupplier(testName, "Rep Name", "555-5555", "test@email.com", "123 Test St");
			System.out.println("FAIL: Duplicate name was allowed");
		} catch (IllegalArgumentException e) {
			System.out.println("PASS: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("FAIL: SQL error - " + e.getMessage());
		}
		// TODO Auto-generated method stub

	}

}
