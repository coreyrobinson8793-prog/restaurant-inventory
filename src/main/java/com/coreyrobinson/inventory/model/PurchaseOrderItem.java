/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Creates a reference to a Purchase Order Item which defines line items within a purchase order.
 * DATE: 05/06/2026
 */
package com.coreyrobinson.inventory.model;

import java.math.BigDecimal;

public class PurchaseOrderItem {
	
	private int poItemId;
	private int poId;
	private int itemId;
	private BigDecimal quantityOrdered;
	private BigDecimal priceAtTime;
	
	public PurchaseOrderItem() {
		
	}
	
	/**
	 * Constructor for creating a line item in a purchase order.
	 * @param poItemId			I.D. for the line item.
	 * @param poId				I.D. for the purchase order.	
	 * @param itemId			I.D. for an item.
	 * @param quantityOrdered	Amount ordered.
	 * @param priceAtTime		Price of an item at creation of the PO.
	 */
	public PurchaseOrderItem(int poItemId, int poId, int itemId, BigDecimal quantityOrdered, BigDecimal priceAtTime) {
		this.poItemId = poItemId;
		this.poId = poId;
		this.itemId = itemId;
		this.quantityOrdered = quantityOrdered;
		this.priceAtTime = priceAtTime;
	}
	
	/**
	 * Constructor for required information at creation of a line item in a purchase order.
	 * @param poId				I.D. for the purchase order.	
	 * @param itemId			I.D. for an item.
	 * @param quantityOrdered	Amount ordered.
	 * @param priceAtTime		Price at creation of the PO.
	 */
	public PurchaseOrderItem(int poId, int itemId, BigDecimal quantityOrdered, BigDecimal priceAtTime) {
		this.poId = poId;
		this.itemId = itemId;
		this.quantityOrdered = quantityOrdered;
		this.priceAtTime = priceAtTime;
	}

	public int getPoItemId() {
		return poItemId;
	}

	public void setPoItemId(int poItemId) {
		this.poItemId = poItemId;
	}

	public int getPoId() {
		return poId;
	}

	public void setPoId(int poId) {
		this.poId = poId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getQuantityOrdered() {
		return quantityOrdered;
	}

	public void setQuantityOrdered(BigDecimal quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}

	public BigDecimal getPriceAtTime() {
		return priceAtTime;
	}

	public void setPriceAtTime(BigDecimal priceAtTime) {
		this.priceAtTime = priceAtTime;
	}

	@Override
	public String toString() {
		return "PurchaseOrderItem [poItemId=" + poItemId + ", poId=" + poId + ", itemId=" + itemId
				+ ", quantityOrdered=" + quantityOrdered + ", priceAtTime=" + priceAtTime + "]";
	}
}
