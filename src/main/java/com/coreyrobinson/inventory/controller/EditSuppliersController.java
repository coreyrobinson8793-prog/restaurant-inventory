/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Handles the logic of the edit suppliers window.
 * DATE: 05/30/26
 */
package com.coreyrobinson.inventory.controller;

import java.sql.SQLException;

import com.coreyrobinson.inventory.model.Supplier;
import com.coreyrobinson.inventory.service.SupplierService;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditSuppliersController {

	@FXML private TextField supplierNameField;
	@FXML private TextField repNameField;
	@FXML private TextField phoneField;
	@FXML private TextField emailField;
	@FXML private TextField addressField;
	@FXML private Label messageLabel;
	private SupplierService supplierService = new SupplierService();
	private Supplier supplierBeingEdited;	
	private boolean saved = false;

	public void setSupplier(Supplier supplier) {
		this.supplierBeingEdited = supplier;
		if (supplier != null) {
			supplierNameField.setText(supplier.getSupplierName());
			repNameField.setText(supplier.getRepName());
			phoneField.setText(supplier.getPhone());
			emailField.setText(supplier.getEmail());
			addressField.setText(supplier.getAddress());
		}
	}

	@FXML
	private void handleSave() {
		String name = supplierNameField.getText().trim();
		String repName = repNameField.getText().trim();
		String phone = phoneField.getText().trim();
		String email = emailField.getText().trim();
		String address = addressField.getText().trim();
		try {
			supplierBeingEdited.setSupplierName(name);
			supplierBeingEdited.setRepName(repName);
			supplierBeingEdited.setPhone(phone);
			supplierBeingEdited.setEmail(email);
			supplierBeingEdited.setAddress(address);
			supplierService.updateSupplier(supplierBeingEdited);
			saved = true;
			Stage stage = (Stage) supplierNameField.getScene().getWindow();
			stage.close();
		} catch (IllegalArgumentException e) {
			showError(e.getMessage());
		} catch (SQLException e) {
			showError("Database error: " + e.getMessage());
		}
	}
	
	@FXML
	private void handleCancel() {
		Stage stage = (Stage) supplierNameField.getScene().getWindow();
		stage.close();		
	}
	
	public boolean isSaved() {
		return saved;
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
