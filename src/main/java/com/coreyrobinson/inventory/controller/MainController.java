/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Handles the logic on the main screen.
 * DATE: 05/10/2026
 */
package com.coreyrobinson.inventory.controller;

import java.io.IOException;

import com.coreyrobinson.inventory.model.User;
import com.coreyrobinson.inventory.session.Session;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {

	@FXML private Label currentUserLabel;
	@FXML private StackPane contentArea;
	@FXML private Button usersButton;
	@FXML private Button priceImportsButton;

	@FXML 
	private void initialize() {
		User currentUser = Session.getCurrentUser();
		if (currentUser != null) {
			currentUserLabel.setText("Logged in as " + currentUser.getUsername() + " (" + currentUser.getPosition() + ").");
			if ("Staff".equals(currentUser.getPosition())) {
				usersButton.setVisible(false);
				usersButton.setManaged(false);
				priceImportsButton.setVisible(false);
				priceImportsButton.setManaged(false);
			}
			loadView("/fxml/InventoryView.fxml");
		}
	}

	/**
	 * Loads an FXML view into the content area.
	 * @param fxmlPath	Classpath to the FXML file.
	 */
	private void loadView(String fxmlPath) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
			Parent view = loader.load();
			contentArea.getChildren().setAll(view);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Opens a modal dialog for changing the user's password.
	 */
	@FXML
	private void handleChangePassword() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChangePassword.fxml"));
			Parent root = loader.load();
			Stage dialog = new Stage();
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.initOwner(currentUserLabel.getScene().getWindow());
			dialog.setTitle("Change Password");
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.showAndWait();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	}

	@FXML
	private void handleLogout(ActionEvent event) throws IOException {
		Session.clear();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
		Parent root = loader.load();
		((Node) event.getSource()).getScene().setRoot(root);
	}

	@FXML
	private void handleNavInventory() {
		loadView("/fxml/InventoryView.fxml");
	}

	@FXML 
	private void handleNavSuppliers() {
		loadView("/fxml/SuppliersView.fxml");
	}

	@FXML
	private void handleNavOrders() {
		loadView("/fxml/PurchaseOrdersView.fxml");
	}

	@FXML 
	private void handleNavUsers() {
		loadView("/fxml/UsersView.fxml");
	}

	@FXML
	private void handleNavReports() {
		loadView("/fxml/ReportsView.fxml");
	}
	
	@FXML
	private void handleNavImports() {
		loadView("/fxml/PriceImportView.fxml");
	}

}
