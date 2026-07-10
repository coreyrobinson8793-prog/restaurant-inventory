/**
 * AUTHOR: Corey Robinson
 * PURPOSE: Handles operations for purchase orders.
 * DATE: 06/30/26
 */
package com.coreyrobinson.inventory.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.coreyrobinson.inventory.dao.ItemSupplierDAO;
import com.coreyrobinson.inventory.dao.PurchaseOrderDAO;
import com.coreyrobinson.inventory.dao.PurchaseOrderItemDAO;
import com.coreyrobinson.inventory.model.ItemSupplier;
import com.coreyrobinson.inventory.model.PurchaseOrder;
import com.coreyrobinson.inventory.model.PurchaseOrderItem;

public class PurchaseOrderService {
	
	private final PurchaseOrderDAO poDao = new PurchaseOrderDAO();
	private final ItemSupplierDAO itemSuppDao = new ItemSupplierDAO();
	private final PurchaseOrderItemDAO poItemDao = new PurchaseOrderItemDAO();
	
	/**
	 * Creates a purchase order.
	 * @param supplierId	Supplier I.D. for purchase order.
	 * @param createdBy	Who created the purchase order.
	 * @param notes	Any notes for the purchase order.
	 * @return	The created purchase order.
	 * @throws SQLException	Error from the database.
	 */
	public PurchaseOrder createPurchaseOrder(int supplierId, int createdBy, String notes) throws SQLException {
		PurchaseOrder newPo = new PurchaseOrder();
		newPo.setSupplierId(supplierId);
		newPo.setCreatedBy(createdBy);
		newPo.setNotes(notes);
		newPo.setCurrentStatus("OPEN");
		newPo.setTotal(BigDecimal.ZERO);
		newPo.setCreatedDate(LocalDateTime.now());
		PurchaseOrder saved = poDao.save(newPo);
		return saved;
	}
	
	/**
	 * Add a new item to a purchase order.
	 * @param poId	The purchase order I.D.
	 * @param itemId	The I.D. of the item to add.
	 * @param quantity	How many of the item to add.
	 * @return	The newly added item.
	 * @throws SQLException	Error from the database.
	 */
	public PurchaseOrderItem addLineItem(int poId, int itemId, BigDecimal quantity) throws SQLException {
		if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Quantity must be greater than zero");
		}
		Optional<PurchaseOrder> optPo = poDao.findById(poId);
		if (optPo.isEmpty()) {
			throw new IllegalArgumentException("Purchase order doesn't exist");
		}
		PurchaseOrder po = optPo.get();
		if (!"OPEN".equals(po.getCurrentStatus())) {
			throw new IllegalArgumentException("Cannot add items to a " + po.getCurrentStatus() + " purchase order.");
		}
		Optional<ItemSupplier> optLinkPoItem = itemSuppDao.findByItemAndSupplier(itemId, po.getSupplierId());
		if (optLinkPoItem.isEmpty()) {
			throw new IllegalArgumentException("This supplier doesn't carry this item");
		}
		ItemSupplier linkPoItem = optLinkPoItem.get();
		BigDecimal priceAtTime = linkPoItem.getPrice();
		PurchaseOrderItem newLineItem = new PurchaseOrderItem();
		newLineItem.setPoId(poId);
		newLineItem.setItemId(itemId);
		newLineItem.setQuantityOrdered(quantity);
		newLineItem.setPriceAtTime(priceAtTime);
		PurchaseOrderItem saved = poItemDao.save(newLineItem);
		recalculateTotal(poId);
		return saved;
	}

	/**
	 * Removes a line item from a purchase order.
	 * @param poItemId The line item I.D. to remove.
	 * @throws SQLException Error from the database.
	 */
	public void removeLineItem(int poItemId) throws SQLException {
		Optional<PurchaseOrderItem> optLineItem = poItemDao.findById(poItemId);
		if (optLineItem.isEmpty()) {
			throw new IllegalArgumentException("Line item doesn't exist");
		}
		PurchaseOrderItem lineItem = optLineItem.get();
		Optional<PurchaseOrder> optPo = poDao.findById(lineItem.getPoId());
		if (optPo.isEmpty()) {
			throw new IllegalArgumentException("Purchase order doesn't exist");
		}
		if (!"OPEN".equals(optPo.get().getCurrentStatus())) {
			throw new IllegalArgumentException("Cannot remove items from a " + optPo.get().getCurrentStatus() + " purchase order.");
		}
		poItemDao.delete(poItemId);
		recalculateTotal(lineItem.getPoId());
	}
	
	/**
	 * Receives a purchase order and updates its status to "RECEIVED".
	 * @param poId The purchase order I.D.
	 * @throws SQLException Error from the database.
	 */
	public void receivePurchaseOrder(int poId) throws SQLException {
		Optional<PurchaseOrder> optPo = poDao.findById(poId);
		if (optPo.isEmpty()) {
			throw new IllegalArgumentException("Purchase order doesn't exist");
		}
		PurchaseOrder po = optPo.get();
		if (!"OPEN".equals(po.getCurrentStatus())) {
			throw new IllegalArgumentException("Cannot receive a " + po.getCurrentStatus() + " purchase order.");
		}
		po.setCurrentStatus("RECEIVED");
		poDao.save(po);
		//TODO: increment stock counts for received items
	}
	
	/**
	 * Cancels a purchase order and updates its status to "CANCELLED".
	 * @param poId The purchase order I.D.
	 * @throws SQLException Error from the database.
	 */
	public void cancelPurchaseOrder(int poId) throws SQLException {
		Optional<PurchaseOrder> optPo = poDao.findById(poId);
		if (optPo.isEmpty()) {
			throw new IllegalArgumentException("Purchase order doesn't exist");
		}
		PurchaseOrder po = optPo.get();
		if ("RECEIVED".equals(po.getCurrentStatus())) {
			throw new IllegalArgumentException("Cannot cancel a received purchase order.");
		}
		if("CANCELLED".equals(po.getCurrentStatus())) {
			throw new IllegalArgumentException("Purchase order is already cancelled.");
		}
		po.setCurrentStatus("CANCELLED");
		poDao.save(po);
	}
	
	/**
	 * Checks if an item is on any open purchase orders.
	 * @param itemId The I.D. of the item to check.
	 * @return True if the item is on an open purchase order, false otherwise.
	 * @throws SQLException Error from the database.
	 */
	public boolean isItemOnOpenPurchaseOrder(int itemId) throws SQLException {
		List<PurchaseOrder> openPurchaseOrders = poDao.findByStatus("OPEN");
		for (PurchaseOrder po : openPurchaseOrders) {
			List<PurchaseOrderItem> lineItems = poItemDao.findByPurchaseOrderId(po.getPoId());
			for (PurchaseOrderItem lineItem : lineItems) {
				if (lineItem.getItemId() == itemId) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Finds all purchase orders in the database.
	 * @return All Purchase Orders.
	 * @throws SQLException Error from the database.
	 */
	public List<PurchaseOrder> findAllPurchaseOrders() throws SQLException {
		return poDao.findAll();
	}
	
	/**
	 * Finds a purchase order by its I.D.
	 * @param poId The purchase order's I.D.
	 * @return The found purchase order.
	 * @throws SQLException Error from the database.
	 */
	public Optional<PurchaseOrder> findPurchaseOrderById(int poId) throws SQLException {
		return poDao.findById(poId);
	}
	
	/**
	 * Finds all line items for a given purchase order.
	 * @param poId The purchase order's I.D.
	 * @return List of line items for the purchase order.
	 * @throws SQLException Error from the database.
	 */
	public List<PurchaseOrderItem> findLineItems(int poId) throws SQLException {
		return poItemDao.findByPurchaseOrderId(poId);
	}
	
	/**
	 * Find purchase orders matching that requested status.
	 * @param status	Status of the PO.
	 * @return	The purchase order.
	 * @throws SQLException	Error from the database.
	 */
	public List<PurchaseOrder> findPurchaseOrdersByStatus(String status) throws SQLException {
		return poDao.findByStatus(status);
	}
	
	/**
	 * Recalculates the total when adding a new item to a purchase order.
	 * @param poId	The I.D. of the purchase order to recalculate the total to.
	 * @throws SQLException	Error from the database.
	 */
	private void recalculateTotal(int poId) throws SQLException{
		List<PurchaseOrderItem> poToRecalculate = poItemDao.findByPurchaseOrderId(poId);
		BigDecimal total = BigDecimal.ZERO;
		for (PurchaseOrderItem line : poToRecalculate) {
			total = total.add(line.getQuantityOrdered().multiply(line.getPriceAtTime()));
		}
		Optional<PurchaseOrder> optRecalculatedPo = poDao.findById(poId);
		if (optRecalculatedPo.isEmpty()) {
			throw new IllegalArgumentException("Purchase order doesn't exist");
		}
		PurchaseOrder recalculatedPo = optRecalculatedPo.get();
		recalculatedPo.setTotal(total);
		poDao.save(recalculatedPo);
	}

}
