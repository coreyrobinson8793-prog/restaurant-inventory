/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Handles operations for importing prices.
 * DATE: 07/24/26
 */
package com.coreyrobinson.inventory.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.coreyrobinson.inventory.dao.CsvImportTemplateDAO;
import com.coreyrobinson.inventory.dao.ItemSupplierDAO;
import com.coreyrobinson.inventory.dao.PriceImportDAO;
import com.coreyrobinson.inventory.model.CsvImportTemplate;
import com.coreyrobinson.inventory.model.ItemSupplier;
import com.coreyrobinson.inventory.model.PriceImport;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class PriceImportService {

	private final CsvImportTemplateDAO csvDao = new CsvImportTemplateDAO();
	private final PriceImportDAO priceImportDao = new PriceImportDAO();
	private final ItemSupplierDAO itemSupplierDao = new ItemSupplierDAO();
	private final SupplierService supplierService = new SupplierService();

	/**
	 * Either inserts a new Csv template or updates an existing Csv template.
	 * @param supplierId	Supplier's I.D.
	 * @param skuColumnName	The sku number.
	 * @param priceColumnName	The price of the item.
	 * @param headerRowNumber	The number of the column for the header in the Csv sheet.
	 * @param createdBy	Who imported the Csv.
	 * @return	The new or updated Csv template.
	 * @throws SQLException	Error from the database.
	 */
	public CsvImportTemplate saveTemplate(int supplierId, String skuColumnName, String priceColumnName, int headerRowNumber, int createdBy) throws SQLException {
		if (skuColumnName == null || skuColumnName.isBlank()) {
			throw new IllegalArgumentException("SKU column name is required");
		}
		if (priceColumnName == null || priceColumnName.isBlank()) {
			throw new IllegalArgumentException("Price column name is required");
		}
		if (headerRowNumber < 1) {
			throw new IllegalArgumentException("Header row number must at least be 1");
		}
		Optional<CsvImportTemplate> csvImport = csvDao.findBySupplierId(supplierId);
		if (csvImport.isEmpty()) {
			CsvImportTemplate newCsvImport = new CsvImportTemplate();
			newCsvImport.setSupplierId(supplierId);
			newCsvImport.setSkuColumnName(skuColumnName);
			newCsvImport.setPriceColumnName(priceColumnName);
			newCsvImport.setHeaderRowNumber(headerRowNumber);
			newCsvImport.setCreatedBy(createdBy);
			return csvDao.save(newCsvImport);
		}
		CsvImportTemplate existingCsv = csvImport.get();
		existingCsv.setSkuColumnName(skuColumnName);
		existingCsv.setPriceColumnName(priceColumnName);
		existingCsv.setHeaderRowNumber(headerRowNumber);
		csvDao.save(existingCsv);
		return existingCsv;
	}

	/**
	 * Imports prices from a CSV file.
	 * @param csvFile	The CSV file to import.
	 * @param supplierId	The supplier's I.D.
	 * @param importedBy	Who imported the CSV file.
	 * @return	A PriceImport object with details of the import.
	 * @throws SQLException	Error from the database.
	 * @throws IOException	Error reading the file.
	 * @throws CsvException	Error parsing the CSV file.
	 */
	public PriceImport importPrices(File csvFile, int supplierId, int importedBy) throws SQLException, IOException, CsvException {
		Optional<CsvImportTemplate> csvImport = csvDao.findBySupplierId(supplierId);
		if (csvImport.isEmpty()) {
			throw new IllegalArgumentException("No import template found. Create one first");
		}
		CsvImportTemplate template = csvImport.get();
		List<ItemSupplier> skuNums = itemSupplierDao.findBySupplierId(supplierId);
		skuNums.removeIf(skuNumber -> !skuNumber.isActive());	// removes inactive 
		Map<String, ItemSupplier> skuMap = new HashMap<>();
		for (ItemSupplier skuNum : skuNums) {
			skuMap.put(skuNum.getSupplierSku(), skuNum);
		}
		List<String[]> csvRows;
		try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
			csvRows = reader.readAll();
		}
		if (csvRows.size() < template.getHeaderRowNumber()) {
			throw new IllegalArgumentException("File doesn't have enough rows for header row " + template.getHeaderRowNumber());
		}
		String[] headerRow = csvRows.get(template.getHeaderRowNumber() - 1);
		int skuIndex = -1;
		int priceIndex = -1;
		for (int i = 0; i < headerRow.length; i++) {
			if (headerRow[i].trim().equalsIgnoreCase(template.getSkuColumnName())) {
				skuIndex = i;
			}
			if (headerRow[i].trim().equalsIgnoreCase(template.getPriceColumnName())) {
				priceIndex = i;
			}
		}
		if (skuIndex == -1) {
			throw new IllegalArgumentException("Column " + template.getSkuColumnName() + " not found");
		}
		if (priceIndex == -1) {
			throw new IllegalArgumentException("The price " + template.getPriceColumnName() + " is not found");

		}
		int processed = 0;	// counter for processed rows
		int updated = 0;	// counter for updated rows
		int skipped = 0;	// counter for skipped rows
		/* iterating the data for each row */
		for (int i = template.getHeaderRowNumber(); i < csvRows.size(); i++) {
			String[] row = csvRows.get(i);
			processed++;
			if (row.length <= skuIndex || row.length <= priceIndex) {
				skipped++;
				continue;
			}
			String sku = row[skuIndex].trim();
			String price = row[priceIndex].trim().replace("$", "");
			ItemSupplier link = skuMap.get(sku);
			/* supplier doesn't carry this SKU or the link is inactive */
			if (link == null) {
				skipped++;
				continue;
			}
			try {
				BigDecimal priceParse = new BigDecimal(price);
				link.setPrice(priceParse);
				supplierService.updateItemSupplier(link);
				updated++;
			} catch (IllegalArgumentException e) { // if a valid value isn't present
				skipped++;
				continue;
			}
		}
		PriceImport newImport = new PriceImport();
		newImport.setSupplierId(supplierId);
		newImport.setImportedBy(importedBy);
		newImport.setFileName(csvFile.getName());
		newImport.setRowsProcessed(processed);
		newImport.setRowsSkipped(skipped);
		newImport.setRowsUpdated(updated);
		newImport.setNotes(null);
		priceImportDao.save(newImport);
		return newImport;
	}
	
	/**
	 * Returns history of all imports.
	 * @return	The import history
	 * @throws SQLException	Error from the database.
	 */
	public List<PriceImport> findImportHistory() throws SQLException {
		return priceImportDao.findAll();
	}
	
	/**
	 * Finds a CSV template by supplier 
	 * @param supplierId	Supplier I.D.
	 * @return	The CSV template
	 * @throws SQLException	Error from the database.
	 */
	public Optional<CsvImportTemplate> findTemplateBySupplier(int supplierId) throws SQLException {
		return csvDao.findBySupplierId(supplierId);
	}
}
