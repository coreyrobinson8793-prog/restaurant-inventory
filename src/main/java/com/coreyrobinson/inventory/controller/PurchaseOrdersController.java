/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Handles the logic for the purchase orders screen.
 * DATE: 07/02/2026
 */
package com.coreyrobinson.inventory.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.coreyrobinson.inventory.dao.UserDAO;
import com.coreyrobinson.inventory.model.Item;
import com.coreyrobinson.inventory.model.PurchaseOrder;
import com.coreyrobinson.inventory.model.PurchaseOrderItem;
import com.coreyrobinson.inventory.model.Supplier;
import com.coreyrobinson.inventory.model.User;
import com.coreyrobinson.inventory.service.ItemService;
import com.coreyrobinson.inventory.service.PurchaseOrderService;
import com.coreyrobinson.inventory.service.SupplierService;
import com.coreyrobinson.inventory.session.Session;
import com.coreyrobinson.inventory.util.ConfirmDialog;
import com.coreyrobinson.inventory.util.MoneyFormatter;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class PurchaseOrdersController {

	@FXML private ChoiceBox<Supplier> supplierChoiceBox;
	@FXML private Label messageLabel;
	@FXML private TextField notesField;
	@FXML private TableView<PurchaseOrder> purchaseOrdersTable;
	@FXML private TableColumn<PurchaseOrder, String> poIdColumn;
	@FXML private TableColumn<PurchaseOrder, String> poSupplierColumn;
	@FXML private TableColumn<PurchaseOrder, String> poCreatedByColumn;
	@FXML private TableColumn<PurchaseOrder, String> poStatusColumn;
	@FXML private TableColumn<PurchaseOrder, String> poTotalCostColumn;
	@FXML private TableColumn<PurchaseOrder, String> poCreatedDateColumn;
	@FXML private Label poDetailHeaderLabel;
	@FXML private TableView<PurchaseOrderItem> lineItemsTable;
	@FXML private TableColumn<PurchaseOrderItem, String> lineItemNameColumn;
	@FXML private TableColumn<PurchaseOrderItem, String> lineQuantityColumn;
	@FXML private TableColumn<PurchaseOrderItem, String> linePriceColumn;
	@FXML private TableColumn<PurchaseOrderItem, String> lineSubTotalColumn;
	@FXML private TableColumn<PurchaseOrderItem, Void> lineActionsColumn;
	@FXML private ChoiceBox<Item> itemChoiceBox;
	@FXML private TextField quantityField;
	@FXML private Button receiveButton;
	@FXML private Button cancelButton;
	private PurchaseOrderService poService = new PurchaseOrderService();
	private SupplierService supplierService = new SupplierService();
	private UserDAO userDao = new UserDAO();
	private ObservableList<PurchaseOrder> poList = FXCollections.observableArrayList();
	private Map<Integer, String> supplierNameMap;
	private Map<Integer, String> userNameMap;
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	private ItemService itemService = new ItemService();
	private ObservableList<PurchaseOrderItem> lineItemsList = FXCollections.observableArrayList();
	private Map<Integer, String> itemNameMap;
	private PurchaseOrder selectedPO;

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
			supplierChoiceBox.setConverter(new StringConverter<Supplier>() {
				@Override
				public String toString(Supplier supplier) {
					return supplier == null ? "" : supplier.getSupplierName();
				}
				@Override
				public Supplier fromString(String s) {
					return null;
				}
			});
			supplierChoiceBox.getItems().setAll(suppliers);
			itemChoiceBox.setConverter(new StringConverter<Item>() {
				@Override
				public String toString(Item item) {
					return item == null ? "" : item.getItemName();
				}
				@Override
				public Item fromString(String s) {
					return null;
				}
			});
			itemChoiceBox.getItems().setAll(items);
		} catch (SQLException e) {
			showError("Error loading suppliers: " + e.getMessage());
		}
		poIdColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getPoId())));
		poSupplierColumn.setCellValueFactory(cell -> new SimpleStringProperty(supplierNameMap.getOrDefault(cell.getValue().getSupplierId(), "?")));
		poCreatedByColumn.setCellValueFactory(cell -> new SimpleStringProperty(userNameMap.getOrDefault(cell.getValue().getCreatedBy(), "?" )));
		poStatusColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCurrentStatus()));
		poTotalCostColumn.setCellValueFactory(cell -> new SimpleStringProperty(MoneyFormatter.format(cell.getValue().getTotal())));
		poCreatedDateColumn.setCellValueFactory(cell -> {
			LocalDateTime d = cell.getValue().getCreatedDate();
			return new SimpleStringProperty(d == null ? "" : d.format(DATE_FORMAT));
		});
		lineItemNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(itemNameMap.getOrDefault(cell.getValue().getItemId(), "?")));
		lineQuantityColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getQuantityOrdered().toString()));
		linePriceColumn.setCellValueFactory(cell -> new SimpleStringProperty(MoneyFormatter.format(cell.getValue().getPriceAtTime())));
		lineSubTotalColumn.setCellValueFactory(cell -> {
			PurchaseOrderItem line = cell.getValue();
			BigDecimal subtotal = line.getQuantityOrdered().multiply(line.getPriceAtTime());
			return new SimpleStringProperty(MoneyFormatter.format(subtotal));
		});
		lineActionsColumn.setCellFactory(column -> new TableCell<PurchaseOrderItem, Void>() {
			private final Button removeButton = new Button("Remove");
			{
				removeButton.setOnAction(event -> {
					PurchaseOrderItem lineItem = getTableView().getItems().get(getIndex());
					handleRemoveLineItem(lineItem);
				});
			}
			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				setGraphic(empty ? null : removeButton);
			}
		});
		purchaseOrdersTable.setItems(poList);
		lineItemsTable.setItems(lineItemsList);
		purchaseOrdersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
			if (newSel != null) {
				selectedPO = newSel;
				loadLineItems(newSel);
				updateStatusButtons(newSel);
			}
		});
		refreshPurchaseOrders();
	}

	/**
	 * Loads the line items for the selected purchase order and updates the line items table.
	 * @param po The selected purchase order for which to load line items.
	 */
	private void loadLineItems(PurchaseOrder po) {
		poDetailHeaderLabel.setText("PO #" + po.getPoId() + " - " + po.getCurrentStatus());
		try {
			List<PurchaseOrderItem> lineItems = poService.findLineItems(po.getPoId());
			lineItemsList.setAll(lineItems);
		} catch (SQLException e) {
			showError("Error loading line items: " + e.getMessage());
		}
	}

	/**
	 * Handles the action of adding a line item to the selected purchase order.
	 * Validates the input and updates the line items table upon successful addition.
	 */
	@FXML
	private void handleAddLineItem() {
		if (selectedPO != null) {
			if (!"OPEN".equals(selectedPO.getCurrentStatus())) {
				showError("Cannot add line items to a purchase order that is not OPEN.");
				return;
			}
			Item selectedItem = itemChoiceBox.getValue();
			if (selectedItem == null) {
				showError("Please select an item.");
				return;
			}
			try {
				BigDecimal quantity = new BigDecimal(quantityField.getText());
				poService.addLineItem(selectedPO.getPoId(), selectedItem.getItemId(), quantity);
				loadLineItems(selectedPO);
				refreshPurchaseOrders();
				itemChoiceBox.setValue(null);
				quantityField.clear();
				showSuccess("Line item added successfully.");
			} catch (NumberFormatException e) {
				showError("Please enter a valid quantity");
			} catch (IllegalArgumentException e) {
				showError(e.getMessage());
			} catch (SQLException e) {
				showError("Database error: " + e.getMessage());
			}
		} else {
			showError("Please select a purchase order to add line items.");
		}
	}

	/**
	 * Removes a line item from the selected purchase order and updates the line items table.
	 * @param lineItem The line item to be removed from the purchase order.
	 */
	private void handleRemoveLineItem(PurchaseOrderItem lineItem) {
		if (!ConfirmDialog.confirm("Remove Line Item", "Remove this line item?")) {
			return;
		}
		try {
			poService.removeLineItem(lineItem.getPoItemId());
			loadLineItems(selectedPO);
			refreshPurchaseOrders();
			showSuccess("Line item removed successfully.");
		} catch (IllegalArgumentException e) {
			showError(e.getMessage());
		} catch (SQLException e) {
			showError("Database error: " + e.getMessage());
		}
	}

	/**
	 * Refreshes the list of purchase orders displayed in the table.
	 */
	private void refreshPurchaseOrders() {
		try {
			List<PurchaseOrder> purchaseOrders = poService.findAllPurchaseOrders();
			poList.setAll(purchaseOrders);
		} catch (SQLException e) {
			showError("Error loading purchase orders: " + e.getMessage());
		}
	}

	/**
	 * Handles the creation of a new purchase order based on the selected supplier and notes.
	 * Validates the input and updates the purchase orders table upon successful creation.
	 */
	@FXML
	private void handleCreatePO() {
		Supplier selected = supplierChoiceBox.getValue();
		if (selected == null) {
			showError("Please select a supplier.");
			return;
		}
		int selectedSupplier = selected.getSupplierId();
		String notes = notesField.getText();
		int createdBy = Session.getCurrentUser().getUserId();
		try {
			poService.createPurchaseOrder(selectedSupplier, createdBy, notes);
			showSuccess("Purchase order created successfully.");
			clearForm();
			refreshPurchaseOrders();
		} catch (SQLException e) {
			showError("Database error: " + e.getMessage());
		}
	}

	/**
	 * Handles marking a purchase order as received.
	 */
	@FXML
	private void handleReceivePO() {
		if (selectedPO == null) {
			showError("Purchase order not found");
			return;
		}
		if (!ConfirmDialog.confirm("Receive Purchase Order", "Receive PO #" + selectedPO.getPoId() + "?")) {
			return;
		}
		try {
			poService.receivePurchaseOrder(selectedPO.getPoId());
			Optional<PurchaseOrder> freshPO = poService.findPurchaseOrderById(selectedPO.getPoId());
			if (freshPO.isPresent()) {
				selectedPO = freshPO.get();
			}
			loadLineItems(selectedPO);
			updateStatusButtons(selectedPO);
			refreshPurchaseOrders();
			showSuccess("Purchase order received");
		} catch (IllegalArgumentException e) {
			showError(e.getMessage());
		} catch (SQLException e) {
			showError("Database error: " + e.getMessage());
		}
	}

	/**
	 * Handles canceling a purchase order.
	 */
	@FXML 
	private void handleCancelPO() {
		if (selectedPO == null) {
			showError("Purchase order not found");
			return;
		}
		if (!ConfirmDialog.confirm("Cancel Purchase Order", "Cancel PO #" + selectedPO.getPoId() + "?")) {
			return;
		}
		try {
			poService.cancelPurchaseOrder(selectedPO.getPoId());
			Optional<PurchaseOrder> freshPO = poService.findPurchaseOrderById(selectedPO.getPoId());
			if (freshPO.isPresent()) {
				selectedPO = freshPO.get();
			}
			loadLineItems(selectedPO);
			updateStatusButtons(selectedPO);
			refreshPurchaseOrders();
			showSuccess("Purchase order canceled");			
		} catch (IllegalArgumentException e) {
			showError(e.getMessage());
		} catch (SQLException e) {
			showError("Database error: " + e.getMessage());
		}		
	}


	/**
	 * Disables the receive and cancel buttons in a purchase order that isn't open.
	 * @param po	The purchase order I.D. 
	 */
	private void updateStatusButtons(PurchaseOrder po) {
		boolean isOpen = "OPEN".equals(po.getCurrentStatus());
		receiveButton.setDisable(!isOpen);
		cancelButton.setDisable(!isOpen);
	}

	private void clearForm() {
		supplierChoiceBox.getSelectionModel().clearSelection();
		notesField.clear();
	}

	private void showSuccess(String message) {
		messageLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");
		messageLabel.setText(message);	
	}

	private void showError(String message) {
		messageLabel.setText(message);
		messageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: red;");				
	}

}
