/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Handles operations for items.
 * DATE: 05/15/2026
 */
package com.coreyrobinson.inventory.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.coreyrobinson.inventory.dao.ItemDAO;
import com.coreyrobinson.inventory.model.Item;

public class ItemService {
	
	private final ItemDAO itemDao = new ItemDAO();
	
	/**
	 * Adds an item to the inventory manager.
	 * @param name	Name of the item.
	 * @param unitId	I.D. of the unit of measurement.
	 * @param currentStock	How many of the item currently on hand.
	 * @param parstock	How many of the item are required to be on hand.
	 * @param categoryId	I.D. for the category of the item.
	 * @return	The item that was added to the inventory manager.
	 * @throws SQLException	Error from the database.
	 * @throws IllegalArgumentException	If the item name already exists.
	 */
	public Item addItem(String name, int unitId, BigDecimal currentStock, BigDecimal parstock, int categoryId) throws SQLException {
		validateItemFields(name, currentStock, parstock);
		Optional<Item> existing = itemDao.findByName(name);
		if (existing.isPresent()) {
			throw new IllegalArgumentException("Item " + name + " already exists");
		}
		Item item = new Item();
		item.setItemName(name);
		item.setUnitId(unitId);
		item.setCurrentStock(currentStock);
		item.setParstock(parstock);
		item.setCategoryId(categoryId);
		item.setActive(true);
		return itemDao.save(item);
	}
	
	/**
	 * Finds all items in the application.
	 * @return	All items.
	 * @throws SQLException	Error from the database.
	 */
	public List<Item> findAllItems() throws SQLException {
		return itemDao.findAll();
	}
	
	/**
	 * Finds an item by its I.D.
	 * @param id	The item's I.D.
	 * @return	The found item.
	 * @throws SQLException	Error from the database.
	 */
	public Optional<Item> findItemById(int id) throws SQLException {
		return itemDao.findById(id);
	}
	
	/**
	 * Updates an item in the application.
	 * @param item	The item to update.
	 * @return	The item after the update.
	 * @throws SQLException	Error from the database.
	 * @throws IllegalArgumentException If validation fails or the item name already exists.
	 */
	public Item updateItem(Item item) throws SQLException {
		validateItemFields(item.getItemName(), item.getCurrentStock(), item.getParstock());
		Optional<Item> existing = itemDao.findByName(item.getItemName());
		if (existing.isPresent() && existing.get().getItemId() != item.getItemId()) {
			throw new IllegalArgumentException("Item " + item.getItemName() + " already exists");
		}
		return itemDao.save(item);
	}
	
	/**
	 * Deactivates an item.
	 * @param itemId	The item.
	 * @throws SQLException	Error from the database.
	 * @throws IllegalArgumentException	If an item is in an open PO.
	 */
	public void deactivateItem(int itemId) throws SQLException {
		Optional<Item> optItem = itemDao.findById(itemId);
		if (optItem.isEmpty()) {
			throw new IllegalArgumentException("Item not found");
		}
		if (isItemOnOpenPurchaseOrder(itemId)) {
			throw new IllegalArgumentException("Cannot deactivate an item that is on an open purchase order");
		}
		itemDao.deactivate(itemId);
	}

	/**
	 * Checks for proper fields for the item.
	 * @param name	Name of the item.
	 * @param currentStock	How many of the item currently on hand.
	 * @param parstock	How many of the item are required to be on hand.
	 * @throws IllegalArgumentException	If validation fails.
	 */
	private void validateItemFields(String name, BigDecimal currentStock, BigDecimal parstock) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Item must have a name");
		}
		if (currentStock == null || currentStock.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Current Stock cannot be less than zero");
		}
		if (parstock == null || parstock.compareTo(BigDecimal.ONE) < 0) {
			throw new IllegalArgumentException("Parstock must be at least one");
		}
	}
	
	/**
	 * Checks if an item is on any open purchase orders.
	 * TODO: Implement when PurchaseOrderDAO is built.
	 * @param itemId	The item's I.D.
	 * @return	True if item is on an open PO, false otherwise.
	 */
	private boolean isItemOnOpenPurchaseOrder(int itemId) {
		// TODO Query open purchase orders for this itemId
		return false;
	}

}
