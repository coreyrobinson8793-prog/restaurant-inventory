package com.coreyrobinson.inventory.app;

import java.sql.SQLException;
import java.util.List;

import com.coreyrobinson.inventory.dao.PriceImportDAO;
import com.coreyrobinson.inventory.model.PriceImport;

public class PriceImportDAOTest {

	public static void main(String[] args) {
		PriceImportDAO priceImportTestDao = new PriceImportDAO();
		int testSupplier = 1;
		int testImportedBy = 1;
		String testFileName = "test.csv";
		int testRowsProcessed = 10;
		int testRowsUpdated = 8;
		int testRowsSkipped = 2;
		String testNotes = "test";
		
		try {
			// insert
			PriceImport newPriceImport = new PriceImport();
			newPriceImport.setSupplierId(testSupplier);
			newPriceImport.setFileName(testFileName);
			newPriceImport.setImportedBy(testImportedBy);
			newPriceImport.setNotes(testNotes);
			newPriceImport.setRowsProcessed(testRowsProcessed);
			newPriceImport.setRowsSkipped(testRowsSkipped);
			newPriceImport.setRowsUpdated(testRowsUpdated);
			PriceImport saved = priceImportTestDao.save(newPriceImport);
			System.out.println("New Price Import Inserted. Import ID: " + saved.getImportId() + " | Supplier ID: " +
			 saved.getSupplierId() + " | File Name: " + saved.getFileName() + " | Imported By: " + saved.getImportedBy() +
			  " | Rows Processed: " + saved.getRowsProcessed() + " | Rows Updated: " + saved.getRowsUpdated() + 
			   " | Rows Skipped: " + saved.getRowsSkipped() + " | Notes: " + saved.getNotes());	
			// find all
			List<PriceImport> fetched = priceImportTestDao.findAll();
			System.out.println("Found " + fetched.size() + " imported prices.");
			for (PriceImport pi : fetched) {
				System.out.println(" | File Name: " + pi.getFileName() +
								   " | Date: " + pi.getImportDate() +
								   " | Imported By: " + pi.getImportedBy());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

}
