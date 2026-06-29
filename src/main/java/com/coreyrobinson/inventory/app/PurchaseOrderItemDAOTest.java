package com.coreyrobinson.inventory.app;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.coreyrobinson.inventory.dao.PurchaseOrderItemDAO;
import com.coreyrobinson.inventory.model.PurchaseOrderItem;

public class PurchaseOrderItemDAOTest {

	public static void main(String[] args) {
		PurchaseOrderItemDAO poItemDao = new PurchaseOrderItemDAO();
		int testPoId = 3;
		int testItemId = 1;
		
		try {
			// insert
			PurchaseOrderItem testPoItem = new PurchaseOrderItem();
			testPoItem.setPoId(testPoId);
			testPoItem.setItemId(testItemId);
			testPoItem.setQuantityOrdered(new BigDecimal("5"));
			testPoItem.setPriceAtTime(new BigDecimal("65.50"));
			PurchaseOrderItem saved = poItemDao.save(testPoItem);
			System.out.println("New purchase order line item inserted. PO I.D.: " + saved.getPoId() + " | Item I.D.: " + saved.getItemId() + " | Quantity: " + saved.getQuantityOrdered() + " | Price: " + saved.getPriceAtTime());
			// find by purchase order I.D.
			List<PurchaseOrderItem> fetched = poItemDao.findByPurchaseOrderId(saved.getPoId());
			System.out.println("Found " + fetched.size() + " items for this purchase order: " + testPoId + ". Here are the items: ");
			for (PurchaseOrderItem poItemId : fetched) {
				System.out.println(" | itemID=" + poItemId.getItemId() +
								   " | quantity ordered: " + poItemId.getQuantityOrdered() +
								   " | price: " + poItemId.getPriceAtTime());
			}
			//find by item I.D.
			Optional<PurchaseOrderItem> forItem = poItemDao.findById(saved.getPoItemId());
			if (forItem.isPresent()) {
				PurchaseOrderItem poItem = forItem.get();
				System.out.println("Fetched inserted line item found by its item I.D.: " + poItem.getItemId() + " | quantity: " + poItem.getQuantityOrdered() + " | price: " + poItem.getPriceAtTime());
			} else {
				System.out.println("Inserted line item cannot be found by its item I.D.");
			}
			//delete
			poItemDao.delete(saved.getPoItemId());
			Optional<PurchaseOrderItem> afterDelete = poItemDao.findById(saved.getPoItemId());
			if (afterDelete.isEmpty()) {
				System.out.println("Deleted line item " + saved.getPoItemId());
			} else {
				System.out.println("Delte FAILED");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

}
