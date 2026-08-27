/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Handles the logic for creating a manager if none exist.
 * DATE: 08/21/2026
 */
package com.coreyrobinson.inventory.controller;

import java.io.IOException;
import java.sql.SQLException;
import com.coreyrobinson.inventory.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SetupController {

	@FXML private TextField usernameField;
	@FXML private PasswordField passwordField;
	@FXML private PasswordField confirmPasswordField;
	@FXML private Label messageLabel;
	private final UserService userService = new UserService();

	@FXML
	private void handleCreateManager() {
		String username = usernameField.getText().trim();
		String password = passwordField.getText();
		try {
			if (username.isBlank() || password.isBlank()) {
				messageLabel.setText("Username and password cannot be empty.");
				return;
			}
			if (!password.equals(confirmPasswordField.getText())) {
				messageLabel.setText("Passwords do not match.");
				return;
			}
			userService.registerUser(username, password, "Manager");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
			Parent root = loader.load();
			usernameField.getScene().setRoot(root);
		} catch (SQLException | IOException e) {
			messageLabel.setText("Database error. Please try again.");
			messageLabel.setStyle("-fx-text-fill: red;");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			messageLabel.setText(e.getMessage());
			messageLabel.setStyle("-fx-text-fill: red;");
		}
	}
}
