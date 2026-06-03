/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Handles the logic on the suppliers screen.
 * DATE: 05/30/26
 */
package com.coreyrobinson.inventory.controller;




import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coreyrobinson.inventory.dao.UnitDAO;
import com.coreyrobinson.inventory.model.Item;
import com.coreyrobinson.inventory.model.ItemSupplier;
import com.coreyrobinson.inventory.model.Supplier;
import com.coreyrobinson.inventory.model.Unit;
import com.coreyrobinson.inventory.service.ItemService;
import com.coreyrobinson.inventory.service.SupplierService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class SuppliersController {

	@FXML private TextField supplierNameField;
	@FXML private TextField repNameField;
	@FXML private TextField phoneField;
	@FXML private TextField emailField;
	@FXML private TextField addressField;
	@FXML private Label messageLabel;
	@FXML private TableView<Supplier> suppliersTable;
	@FXML private TableColumn<Supplier, String> supplierNameColumn;
	@FXML private TableColumn<Supplier, String> repNameColumn;
	@FXML private TableColumn<Supplier, String> phoneColumn;
	@FXML private TableColumn<Supplier, String> emailColumn;
	@FXML private TableColumn<Supplier, String> addressColumn;
	@FXML private TableColumn<Supplier, Void> actionsColumn;
	@FXML private TableColumn<Supplier, String> activeColumn;
	@FXML private Label detailHeaderLabel;
	@FXML private TableView<ItemSupplier> supplierItemsTable;
	@FXML private TableColumn<ItemSupplier, String> detailItemNameColumn;
	@FXML private TableColumn<ItemSupplier, String> detailSkuColumn;
	@FXML private TableColumn<ItemSupplier, String> detailPriceColumn;
	@FXML private TableColumn<ItemSupplier, String> detailPackSizeColumn;
	@FXML private TableColumn<ItemSupplier, String> detailPreferredColumn;
	@FXML private TableColumn<ItemSupplier, Void> detailActionsColumn;
	@FXML private ChoiceBox<Item> linkItemChoiceBox;
	@FXML private TextField linkSkuField;
	@FXML private TextField linkPriceField;
	@FXML private TextField linkPackSizeField;
	@FXML private ChoiceBox<Unit> linkPackUnitChoiceBox;
	private SupplierService supplierService = new SupplierService();
	private ItemService itemService = new ItemService();
	private UnitDAO unitDao = new UnitDAO();
	private ObservableList<ItemSupplier> supplierItemsList = FXCollections.observableArrayList();
	private ObservableList<Supplier> suppliersList = FXCollections.observableArrayList();
	private Map<Integer, String> itemNameMap;
	private Supplier selectedSupplier;

	@FXML
	public void initialize() {
		// Load item names for display
		try {
			List<Item> items = itemService.findAllItems();
			itemNameMap = new HashMap<>();
			for (Item item : items) {
				itemNameMap.put(item.getItemId(), item.getItemName());
			}
			linkItemChoiceBox.setConverter(new StringConverter<Item>() {
				@Override
				public String toString(Item item) {
					return item == null ? "" : item.getItemName();
				}
				@Override
				public Item fromString(String s) {
					return null;
				}
			});
			linkItemChoiceBox.getItems().setAll(items);
			List<Unit> packUnits = unitDao.findAll();
			linkPackUnitChoiceBox.setConverter(new StringConverter<Unit>() {
				@Override
				public String toString(Unit unit) {
					return unit == null ? "" : unit.getUnitName();
				}
				@Override
				public Unit fromString(String s) {
					return null;
				}
			});
			linkPackUnitChoiceBox.getItems().setAll(packUnits);
		} catch (SQLException e) {
			showError("Error loading items: " + e.getMessage());
		}
		// Simple direct-field columns
		supplierNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSupplierName()));
		repNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRepName()));
		phoneColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPhone()));
		emailColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));
		addressColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAddress()));
		activeColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().isActive() ? "Active" : "Inactive"));
		detailItemNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(itemNameMap.getOrDefault(cell.getValue().getItemId(), "?")));
		detailSkuColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSupplierSku()));
		detailPriceColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPrice().toString()));
		detailPackSizeColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPackSize().toString()));
		detailPreferredColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().isPreferred() ? "Yes" : "No"));
		detailActionsColumn.setCellFactory(column -> new TableCell<ItemSupplier, Void>() {
			private final Button preferredButton = new Button("Preferred");
			private final Button unlinkButton = new Button("Unlink");
			private final HBox buttons = new HBox(5, preferredButton, unlinkButton);
			{
				preferredButton.setOnAction(event -> {
					ItemSupplier link = getTableView().getItems().get(getIndex());
					handleSetPreferred(link);
				});
				unlinkButton.setOnAction(event -> {
					ItemSupplier link = getTableView().getItems().get(getIndex());
					handleUnlink(link);
				});
			}
			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				setGraphic(empty ? null : buttons);
			}
		});
		actionsColumn.setCellFactory(column -> new TableCell<Supplier, Void>() {
			private final Button editButton = new Button("Edit");
			private final Button deactivateButton = new Button("Deactivate");
			private final HBox buttons = new HBox(5, editButton, deactivateButton);
			{
				editButton.setOnAction(event -> {
					Supplier supplier = getTableView().getItems().get(getIndex());
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditSupplierView.fxml"));
						Parent root = loader.load();
						EditSuppliersController controller = loader.getController();
						controller.setSupplier(supplier);
						Stage dialog = new Stage();
						dialog.initModality(Modality.APPLICATION_MODAL);
						dialog.setScene(new Scene(root));
						dialog.setTitle("Edit Supplier");
						dialog.showAndWait();
						if (controller.isSaved()) {
							refreshSuppliers();
						}
					} catch (IOException e) {
						showError("Could not open edit dialog");
						e.printStackTrace();
					}
				});
				deactivateButton.setOnAction(event -> {
					Supplier supplier = getTableView().getItems().get(getIndex());
					handleDeactivate(supplier);
				});

			}
			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(buttons);
				}
			}
		});
		suppliersTable.setItems(suppliersList);
		refreshSuppliers();
		supplierItemsTable.setItems(supplierItemsList);
		suppliersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				selectedSupplier = newSelection;
				loadSupplierItems(newSelection);
			}
		});
	}

	/**
	 * Loads the items supplied by the selected supplier and updates the detail view.
	 * @param supplier	The selected supplier.
	 */
	private void loadSupplierItems(Supplier supplier) {
		detailHeaderLabel.setText("Items supplied by " + supplier.getSupplierName());
		try {
			List<ItemSupplier> links = supplierService.findItemsForSupplier(supplier.getSupplierId());
			links.removeIf(link -> !link.isActive());
			supplierItemsList.setAll(links);
		} catch (SQLException e) {
			showError("Error loading supplier items: " + e.getMessage());
		}
	}

	private void refreshSuppliers() {
		try {
			List<Supplier> suppliers = supplierService.findAllSuppliers();
			suppliers.removeIf(s -> !s.isActive());
			suppliersList.setAll(suppliers);
		} catch (SQLException e) {
			showError("Error loading suppliers: " + e.getMessage());
		}
	}

	/**
	 * Shows a success message
	 * @param message	The success message.
	 */	
	private void showSuccess(String message) {
		messageLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");
		messageLabel.setText(message);		
	}

	/**
	 * Shows an error message.
	 * @param message	The error message.
	 */
	private void showError(String message) {
		messageLabel.setText(message);
		messageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: red;");		
	}

	/**
	 * Handles deactivating a supplier.
	 * @param supplier	The supplier to deactivate.
	 */
	private void handleDeactivate(Supplier supplier) {
		try {
			supplierService.deactivateSupplier(supplier.getSupplierId());
			showSuccess("Supplier '" + supplier.getSupplierName() + "' deactivated.");
			refreshSuppliers();
		} catch(IllegalArgumentException e) {
			showError(e.getMessage());
		} catch (SQLException e) {
			showError("Could not deactivate supplier: " + e.getMessage());
		}
	}

	@FXML
	private void handleAddSupplier() {
		String supplierName = supplierNameField.getText();
		String repName = repNameField.getText();
		String phone = phoneField.getText();
		String email = emailField.getText();
		String address = addressField.getText();
		try {
			supplierService.addSupplier(supplierName, repName, phone, email, address);
			showSuccess("Supplier '" + supplierName + "' added.");
			clearForm();
			refreshSuppliers();
		} catch (IllegalArgumentException e) {
			showError(e.getMessage());
		} catch (SQLException e) {
			showError("Database error: " + e.getMessage());
		}
	}
	
	@FXML 
	private void handleLinkItem() {
		if (selectedSupplier == null) {
			showError("Please select a supplier first");
			return;
		}
		Item item = linkItemChoiceBox.getValue();
		String sku = linkSkuField.getText();
		Unit packUnit = linkPackUnitChoiceBox.getValue();
		if (item == null || packUnit == null) {
			showError("Please select an item and pack unit");
			return;
		}
		try {
			BigDecimal price = new BigDecimal(linkPriceField.getText());
			BigDecimal packSize = new BigDecimal(linkPackSizeField.getText());
			supplierService.linkItemToSupplier(item.getItemId(), selectedSupplier.getSupplierId(), sku, price, packSize, packUnit.getUnitId());
			showSuccess("Item linked to supplier");
			loadSupplierItems(selectedSupplier);
			linkItemChoiceBox.setValue(null);
			linkSkuField.clear();
			linkPriceField.clear();
			linkPackSizeField.clear();
			linkPackUnitChoiceBox.setValue(null);
		} catch (NumberFormatException e) {
			showError("Price and pack size must be numbers");
		} catch (IllegalArgumentException e) {
			showError(e.getMessage());
		} catch (SQLException e) {
			showError("Database error: " + e.getMessage());
		}
	}
	
	/**
	 * Handles marking a supplier-item link as preferred.
	 * @param link	The supplier-item link to mark preferred.
	 */
	private void handleSetPreferred(ItemSupplier link) {
		try {
			supplierService.setPreferredSupplier(link.getItemId(), link.getItemSupplierId());
			showSuccess("Supplier marked preferred");
			loadSupplierItems(selectedSupplier);
		} catch (IllegalArgumentException e) {
			showError(e.getMessage());
		} catch (SQLException e) {
			showError("Database error: " + e.getMessage());
		}
	}
	
	/**
	 * Handles unlinking an item from a supplier.
	 * @param link	The supplier-item link to deactivate.
	 */
	private void handleUnlink(ItemSupplier link) {
		try {
			supplierService.deactivateItemSupplier(link.getItemSupplierId());
			showSuccess("Item unlinked from supplier");
			loadSupplierItems(selectedSupplier);
		} catch (IllegalArgumentException e) {
			showError(e.getMessage());
		} catch (SQLException e) {
			showError("Database error: " + e.getMessage());
		}
	}

	private void clearForm() {
		supplierNameField.clear();
		repNameField.clear();
		phoneField.clear();
		emailField.clear();
		addressField.clear();
	}
}



