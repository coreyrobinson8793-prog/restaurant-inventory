package com.coreyrobinson.inventory.app;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.coreyrobinson.inventory.dao.ItemDAO;
import com.coreyrobinson.inventory.model.Item;

public class ItemDAOTest {

	public static void main(String[] args) {
		ItemDAO itemDAO = new ItemDAO();
		
		try {
			String testItem = "test_item_" + System.currentTimeMillis();
			// Insert
			Item newItem = new Item();
			newItem.setItemName(testItem);
			newItem.setUnitId(1);
			newItem.setCategoryId(1);
			newItem.setCurrentStock(new BigDecimal("0"));
			newItem.setParstock(new BigDecimal("10"));
			newItem.setActive(true);
			Item saved = itemDAO.save(newItem);
			System.out.println("Inserted Item ID: " + saved.getItemId());
			// Find by ID
			// Test 2: real findById
			Optional<Item> fetched = itemDAO.findById(saved.getItemId());
			if (fetched.isPresent()) {
			    Item f = fetched.get();
			    System.out.println("Fetched item I.D.: " + f.getItemName() + " | stock=" + f.getCurrentStock() + " | par=" + f.getParstock());
			} else {
			    System.out.println("FAIL: Item not found after insert");
			}
			Optional<Item> fetchedName = itemDAO.findByName(saved.getItemName());
			if (fetchedName.isPresent()) {
			    Item fn = fetchedName.get();
			    System.out.println("Fetched item name: " + fn.getItemName() + " | stock=" + fn.getCurrentStock() + " | par=" + fn.getParstock());
			} else {
			    System.out.println("FAIL: Item not found after insert");
			}
			List<Item> items = itemDAO.findAll();
			System.out.println("Total items: " + items.size());
			saved.setCurrentStock(new BigDecimal("60"));
			saved.setParstock(new BigDecimal("30"));
			itemDAO.save(saved);
			Optional<Item> newFetch = itemDAO.findById(saved.getItemId());
			if (newFetch.isPresent()) {
				Item nf = newFetch.get();
				System.out.println("Updated Item: " + nf.getItemName() + " | stock=" + nf.getCurrentStock() + " | par=" + nf.getParstock());
			} else {
				System.out.println("FAIL: Item not found after update.");
			}
			itemDAO.deactivate(saved.getItemId());
			Optional<Item> deactivated = itemDAO.findById(saved.getItemId());
			if (deactivated.isPresent()) {
			    Item d = deactivated.get();
			    System.out.println("After deactivate - Active: " + d.isActive());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

}
