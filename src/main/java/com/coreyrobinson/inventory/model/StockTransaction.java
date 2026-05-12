/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Creates a reference to a Stock Transaction which logs every quantity change for inventory.
 * DATE: 05/06/2026
 */
package com.coreyrobinson.inventory.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StockTransaction {
	
	private int transactionId;
	private int itemId;
	private BigDecimal quantityChange;
	private String transType;
	private LocalDateTime transDate;
	private int changedBy;
	private String notes;
	
	public StockTransaction() {
		
	}
	
	/**
	 * Construction for creating a stock transaction.
	 * @param transactionId		I.D. of the stock transaction.
	 * @param itemId			I.D. of an item.
	 * @param quantityChange	Amount the stock has changed. "+" for stock received, "-" for stock used.
	 * @param transType			Type of stock transaction (used, received, adjusted).
	 * @param transDate			Date of the stock transaction.
	 * @param changedBy			Who did the stock transaction.
	 * @param notes				Any notes on the stock transaction.
	 */
	public StockTransaction(int transactionId, int itemId, BigDecimal quantityChange, String transType, LocalDateTime transDate, int changedBy, String notes) {
		this.transactionId = transactionId;
		this.itemId = itemId;
		this.quantityChange = quantityChange;
		this.transType = transType;
		this.transDate = transDate;
		this.changedBy = changedBy;
		this.notes = notes;
	}
	
	/**
	 * Constructor for required information during creation of a stock transaction.
	 * @param itemId			I.D. of an item.
	 * @param quantityChange	Amount the stock has changed. "+" for stock received, "-" for stock used.
	 * @param transType			Type of stock transaction (used, received, adjusted).
	 * @param changedBy			Who did the stock transaction.
	 * @param notes				Any notes on the stock transaction.
	 */
	public StockTransaction(int itemId, BigDecimal quantityChange, String transType, int changedBy, String notes) {
		this.itemId = itemId;
		this.quantityChange = quantityChange;
		this.transType = transType;
		this.changedBy = changedBy;
		this.notes = notes;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getQuantityChange() {
		return quantityChange;
	}

	public void setQuantityChange(BigDecimal quantityChange) {
		this.quantityChange = quantityChange;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public LocalDateTime getTransDate() {
		return transDate;
	}

	public void setTransDate(LocalDateTime transDate) {
		this.transDate = transDate;
	}

	public int getChangedBy() {
		return changedBy;
	}

	public void setChangedBy(int changedBy) {
		this.changedBy = changedBy;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "StockTransaction [transactionId=" + transactionId + ", itemId=" + itemId + ", quantityChange="
				+ quantityChange + ", transType=" + transType + ", transDate=" + transDate + ", changedBy=" + changedBy
				+ ", notes=" + notes + "]";
	}
}
