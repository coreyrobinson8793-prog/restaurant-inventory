/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Handles the logic for resetting a user's password.
 * DATE: 09/24/2026
 */
package com.coreyrobinson.inventory.controller;

import java.sql.SQLException;

import com.coreyrobinson.inventory.model.User;
import com.coreyrobinson.inventory.service.UserService;
import com.coreyrobinson.inventory.session.Session;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class ResetPasswordController {

	@FXML private Label usernameLabel;
	@FXML private PasswordField newPasswordField;
	@FXML private PasswordField confirmNewPasswordField;
	@FXML private Label messageLabel;
	private User targetUser;
	private final UserService userService = new UserService();

	/**
	 * Sets the target user for password reset and updates the username label accordingly.
	 * @param user The user whose password is to be reset.
	 */
	public void setTargetUser(User user) {
		this.targetUser = user;
		usernameLabel.setText("Resetting password for: " + user.getUsername());
	}

	/**
	 * Handles the password reset process. Validates input fields, checks for matching passwords,
	 * and updates the target user's password in the database. Displays success or error messages accordingly.
	 */
	@FXML
	private void handleResetPassword() {
		String newPassword = newPasswordField.getText();
		String confirmPassword = confirmNewPasswordField.getText();
		if (newPassword.isBlank() || confirmPassword.isBlank()) {
			messageLabel.setText("Password fields cannot be empty.");
			messageLabel.setStyle("-fx-text-fill: red;");
			return;
		}
		if (!newPassword.equals(confirmPassword)) {
			messageLabel.setText("Passwords do not match.");
			messageLabel.setStyle("-fx-text-fill: red;");
			return;
		}
		if (targetUser == null) {
			messageLabel.setText("Target user not set.");
			messageLabel.setStyle("-fx-text-fill: red;");
			return;
		}		
		try {
			int managerId = Session.getCurrentUser().getUserId();
			userService.resetPassword(managerId, targetUser.getUserId(), newPassword);
			messageLabel.setText("Password reset successfully.");
			messageLabel.setStyle("-fx-text-fill: green;");
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
		((Stage) newPasswordField.getScene().getWindow()).close();
	}	
	
}





