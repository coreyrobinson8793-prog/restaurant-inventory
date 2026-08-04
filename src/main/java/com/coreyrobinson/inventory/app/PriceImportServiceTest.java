package com.coreyrobinson.inventory.app;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import com.coreyrobinson.inventory.model.PriceImport;
import com.coreyrobinson.inventory.service.PriceImportService;
import com.opencsv.exceptions.CsvException;

public class PriceImportServiceTest {

	public static void main(String[] args) {
		/* Expects supplier 1 with SKUs 1111111 and 222222 linked */
		PriceImportService priceImportServiceTest = new PriceImportService();
		try {
			priceImportServiceTest.saveTemplate(1, "SKU", "Price", 1, 1);
			PriceImport result = priceImportServiceTest.importPrices(new File("test_prices.csv"), 1, 1);
			System.out.println("Rows processed: " + result.getRowsProcessed() + " | Rows skipped: " + result.getRowsSkipped()
			+ " | Rows updated: " + result.getRowsUpdated() + " | Import I.D.: " + result.getImportId());
		} catch (SQLException e) {
			System.out.println("FAIL: SQLException occurred - " + e.getMessage());
		} catch (IOException e) {
			System.out.println("FAIL: IOException occurred - " + e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println("FAIL: IllegalArgumentException occurred - " + e.getMessage());
		} catch (CsvException e) {
			System.out.println("FAIL: CsvException occurred - " + e.getMessage());
		}
	}
}
