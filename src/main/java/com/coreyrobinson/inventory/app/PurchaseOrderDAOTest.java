package com.coreyrobinson.inventory.app;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.coreyrobinson.inventory.dao.PurchaseOrderDAO;
import com.coreyrobinson.inventory.model.PurchaseOrder;

public class PurchaseOrderDAOTest {

	public static void main(String[] args) {
		PurchaseOrderDAO poDao = new PurchaseOrderDAO();
		int testUserId = 1;
		int testSuppId = 1;
		int testSuppId2 = 2;
		try {
			// insert
			PurchaseOrder newPo = new PurchaseOrder();
			newPo.setSupplierId(testSuppId);
			newPo.setCreatedBy(testUserId);
			newPo.setCreatedDate(LocalDateTime.now());
			newPo.setCurrentStatus("OPEN");
			newPo.setTotal(new BigDecimal("0"));
			newPo.setNotes("This is a test");
			PurchaseOrder saved = poDao.save(newPo);
			System.out.println("New PO inserted. Supplier I.D.: " + saved.getSupplierId() + " | User I.D.: " + saved.getCreatedBy() +
					" | PO I.D.: " + saved.getPoId() + " | Created at: " + saved.getCreatedDate() + " | Current Status: " + saved.getCurrentStatus() +
					" | Total: " + saved.getTotal() + " | Notes: " + saved.getNotes());
			// find by I.D.
			Optional<PurchaseOrder> fetched = poDao.findById(saved.getPoId());
			if (fetched.isPresent()) {
				PurchaseOrder testPo = fetched.get();
				System.out.println("Fetched PO: " + testPo.getPoId() + " | Supplier I.D.: " + testPo.getSupplierId() + " | User I.D.: " + 
				testPo.getCreatedBy() + " | Created At: " + testPo.getCreatedDate() + " | Current Status :" + testPo.getCurrentStatus() + 
				" | Total: " + testPo.getTotal() + " | Notes: " + testPo.getNotes());
			} else {
				System.out.println("Fetched PO I.D. cannot be found.");
			}
			// find by status OPEN
			List<PurchaseOrder> openPo = poDao.findByStatus("OPEN");
			System.out.println("OPEN POs: " + openPo.size());
			// find by status RECEIVED (should be empty)
			List<PurchaseOrder> recPo = poDao.findByStatus("RECEIVED");
			System.out.println("RECEIVED POs: " + recPo.size());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

}
