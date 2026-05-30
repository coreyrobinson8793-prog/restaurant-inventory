/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Handles the logic on the suppliers screen.
 * DATE: 05/30/26
 */
package com.coreyrobinson.inventory.controller;




import java.sql.SQLException;
import java.util.List;

import com.coreyrobinson.inventory.model.Supplier;
import com.coreyrobinson.inventory.service.SupplierService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

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
	private SupplierService supplierService = new SupplierService();
	private ObservableList<Supplier> suppliersList = FXCollections.observableArrayList();

	@FXML
	public void initialize() {
		// Simple direct-field columns
		supplierNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSupplierName()));
		repNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRepName()));
		phoneColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPhone()));
		emailColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));
		addressColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAddress()));
		activeColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().isActive() ? "Active" : "Inactive"));
		actionsColumn.setCellFactory(column -> new TableCell<Supplier, Void>() {
			private final Button editButton = new Button("Edit");
			private final Button deactivateButton = new Button("Deactivate");
			private final HBox buttons = new HBox(5, editButton, deactivateButton);
			{
				editButton.setOnAction(event -> {
					// TODO: open edit dialog in P2
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

	private void clearForm() {
		supplierNameField.clear();
		repNameField.clear();
		phoneField.clear();
		emailField.clear();
		addressField.clear();
	}
}



