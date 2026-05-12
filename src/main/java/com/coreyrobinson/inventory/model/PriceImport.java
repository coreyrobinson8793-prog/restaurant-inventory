/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Creates a reference to a Price Import for adding prices of items from vendors.
 * DATE: 05/06/2026
 */
package com.coreyrobinson.inventory.model;

import java.time.LocalDateTime;

public class PriceImport {
	
	private int importId;
	private int supplierId;
	private int importedBy;
	private LocalDateTime importDate;
	private String fileName;
	private int rowsProcessed;
	private int rowsUpdated;
	private int rowsSkipped;
	private String notes;
	
	public PriceImport() {
		
	}
	
	/**
	 * Constructor for creating a price import.
	 * @param importId		I.D. of the price import.
	 * @param supplierId	I.D. of the supplier.
	 * @param importedBy	Who imported the prices.
	 * @param importDate	When the price import was done.
	 * @param fileName		Name of the file the price import is stored in.
	 * @param rowsProcessed	Total rows of the price import.
	 * @param rowsUpdated	Rows that were updated by the price import.
	 * @param rowsSkipped	Rows that were not updated by the price import.
	 * @param notes			Any notes for each row of the price import.
	 */
	public PriceImport(int importId, int supplierId, int importedBy, LocalDateTime importDate, String fileName, int rowsProcessed, int rowsUpdated, int rowsSkipped, String notes) {
		this.importId = importId;
		this.supplierId = supplierId;
		this.importedBy = importedBy;
		this.importDate = importDate;
		this.fileName = fileName;
		this.rowsProcessed = rowsProcessed;
		this.rowsUpdated = rowsUpdated;
		this.rowsSkipped = rowsSkipped;
		this.notes = notes;
	}
	
	/**
	 * Constructor for the required information at creation of a price import.
	 * @param supplierId	I.D. of the supplier.
	 * @param importedBy	Who imported the prices.
	 * @param fileName		Name of the file the price import is stored in.
	 */
	public PriceImport(int supplierId, int importedBy, String fileName) {
		this.supplierId = supplierId;
		this.importedBy = importedBy;
		this.fileName = fileName;
	}

	public int getImportId() {
		return importId;
	}

	public void setImportId(int importId) {
		this.importId = importId;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public int getImportedBy() {
		return importedBy;
	}

	public void setImportedBy(int importedBy) {
		this.importedBy = importedBy;
	}

	public LocalDateTime getImportDate() {
		return importDate;
	}

	public void setImportDate(LocalDateTime importDate) {
		this.importDate = importDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getRowsProcessed() {
		return rowsProcessed;
	}

	public void setRowsProcessed(int rowsProcessed) {
		this.rowsProcessed = rowsProcessed;
	}

	public int getRowsUpdated() {
		return rowsUpdated;
	}

	public void setRowsUpdated(int rowsUpdated) {
		this.rowsUpdated = rowsUpdated;
	}

	public int getRowsSkipped() {
		return rowsSkipped;
	}

	public void setRowsSkipped(int rowsSkipped) {
		this.rowsSkipped = rowsSkipped;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "PriceImport [importId=" + importId + ", supplierId=" + supplierId + ", importedBy=" + importedBy
				+ ", importDate=" + importDate + ", fileName=" + fileName + ", rowsProcessed=" + rowsProcessed
				+ ", rowsUpdated=" + rowsUpdated + ", rowsSkipped=" + rowsSkipped + ", notes=" + notes + "]";
	}
}
