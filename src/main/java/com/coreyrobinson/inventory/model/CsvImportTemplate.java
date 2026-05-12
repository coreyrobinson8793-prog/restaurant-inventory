/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Creates a reference to a CSV template from a vendor for updating pricing.
 * DATE: 05/06/2026
 */
package com.coreyrobinson.inventory.model;

import java.time.LocalDateTime;

public class CsvImportTemplate {
	
	private int templateId;
	private int supplierId;
	private String skuColumnName;
	private String priceColumnName;
	private int headerRowNumber;
	private int createdBy;
	private LocalDateTime createdAt;
	
	public CsvImportTemplate() {
		
	}
	
	/**
	 * Constructor for creating a CSV template.
	 * @param templateId		I.D. for a given template.
	 * @param supplierId		I.D. for a given supplier.
	 * @param skuColumnName		Column for the sku number.
	 * @param priceColumnName	Column for the price of an item.
	 * @param headerRowNumber	Row in CSV file containing column headers.
	 * @param createdBy			Who created the template.
	 * @param createdAt			When the template was created.
	 */
	public CsvImportTemplate(int templateId, int supplierId, String skuColumnName, String priceColumnName, int headerRowNumber, int createdBy, LocalDateTime createdAt) {
		this.templateId = templateId;
		this.supplierId = supplierId;
		this.skuColumnName = skuColumnName;
		this.priceColumnName = priceColumnName;
		this.headerRowNumber = headerRowNumber;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
	}
	
	/**
	 * Constructor for required information at creation of a CSV template.
	 * @param supplierId		I.D. for a given supplier.
	 * @param skuColumnName		Column for the sku number.
	 * @param priceColumnName	Column for the price of an item.
	 * @param headerRowNumber	Row in CSV file containing column headers.
	 * @param createdBy			Who created the template.
	 */
	public CsvImportTemplate(int supplierId, String skuColumnName, String priceColumnName, int headerRowNumber, int createdBy) {
		this.supplierId = supplierId;
		this.skuColumnName = skuColumnName;
		this.priceColumnName = priceColumnName;
		this.headerRowNumber = headerRowNumber;
		this.createdBy = createdBy;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public String getSkuColumnName() {
		return skuColumnName;
	}

	public void setSkuColumnName(String skuColumnName) {
		this.skuColumnName = skuColumnName;
	}

	public String getPriceColumnName() {
		return priceColumnName;
	}

	public void setPriceColumnName(String priceColumnName) {
		this.priceColumnName = priceColumnName;
	}

	public int getHeaderRowNumber() {
		return headerRowNumber;
	}

	public void setHeaderRowNumber(int headerRowNumber) {
		this.headerRowNumber = headerRowNumber;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "CsvImportTemplate [templateId=" + templateId + ", supplierId=" + supplierId + ", skuColumnName="
				+ skuColumnName + ", priceColumnName=" + priceColumnName + ", headerRowNumber=" + headerRowNumber
				+ ", createdBy=" + createdBy + ", createdAt=" + createdAt + "]";
	}
}
