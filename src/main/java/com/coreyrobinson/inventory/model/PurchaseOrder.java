/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Creates a reference to a Purchase Order which tracks what's being ordered from a supplier and the order's status through its life-cycle.
 * DATE: 05/06/2026
 */
package com.coreyrobinson.inventory.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PurchaseOrder {
	
	private int poId;
	private int supplierId;
	private int createdBy;
	private LocalDateTime createdDate;
	private String currentStatus;
	private BigDecimal total;
	private String notes;
	
	public PurchaseOrder() {
		
	}
	
	/**
	 * Constructor for creating a purchase order.
	 * @param poId			I.D. for the purchase order.
	 * @param supplierId	I.D. of the supplier.
	 * @param createdBy		Who created the purchase order.
	 * @param createdDate	When the purchase order was created.
	 * @param currentStatus	Status of the purchase order (draft, finalized, sent).
	 * @param total			Total cost of the purchase order.
	 * @param notes			Any notes about the purchase order.
	 */
	public PurchaseOrder(int poId, int supplierId, int createdBy, LocalDateTime createdDate, String currentStatus, BigDecimal total, String notes) {
		this.poId = poId;
		this.supplierId = supplierId;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.currentStatus = currentStatus;
		this.total = total;
		this.notes = notes;
	}
	
	/**
	 * Constructor for required information at creation of a purchase order.
	 * @param supplierId	I.D. of the supplier.
	 * @param createdBy		Who created the purchase order.
	 * @param notes			Any notes about the purchase order.
	 */
	public PurchaseOrder(int supplierId, int createdBy, String notes) {
		this.supplierId = supplierId;
		this.createdBy = createdBy;
		this.notes = notes;
		this.currentStatus = "Draft";
		this.total = BigDecimal.ZERO;
	}

	public int getPoId() {
		return poId;
	}

	public void setPoId(int poId) {
		this.poId = poId;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "PurchaseOrder [poId=" + poId + ", supplierId=" + supplierId + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", currentStatus=" + currentStatus + ", total=" + total + ", notes="
				+ notes + "]";
	}

}
