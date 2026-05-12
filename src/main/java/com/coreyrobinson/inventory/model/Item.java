/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Creates a reference to an Item.
 * DATE: 05/03/2026
 */
package com.coreyrobinson.inventory.model;

import java.math.BigDecimal;

public class Item {
	
	private int itemId;
	private String itemName;
	private int unitId;
	private int categoryId;
	private BigDecimal currentStock;
	private BigDecimal parstock;
	private boolean active;
	
	public Item() {
		
	}
	
	/**
	 * Constructor for an item's information.
	 * @param itemId		I.D. for the item.
	 * @param itemName		Name of the item.
	 * @param categoryId	Category for a given item.
	 * @param unitId		I.D. for the unit of measurement.
	 * @param currentStock	The current stock on hand.
	 * @param parstock		The minimum required stock to keep on hand.
	 * @param active		If the item is actively being ordered.
	 */
	public Item(int itemId, String itemName, int unitId, int categoryId, BigDecimal currentStock, BigDecimal parstock, boolean active) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.unitId = unitId;
		this.categoryId = categoryId;
		this.currentStock = currentStock;
		this.parstock = parstock;
		this.active = active;
	}
	
	/**
	 * Constructor for information needed only at registration.
	 * @param itemName		Name of the item.
	 * @param unitId		I.D. for the unit of measurement.
	 * @param categoryId	Category for a given item.
	 * @param parstock		The minimum required stock to keep on hand.
	 */
	public Item(String itemName, int unitId, int categoryId, BigDecimal parstock) {
		this.itemName = itemName;
		this.unitId = unitId;
		this.categoryId = categoryId;
		this.parstock = parstock;
		this.currentStock = BigDecimal.ZERO;
		this.active = true;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public BigDecimal getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(BigDecimal currentStock) {
		this.currentStock = currentStock;
	}

	public BigDecimal getParstock() {
		return parstock;
	}

	public void setParstock(BigDecimal parstock) {
		this.parstock = parstock;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "Item [itemId=" + itemId + ", itemName=" + itemName + ", unitId=" + unitId + ", categoryId=" + categoryId
				+ ", currentStock=" + currentStock + ", parstock=" + parstock + ", active=" + active + "]";
	}
}
