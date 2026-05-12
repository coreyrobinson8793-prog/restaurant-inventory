/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Creates a reference to an Item Supplier which will handle pricing, ordering, and POs from suppliers.
 * DATE: 05/06/2026
 */
package com.coreyrobinson.inventory.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ItemSupplier {
	
	private int itemSupplierId;
	private int itemId;
	private int supplierId;
	private String supplierSku;
	private BigDecimal price;
	private BigDecimal packSize;
	private int packUnitId;
	private boolean preferred;
	private boolean active;
	private LocalDateTime lastPriceUpdate;
	
	public ItemSupplier() {
		
	}
	
	/**
	 * Constructor for an items supplier information.
	 * @param itemSupplierId	I.D. of the item from the supplier.
	 * @param itemId			I.D. of the item.
	 * @param supplierId		I.D. of the supplier.
	 * @param supplierSku		Suppliers SKU number.
	 * @param price				Price of the item.
	 * @param packSize			Unit the amount of the item is measured in.
	 * @param packUnitId		I.D. of the pack size.
	 * @param preferred			A flag for a preferred supplier for a given item.
	 * @param active			A flag for an item supplier being active.
	 * @param lastPriceUpdate	Date and time of the last pricing update.
	 */
	public ItemSupplier(int itemSupplierId, int itemId, int supplierId, String supplierSku, BigDecimal price, BigDecimal packSize, int packUnitId, boolean preferred, boolean active, LocalDateTime lastPriceUpdate) {
		this.itemSupplierId = itemSupplierId;
		this.itemId = itemId;
		this.supplierId = supplierId;
		this.supplierSku = supplierSku;
		this.price = price;
		this.packSize = packSize;
		this.packUnitId = packUnitId;
		this.preferred = preferred;
		this.active = active;
		this.lastPriceUpdate = lastPriceUpdate;
	}
	
	/**
	 * Constructor for required information at creation of an items supplier relationship.
	 * @param itemId		I.D. of the item.
	 * @param supplierId	I.D. of the supplier.
	 * @param supplierSku	Suppliers SKU number.
	 * @param price			Price of the item.
	 * @param packSize		Unit the amount of the item is measured in.
	 * @param packUnitId	I.D. of the pack size.
	 */
	public ItemSupplier(int itemId, int supplierId, String supplierSku, BigDecimal price, BigDecimal packSize, int packUnitId) {
		this.itemId = itemId;
		this.supplierId = supplierId;
		this.supplierSku = supplierSku;
		this.price = price;
		this.packSize = packSize;
		this.packUnitId = packUnitId;
		this.preferred = false;
		this.active = true;
	}

	public int getItemSupplierId() {
		return itemSupplierId;
	}

	public void setItemSupplierId(int itemSupplierId) {
		this.itemSupplierId = itemSupplierId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierSku() {
		return supplierSku;
	}

	public void setSupplierSku(String supplierSku) {
		this.supplierSku = supplierSku;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPackSize() {
		return packSize;
	}

	public void setPackSize(BigDecimal packSize) {
		this.packSize = packSize;
	}

	public int getPackUnitId() {
		return packUnitId;
	}

	public void setPackUnitId(int packUnitId) {
		this.packUnitId = packUnitId;
	}

	public boolean isPreferred() {
		return preferred;
	}

	public void setPreferred(boolean preferred) {
		this.preferred = preferred;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public LocalDateTime getLastPriceUpdate() {
		return lastPriceUpdate;
	}

	public void setLastPriceUpdate(LocalDateTime lastPriceUpdate) {
		this.lastPriceUpdate = lastPriceUpdate;
	}

	@Override
	public String toString() {
		return "ItemSupplier [itemSupplierId=" + itemSupplierId + ", itemId=" + itemId + ", supplierId=" + supplierId
				+ ", supplierSku=" + supplierSku + ", price=" + price + ", packSize=" + packSize + ", packUnitId="
				+ packUnitId + ", preferred=" + preferred + ", active=" + active + ", lastPriceUpdate="
				+ lastPriceUpdate + "]";
	}
	
	

}
