package com.coreyrobinson.inventory.app;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

import com.coreyrobinson.inventory.dao.PurchaseOrderDAO;
import com.coreyrobinson.inventory.model.PurchaseOrder;
import com.coreyrobinson.inventory.model.PurchaseOrderItem;
import com.coreyrobinson.inventory.service.ItemService;
import com.coreyrobinson.inventory.service.PurchaseOrderService;

public class PurchaseOrderServiceTest {

	public static void main(String[] args) {
	    PurchaseOrderService poService = new PurchaseOrderService();
	    ItemService itemService = new ItemService();	    
	    int testSupplierId = 1;
	    int testCreatedBy = 1;
	    int testItemId = 1;

	    try {
	        // Create a new purchase order
	        PurchaseOrder addedPo = poService.createPurchaseOrder(testSupplierId, testCreatedBy, "service test");
	        System.out.println("PASS: Added PO I.D.: " + addedPo.getPoId() + " | Status: " + addedPo.getCurrentStatus() +
	                " | Total: " + addedPo.getTotal());

	        // Add a line item
	        PurchaseOrderItem addedLineItem = poService.addLineItem(addedPo.getPoId(), testItemId, new BigDecimal("3"));
	        System.out.println("PASS: Added Line Item I.D.: " + addedLineItem.getItemId() + " | Quantity: " + addedLineItem.getQuantityOrdered() +
	                " | Price: " + addedLineItem.getPriceAtTime());

	        // Verify total update
	        PurchaseOrder updatedPo = poService.findPurchaseOrderById(addedPo.getPoId()).get();
	        System.out.println("PASS: Total (should be 3 x price): " + updatedPo.getTotal());

	        // Rejection test: uncarried item (nested so it doesn't abort the rest)
	        try {
	            poService.addLineItem(addedPo.getPoId(), 999, new BigDecimal("1"));
	            System.out.println("FAIL: expected rejection for uncarried item");
	        } catch (IllegalArgumentException e) {
	            System.out.println("PASS (expected): " + e.getMessage());
	        }

	        // Receive the purchase order
	        poService.receivePurchaseOrder(addedPo.getPoId());
	        PurchaseOrder received = poService.findPurchaseOrderById(addedPo.getPoId()).get();
	        if ("RECEIVED".equals(received.getCurrentStatus())) {
	            System.out.println("PASS: PO " + addedPo.getPoId() + " is now RECEIVED");
	        } else {
	            System.out.println("FAIL: expected RECEIVED, got " + received.getCurrentStatus());
	        }

	        // Rejection test: add to a RECEIVED order
	        try {
	            poService.addLineItem(addedPo.getPoId(), testItemId, new BigDecimal("1"));
	            System.out.println("FAIL: adding to a RECEIVED order should be rejected");
	        } catch (IllegalArgumentException e) {
	            System.out.println("PASS (expected): " + e.getMessage());
	        }

	        // Rejection test: cancel a RECEIVED order
	        try {
	            poService.cancelPurchaseOrder(addedPo.getPoId());
	            System.out.println("FAIL: cancelling a RECEIVED order should be rejected");
	        } catch (IllegalArgumentException e) {
	            System.out.println("PASS (expected): " + e.getMessage());
	        }

	    } catch (IllegalArgumentException e) {
	        System.out.println("FAIL: unexpected rejection - " + e.getMessage());
	    } catch (SQLException e) {
	        System.out.println("FAIL: SQL error - " + e.getMessage());
	    }
	 // Cross-service rule: can't deactivate an item that's on an open PO
	    try {
	        PurchaseOrder openPo = poService.createPurchaseOrder(testSupplierId, testCreatedBy, "deactivation test");
	        poService.addLineItem(openPo.getPoId(), testItemId, new BigDecimal("1"));
	        // openPo is OPEN and now has item 1 on it

	        itemService.deactivateItem(testItemId);
	        System.out.println("FAIL: deactivating an item on an open PO should be rejected");
	    } catch (IllegalArgumentException e) {
	        System.out.println("PASS (expected): " + e.getMessage());
	    } catch (SQLException e) {
	        System.out.println("FAIL: SQL error - " + e.getMessage());
	    }
	}

}
