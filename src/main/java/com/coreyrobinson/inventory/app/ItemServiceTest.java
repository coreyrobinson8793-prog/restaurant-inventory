package com.coreyrobinson.inventory.app;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.coreyrobinson.inventory.model.Item;
import com.coreyrobinson.inventory.service.ItemService;

public class ItemServiceTest {

	public static void main(String[] args) {
		
		ItemService itemService = new ItemService();
		String testName = "service_test_" + System.currentTimeMillis();
		
		// Happy Path
		try {
			Item added = itemService.addItem(testName, 1, new BigDecimal("5"), new BigDecimal("10"), 1);
			System.out.println("PASS: Added item ID " + added.getItemId());
		} catch (Exception e) {
			System.out.println("FAIL: " + e.getMessage());
		}
		// Duplicate Name
		try {
			itemService.addItem(testName, 1, new BigDecimal("5"), new BigDecimal("10"), 1);
			System.out.println("Test 2 FAIL: Duplicate was allowed");
		} catch (IllegalArgumentException e) {
			System.out.println("Test 2 PASS: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Test 2 FAIL: SQL error - " + e.getMessage());
		}
		// Blank name
		try {
			itemService.addItem("", 1, new BigDecimal("5"), new BigDecimal("10"), 1);
			System.out.println("Test 3 FAIL: Blank name allowed");
		} catch (IllegalArgumentException e) {
			System.out.println("Test 2 PASS: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Test 2 FAIL: SQL error - " + e.getMessage());
		}
		// Negative stock
		try {
			itemService.addItem(testName, 1, new BigDecimal("-5"), new BigDecimal("10"), 1);
			System.out.println("Test 2 FAIL: Negative stock allowed");
		} catch (IllegalArgumentException e) {
			System.out.println("Test 2 PASS: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Test 2 FAIL: SQL error - " + e.getMessage());
		}
		// Parstock zero
		try {
			itemService.addItem(testName, 1, new BigDecimal("5"), new BigDecimal("0"), 1);
			System.out.println("Test 2 FAIL: Parstock less than zero allowed");
		} catch (IllegalArgumentException e) {
			System.out.println("Test 2 PASS: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Test 2 FAIL: SQL error - " + e.getMessage());
		}
		// TODO Auto-generated method stub

	}

}
