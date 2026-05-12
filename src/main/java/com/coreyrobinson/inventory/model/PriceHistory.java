/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Creates a reference to the Price History of a given range.
 * DATE: 05/03/2026
 */
package com.coreyrobinson.inventory.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PriceHistory {
	
	private int priceHistoryId;
	private int itemSupplierId;
	private BigDecimal price;
	private LocalDateTime changedAt;
	private int changedBy;
	
	public PriceHistory() {
		
	}
	
	/**
	 * Constructor for price history information.
	 * @param priceHistoryId	I.D. of the price history query.
	 * @param itemSupplierId			I.D. of the items in the price history.
	 * @param price				Price of the items.
	 * @param changedAt			When the price was changed.
	 * @param changedBy			Who changed the price.
	 */
	public PriceHistory(int priceHistoryId, int itemSupplierId, BigDecimal price, LocalDateTime changedAt, int changedBy) {
		this.priceHistoryId = priceHistoryId;
		this.itemSupplierId = itemSupplierId;
		this.price = price;
		this.changedAt = changedAt;
		this.changedBy = changedBy;
	}
	
	/**
	 * Constructor for information needed only at registration.
	 * @param itemSupplierId		I.D. of the items in the price history.
	 * @param price			Price of the items.
	 * @param changedBy		Who changed the price.
	 */
	public PriceHistory(int itemSupplierId, BigDecimal price, int changedBy) {
		this.itemSupplierId = itemSupplierId;
		this.price = price;
		this.changedBy = changedBy;
	}

	public int getPriceHistoryId() {
		return priceHistoryId;
	}

	public void setPriceHistoryId(int priceHistoryId) {
		this.priceHistoryId = priceHistoryId;
	}

	public int getItemSupplierId() {
		return itemSupplierId;
	}

	public void setItemSupplierId(int itemSupplierId) {
		this.itemSupplierId = itemSupplierId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public LocalDateTime getChangedAt() {
		return changedAt;
	}

	public void setChangedAt(LocalDateTime changedAt) {
		this.changedAt = changedAt;
	}

	public int getChangedBy() {
		return changedBy;
	}

	public void setChangedBy(int changedBy) {
		this.changedBy = changedBy;
	}

	@Override
	public String toString() {
		return "PriceHistory [priceHistoryId=" + priceHistoryId + ", itemSupplierId=" + itemSupplierId + ", price=" + price
				+ ", changedAt=" + changedAt + ", changedBy=" + changedBy + "]";
	}
	
	

}
