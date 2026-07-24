package com.coreyrobinson.inventory.app;

import java.sql.SQLException;
import java.util.Optional;

import com.coreyrobinson.inventory.dao.CsvImportTemplateDAO;
import com.coreyrobinson.inventory.model.CsvImportTemplate;

public class CsvImportTemplateDAOTest {

	public static void main(String[] args) {
		CsvImportTemplateDAO csvDao = new CsvImportTemplateDAO();
		int testSupplierId = 1;
		String testSku = "SKU";
		String testPrice = "Price";
		int testRowHeader = 1;
		int testCreatedBy = 1;
		
		try {
			// insert
			CsvImportTemplate newCsv = new CsvImportTemplate();
			newCsv.setSupplierId(testSupplierId);
			newCsv.setSkuColumnName(testSku);
			newCsv.setPriceColumnName(testPrice);
			newCsv.setHeaderRowNumber(testRowHeader);
			newCsv.setCreatedBy(testCreatedBy);
			CsvImportTemplate saved = csvDao.save(newCsv);
			System.out.println("New Csv Template Inserted: Template I.D.: " + saved.getTemplateId() + " | Supplier I.D.: " + saved.getSupplierId() + " | SKU: " + saved.getSkuColumnName() + " | Price: " + saved.getPriceColumnName() + 
					" | Row Number: " + saved.getHeaderRowNumber() + " | Created by: " + saved.getCreatedBy() + " | Created date: " + saved.getCreatedAt());
			// find by supplier id
			Optional<CsvImportTemplate> fetched = csvDao.findBySupplierId(saved.getSupplierId());
			if (fetched.isPresent()) {
				CsvImportTemplate fetchedCsv = fetched.get();
				System.out.println("Fetched Csv Template: Template I.D.: " + fetchedCsv.getTemplateId() + " | Supplier I.D.: " + fetchedCsv.getSupplierId() + " | SKU: " + fetchedCsv.getSkuColumnName() + " | Price: " + fetchedCsv.getPriceColumnName() + 
						" | Row Number: " + fetchedCsv.getHeaderRowNumber() + " | Created by: " + fetchedCsv.getCreatedBy() + " | Created date: " + fetchedCsv.getCreatedAt());
			} else {
				System.out.println("Fetched Csv cannot be found");
			}
			// UNI constraint
			try {
				CsvImportTemplate duplicateCsv = new CsvImportTemplate();
				duplicateCsv.setSupplierId(testSupplierId);
				duplicateCsv.setSkuColumnName(testSku);
				duplicateCsv.setPriceColumnName(testPrice);
				duplicateCsv.setHeaderRowNumber(testRowHeader);
				duplicateCsv.setCreatedBy(testCreatedBy);
				CsvImportTemplate savedDuplicate = csvDao.save(duplicateCsv);
				System.out.println("FAIL Duplicate Csv Template Inserted: Template I.D.: " + duplicateCsv.getTemplateId() + " | Supplier I.D.: " + duplicateCsv.getSupplierId() + " | SKU: " + duplicateCsv.getSkuColumnName() + " | Price: " + duplicateCsv.getPriceColumnName() + 
						" | Row Number: " + duplicateCsv.getHeaderRowNumber() + " | Created by: " + duplicateCsv.getCreatedBy() + " | Created date: " + duplicateCsv.getCreatedAt());
			} catch (SQLException e) {
				System.out.println("PASS Catches duplicate CSV Template");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

}
