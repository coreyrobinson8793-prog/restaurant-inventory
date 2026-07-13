/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Handles the logic for the reports screen.
 * DATE: 07/09/26
 */
package com.coreyrobinson.inventory.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coreyrobinson.inventory.dao.UserDAO;
import com.coreyrobinson.inventory.model.Item;
import com.coreyrobinson.inventory.model.ItemSupplier;
import com.coreyrobinson.inventory.model.PurchaseOrder;
import com.coreyrobinson.inventory.model.Supplier;
import com.coreyrobinson.inventory.model.User;
import com.coreyrobinson.inventory.service.ItemService;
import com.coreyrobinson.inventory.service.PurchaseOrderService;
import com.coreyrobinson.inventory.service.SupplierService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ReportsController {

	@FXML private TableView<Item> lowStockTable;
	@FXML private TableColumn<Item, String> lsItemColumn;
	@FXML private TableColumn<Item, String> lsCurrentStockColumn;
	@FXML private TableColumn<Item, String> lsParColumn;
	@FXML private TableColumn<Item, String> lsShortageColumn;
	@FXML private TableView<PurchaseOrder> openPoTable;
	@FXML private TableColumn<PurchaseOrder, String> opPoIdColumn;
	@FXML private TableColumn<PurchaseOrder, String> opSupplierColumn;
	@FXML private TableColumn<PurchaseOrder, String> opDateColumn;
	@FXML private TableColumn<PurchaseOrder, String> opUserColumn;
	@FXML private TableColumn<PurchaseOrder, String> opTotalColumn;
	@FXML private TableView <SupplierSpend> spendBySupplierTable;
	record SupplierSpend(String supplierName, int orderCount, BigDecimal totalSpend) {}
	@FXML private TableColumn <SupplierSpend, String> spSupplierColumn;
	@FXML private TableColumn <SupplierSpend, String> spPoCountColumn;
	@FXML private TableColumn <SupplierSpend, String> spTotalColumn;
	@FXML private TableView <ItemSupplier> priceCompareTable;
	@FXML private TableColumn<ItemSupplier, String> pcItemColumn;
	@FXML private TableColumn<ItemSupplier, String> pcSupplierColumn;
	@FXML private TableColumn<ItemSupplier, String> pcPriceColumn;
	@FXML private TableColumn<ItemSupplier, String> pcPackSizeColumn;
	@FXML private TableColumn<ItemSupplier, String> pcPreferredColumn;
	@FXML private Label messageLabel;
	private ItemService itemService = new ItemService();
	private PurchaseOrderService poService = new PurchaseOrderService();
	private SupplierService supplierService = new SupplierService();
	private UserDAO userDao = new UserDAO();
	private Map <Integer, String> supplierNameMap;
	private Map <Integer, String> itemNameMap;
	private Map <Integer, String> userNameMap;
	private ObservableList<Item> lowStockList = FXCollections.observableArrayList();
	private ObservableList<PurchaseOrder> openPoList = FXCollections.observableArrayList();
	private ObservableList<SupplierSpend> spendBySupplierList = FXCollections.observableArrayList();
	private ObservableList<ItemSupplier> priceCompareList = FXCollections.observableArrayList();
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	@FXML
	public void initialize() {
		try {
			List<Supplier> suppliers = supplierService.findAllSuppliers();
			supplierNameMap = new HashMap<>();
			for (Supplier supplier : suppliers) {
				supplierNameMap.put(supplier.getSupplierId(), supplier.getSupplierName());
			}
			List<User> users = userDao.findAll();
			userNameMap = new HashMap<>();
			for (User user : users) {
				userNameMap.put(user.getUserId(), user.getUsername());
			}
			List<Item> items = itemService.findAllItems();
			itemNameMap = new HashMap<>();
			for (Item item : items) {
				itemNameMap.put(item.getItemId(), item.getItemName());
			}
		} catch (SQLException e) {
			showError("Error loading suppliers: " + e.getMessage());
		}
		lsItemColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getItemName())));
		lsCurrentStockColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getCurrentStock())));
		lsParColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getParstock())));
		lsShortageColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getParstock().subtract(cell.getValue().getCurrentStock()))));
		opPoIdColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getPoId())));
		opSupplierColumn.setCellValueFactory(cell -> new SimpleStringProperty(supplierNameMap.getOrDefault(cell.getValue().getSupplierId(), "?")));
		opDateColumn.setCellValueFactory(cell -> {
			LocalDateTime d = cell.getValue().getCreatedDate();
			return new SimpleStringProperty(d == null ? "" : d.format(DATE_FORMAT));
		});
		opUserColumn.setCellValueFactory(cell -> new SimpleStringProperty(userNameMap.getOrDefault(cell.getValue().getCreatedBy(), "?")));
		opTotalColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getTotal())));
		spSupplierColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().supplierName())));
		spPoCountColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().orderCount())));
		spTotalColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().totalSpend().toString())));
		pcItemColumn.setCellValueFactory(cell -> new SimpleStringProperty(itemNameMap.getOrDefault(cell.getValue().getItemId(), "?")));
		pcSupplierColumn.setCellValueFactory(cell -> new SimpleStringProperty(supplierNameMap.getOrDefault(cell.getValue().getSupplierId(), "?")));
		pcPriceColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getPrice().toString())));
		pcPackSizeColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getPackSize().toString())));
		pcPreferredColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().isPreferred() ? "Yes" : "No")));
		lowStockTable.setItems(lowStockList);
		loadLowStock();
		openPoTable.setItems(openPoList);
		loadOpenPos();
		priceCompareTable.setItems(priceCompareList);
		loadPriceComparison();
		spendBySupplierTable.setItems(spendBySupplierList);
		loadSpendBySupplier();
	}
	
	/*Loads the Low Stock List*/
	private void loadLowStock() {
		try {
			List<Item> lowStockItems = itemService.findAllItems();
			lowStockItems.removeIf(lowStockItem -> !lowStockItem.isActive());
			lowStockItems.removeIf(lowStockItem -> lowStockItem.getCurrentStock().compareTo(lowStockItem.getParstock()) >= 0);
			lowStockList.setAll(lowStockItems);
		} catch (SQLException e) {
			showError("Error finding items: " + e.getMessage());
		}
	}
	
	/*Loads open purchase orders*/
	private void loadOpenPos() {
		try {
			openPoList.setAll(poService.findPurchaseOrdersByStatus("OPEN"));
		} catch (SQLException e) {
			showError("Error finding Open POs");
		}
	}
	
	/*Compare prices of items from two different suppliers*/
	private void loadPriceComparison() {
		try {
			List<ItemSupplier> priceCompare = supplierService.findAllItemSuppliers();
			priceCompare.removeIf(priceCompared -> !priceCompared.isActive());
			priceCompare.sort(Comparator.comparing(link -> itemNameMap.getOrDefault(link.getItemId(), "")));
			priceCompareList.setAll(priceCompare);
		} catch (SQLException e) {
			showError("Cannot find supplier for this item: " + e.getMessage());
		}
	}
	
	/*Reports how much you have spent with each supplier*/
	private void loadSpendBySupplier() {
		Map<Integer, Integer> countBySupplier = new HashMap<>();
		Map<Integer, BigDecimal> totalBySupplier = new HashMap<>();		
		try {
			List<PurchaseOrder> receivedPOs = poService.findPurchaseOrdersByStatus("RECEIVED");
			for (PurchaseOrder po : receivedPOs) {
				int id = po.getSupplierId();
				countBySupplier.put(id, countBySupplier.getOrDefault(id, 0) + 1);
				totalBySupplier.put(id, totalBySupplier.getOrDefault(id, BigDecimal.ZERO).add(po.getTotal()));
			}
			List<SupplierSpend> spendResults = new ArrayList<>();
			for (int supplierId : countBySupplier.keySet()) {
				String name = supplierNameMap.getOrDefault(supplierId, "?");
				int count = countBySupplier.get(supplierId);
				BigDecimal total = totalBySupplier.get(supplierId);
				spendResults.add(new SupplierSpend(name, count, total));
			}
			spendBySupplierList.setAll(spendResults);
		} catch (SQLException e) {
			showError("Cannot load totals for suppliers: " + e.getMessage());
		}
	}
	
	/**
	 * Shows an error message.
	 * @param message	The error message.
	 */
	private void showError(String message) {
		messageLabel.setText(message);
		messageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: red;");	
	}
}
