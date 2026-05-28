package com.coreyrobinson.inventory.app;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.coreyrobinson.inventory.dao.ItemSupplierDAO;
import com.coreyrobinson.inventory.model.ItemSupplier;

public class ItemSupplierDAOTest {

	public static void main(String[] args) {
		ItemSupplierDAO itemSupplierDao = new ItemSupplierDAO();
		int testItemId = 8;
		int testSupplierId = 8;
		int testPackUnitId = 1;
		try {
			// insert
			ItemSupplier newItemSupplier = new ItemSupplier();
			newItemSupplier.setItemId(testItemId);
			newItemSupplier.setSupplierId(testSupplierId);
			newItemSupplier.setPackUnitId(testPackUnitId);
			newItemSupplier.setSupplierSku("SKU-001");
			newItemSupplier.setPrice(new BigDecimal("20"));
			newItemSupplier.setPackSize(new BigDecimal("10"));
			newItemSupplier.setPreferred(false);
			newItemSupplier.setActive(true);
			newItemSupplier.setLastPriceUpdate(LocalDateTime.now());
			ItemSupplier saved = itemSupplierDao.save(newItemSupplier);
			System.out.println("New item supplier inserted. Item Supplier I.D.: " + saved.getItemSupplierId() + " | Item I.D.: " + saved.getItemId() + " | Supplier I.D.: " + saved.getSupplierId() + " | Pack Unit I.D.: " + saved.getPackUnitId() +
					" | Supplier Sku: " + saved.getSupplierSku() + " | Price: " + saved.getPrice() + " | Pack Size: " + saved.getPackSize() + " | Preferred: " + saved.isPreferred() + " | Last Updated: " + saved.getLastPriceUpdate());
			// find by item supplier id
			Optional<ItemSupplier> fetched = itemSupplierDao.findById(saved.getItemSupplierId());
			if (fetched.isPresent()) {
				ItemSupplier itSup = fetched.get();
				System.out.println("Fetched item supplier I.D.: " + itSup.getItemSupplierId() + " | Last Updated Price: " + itSup.getLastPriceUpdate());
			} else {
				System.out.println("Fetched item supplier I.D. cannot be found");
			}
			// find by item id
			List<ItemSupplier> forItem = itemSupplierDao.findByItemId(testItemId);
			System.out.println("Found " + forItem.size() + " supplier(s) for item " + testItemId + ":");
			for (ItemSupplier is : forItem) {
				System.out.println("  itemSupplierID=" + is.getItemSupplierId()
				+ " | itemID=" + is.getItemId()
				+ " | supplierID=" + is.getSupplierId()
				+ " | price=" + is.getPrice());
			}
			// find by supplier id
			List<ItemSupplier> forSupplier = itemSupplierDao.findBySupplierId(testSupplierId);
			System.out.println("Found " + forSupplier.size() + " supplier(s) for item " + testSupplierId + ":");
			for (ItemSupplier is : forSupplier) {
				System.out.println(" itemSupplierID=" + is.getItemSupplierId()
				+ " | itemID=" + is.getItemId()
				+ " | supplierID=" + is.getSupplierId()
				+ " | price=" + is.getPrice());
			}
			//find by item and supplier id
			Optional<ItemSupplier> fetchedItSupp = itemSupplierDao.findByItemAndSupplier(testItemId, testSupplierId);
			if (fetchedItSupp.isPresent()) {
				ItemSupplier itSup = fetchedItSupp.get();
				System.out.println(" Fetched item supplier I.D.: " + itSup.getItemSupplierId());
			} else {
				System.out.println("Fetched item supplier I.D. cannot be found");
			}
			// update
			saved.setPrice(new BigDecimal("30"));
			saved.setLastPriceUpdate(LocalDateTime.of(2026, 6, 1, 5, 0));
			itemSupplierDao.save(saved);
			Optional<ItemSupplier> reFetched = itemSupplierDao.findById(saved.getItemSupplierId());
			if (reFetched.isPresent()) {
				ItemSupplier rf = reFetched.get();
				System.out.println("Updated item supplier I.D.: " + rf.getItemSupplierId() + " | updated last price update: " + rf.getLastPriceUpdate());
			}
			// deactivate
			itemSupplierDao.deactivate(saved.getItemSupplierId());
			Optional<ItemSupplier> deactivated = itemSupplierDao.findById(saved.getItemSupplierId());
			if (deactivated.isPresent()) {
				ItemSupplier d = deactivated.get();
				System.out.println("After deactivate - Active: " + d.isActive());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
