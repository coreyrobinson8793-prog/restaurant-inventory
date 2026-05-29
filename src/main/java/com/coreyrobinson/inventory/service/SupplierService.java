/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Handles operations for items linked to their supplier.
 * DATE: 05/28/26
 */
package com.coreyrobinson.inventory.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.coreyrobinson.inventory.dao.ItemSupplierDAO;
import com.coreyrobinson.inventory.dao.SupplierDAO;
import com.coreyrobinson.inventory.model.ItemSupplier;
import com.coreyrobinson.inventory.model.Supplier;

public class SupplierService {

	private final SupplierDAO supplierDao = new SupplierDAO();
	private final ItemSupplierDAO itemSupplierDao = new ItemSupplierDAO();

	/**
	 * Adds a supplier to the supplier menu.
	 * @param name	Name of the supplier.
	 * @param repName	Name of the supplier representative.
	 * @param phone	Supplier's phone number.
	 * @param email	Supplier's email address.
	 * @param address	Supplier's address.
	 * @return	The added supplier.
	 * @throws SQLException	Error from the database.
	 */
	public Supplier addSupplier(String name, String repName, String phone, String email, String address) throws SQLException {
		validateSupplierFields(name);
		Optional<Supplier> existing = supplierDao.findByName(name);
		if (existing.isPresent()) {
			throw new IllegalArgumentException("Supplier " + name + " already exists");
		}
		Supplier supplier = new Supplier();
		supplier.setSupplierName(name);
		supplier.setRepName(repName);
		supplier.setPhone(phone);
		supplier.setEmail(email);
		supplier.setAddress(address);
		supplier.setActive(true);
		return supplierDao.save(supplier);
	}

	/**
	 * Updates a supplier in the supplier menu.
	 * @param supplier	The supplier to update.
	 * @return	The updated supplier.
	 * @throws SQLException	Error from the database.
	 * @throws IllegalArgumentException	If the supplier name already exists for a different supplier.
	 */
	public Supplier updateSupplier(Supplier supplier) throws SQLException {
		validateSupplierFields(supplier.getSupplierName());
		Optional<Supplier> existing = supplierDao.findByName(supplier.getSupplierName());
		if (existing.isPresent() && existing.get().getSupplierId() != supplier.getSupplierId()) {
			throw new IllegalArgumentException("Supplier " + supplier.getSupplierName() + " already exists");
		}
		return supplierDao.save(supplier);
	}
	/**
	 * Finds all suppliers in the application.
	 * @return	All suppliers.
	 * @throws SQLException	Error from the database.
	 */
	public List<Supplier> findAllSuppliers() throws SQLException {
		return supplierDao.findAll();
	}

	/**
	 * Finds a supplier by its I.D.
	 * @param supplierId	The supplier's I.D.
	 * @return	The found supplier.
	 * @throws SQLException	Error from the database.
	 */
	public Optional<Supplier> findSupplierById(int supplierId) throws SQLException {
		return supplierDao.findById(supplierId);
	}

	/**
	 * Deactivates a supplier in the supplier menu.
	 * @param supplierId	The I.D. of the supplier to deactivate.
	 * @throws SQLException	Error from the database.
	 * @throws IllegalArgumentException	If the supplier does not exist.
	 */
	public void deactivateSupplier(int supplierId) throws SQLException {
		Optional<Supplier> optSupplier = supplierDao.findById(supplierId);
		if (optSupplier.isEmpty()) {
			throw new IllegalArgumentException("Supplier with ID " + supplierId + " does not exist");
		}
		supplierDao.deactivate(supplierId);
	}

	/**
	 * Links an item with a supplier.
	 * @param itemId	I.D. of the item.
	 * @param supplierId	I.D. of the supplier.
	 * @param supplierSku	Sku number of the item.
	 * @param price	Price of the item.
	 * @param packSize	Pack size of the item.
	 * @param packUnitId	I.D. of the measurement unit.
	 * @return	The linked item and supplier.
	 * @throws SQLException	Error from the database.
	 */
	public ItemSupplier linkItemToSupplier(int itemId, int supplierId, String supplierSku, BigDecimal price, BigDecimal packSize, 
			int packUnitId) throws SQLException {
		validateItemSupplierFields(price, packSize, supplierSku);
		Optional<ItemSupplier> existing = itemSupplierDao.findByItemAndSupplier(itemId, supplierId);
		if (existing.isPresent()) {
			throw new IllegalArgumentException("Supplier already linked to this item.");
		}		
		ItemSupplier linkItemSupplier = new ItemSupplier();
		linkItemSupplier.setItemId(itemId);
		linkItemSupplier.setSupplierId(supplierId);
		linkItemSupplier.setSupplierSku(supplierSku);
		linkItemSupplier.setPrice(price);
		linkItemSupplier.setPackSize(packSize);
		linkItemSupplier.setPackUnitId(packUnitId);
		linkItemSupplier.setPreferred(false);
		linkItemSupplier.setActive(true);
		linkItemSupplier.setLastPriceUpdate(LocalDateTime.now());
		return itemSupplierDao.save(linkItemSupplier);
	}

	/**
	 * Finds all suppliers for a certain item.
	 * @param itemId	The item.
	 * @return	The list of suppliers for that item.
	 * @throws SQLException	Error from the database.
	 */
	public List<ItemSupplier> findSuppliersForItem(int itemId) throws SQLException {
		return itemSupplierDao.findByItemId(itemId);
	}

	/**
	 * Finds all items for a certain supplier.
	 * @param supplierId	The supplier.
	 * @return	The list of items for that supplier.
	 * @throws SQLException	Error from the database.
	 */
	public List<ItemSupplier> findItemsForSupplier(int supplierId) throws SQLException {
		return itemSupplierDao.findBySupplierId(supplierId);
	}

	/**
	 * Deactivates the linked relationship from item to supplier.
	 * @param itemSupplierId	The item linked to the supplier to deactivate.
	 * @throws SQLException	Error from the database.
	 */
	public void deactivateItemSupplier(int itemSupplierId) throws SQLException {
		Optional<ItemSupplier> optItemSupplier = itemSupplierDao.findById(itemSupplierId);
		if (optItemSupplier.isEmpty()) {
			throw new IllegalArgumentException("This item isn't linked to this supplier or doesn't exist");
		}
		itemSupplierDao.deactivate(itemSupplierId);
	}

	/**
	 * Updates a linked item supplier.
	 * @param itemSupplier	The linked item supplier to update.
	 * @return	The updated linked item supplier.
	 * @throws SQLException	Error from the database.
	 */
	public ItemSupplier updateItemSupplier(ItemSupplier itemSupplier) throws SQLException {
		validateItemSupplierFields(itemSupplier.getPrice(), itemSupplier.getPackSize(), itemSupplier.getSupplierSku());
		Optional<ItemSupplier> existing = itemSupplierDao.findById(itemSupplier.getItemSupplierId());
		if (existing.isEmpty()) {
			throw new IllegalArgumentException("Item-supplier link doesn't exist");
		}
		if (existing.get().getPrice().compareTo(itemSupplier.getPrice()) != 0) {
			itemSupplier.setLastPriceUpdate(LocalDateTime.now());
		}
		return itemSupplierDao.save(itemSupplier);
	}
	
	/**
	 * Sets a preferred supplier for an item.
	 * @param itemId	The item.
	 * @param itemSupplierId	The item-supplier.
	 * @throws SQLException	Error from the database.
	 */
	public void setPreferredSupplier(int itemId, int itemSupplierId) throws SQLException {
		Optional<ItemSupplier> itemSupplier = itemSupplierDao.findById(itemSupplierId);
		if (itemSupplier.isEmpty()) {
			throw new IllegalArgumentException("Supplier doesn't exist");
		}
		if (itemSupplier.get().getItemId() != itemId) {
			throw new IllegalArgumentException("Supplier doesn't belong to this item");
		}
		for (ItemSupplier supplier : itemSupplierDao.findByItemId(itemId)) {
			if (supplier.getItemSupplierId() != itemSupplierId) {
				supplier.setPreferred(false);
				itemSupplierDao.save(supplier);
			} else {
				supplier.setPreferred(true);
				itemSupplierDao.save(supplier);
			}
		}
	}

	/**
	 * Validates the linkItemToSupplier fields.
	 * @param price	The linked items price.
	 * @param packSize	The linked items pack size.
	 * @param supplierSku	The linked items sku number.
	 * @throws IllegalArgumentException	If the price or pack size is negative, or the sku number is blank.
	 */
	private void validateItemSupplierFields(BigDecimal price, BigDecimal packSize, String supplierSku) {
		if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Price cannot be negative");
		}
		if (packSize == null || packSize.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Pack size cannot be negative");
		}
		if (supplierSku == null || supplierSku.isBlank()) {
			throw new IllegalArgumentException("Must have a sku number");
		}		
	}

	/**
	 * Validates supplier fields.
	 * @param name	The supplier's name.
	 * @throws IllegalArgumentException	If the supplier name is null or blank.
	 */
	private void validateSupplierFields(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Supplier must have a name");
		}
	}

}
