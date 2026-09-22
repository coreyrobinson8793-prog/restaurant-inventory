/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Handles the logic for changing a user's password.
 * DATE: 09/22/2026
 */
package com.coreyrobinson.inventory.controller;

import java.sql.SQLException;

import com.coreyrobinson.inventory.service.UserService;
import com.coreyrobinson.inventory.session.Session;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class ChangePasswordController {

	@FXML private PasswordField currentPasswordField;
	@FXML private PasswordField newPasswordField;
	@FXML private PasswordField confirmNewPasswordField;
	@FXML private Label messageLabel;
	private final UserService userService = new UserService();

	/**
	 * Handles the password change process. Validates input fields, checks for matching passwords,
	 * and updates the user's password in the database. Displays success or error messages accordingly.
	 */
	@FXML
	private void handleChangePassword() {
		String currentPassword = currentPasswordField.getText();
		String newPassword = newPasswordField.getText();
		String confirmPassword = confirmNewPasswordField.getText();
		if (currentPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
			messageLabel.setText("Password fields cannot be empty.");
			messageLabel.setStyle("-fx-text-fill: red;");
			return;
		}
		if (!newPassword.equals(confirmPassword)) {
			messageLabel.setText("Passwords do not match.");
			messageLabel.setStyle("-fx-text-fill: red;");
			return;
		}
		try {
			int userId = Session.getCurrentUser().getUserId();
			userService.changePassword(userId, currentPassword, newPassword);
			messageLabel.setText("Password changed successfully.");
			messageLabel.setStyle("-fx-text-fill: green;");
			currentPasswordField.clear();
			newPasswordField.clear();
			confirmNewPasswordField.clear();
		} catch (SQLException e) {
			messageLabel.setText("Database error. Please try again.");
			messageLabel.setStyle("-fx-text-fill: red;");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			messageLabel.setText(e.getMessage());
			messageLabel.setStyle("-fx-text-fill: red;");
		}
	}

	@FXML
	private void handleCancel() {
		((Stage) currentPasswordField.getScene().getWindow()).close();
	}
}
