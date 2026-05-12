/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Handles the log in logic for the application.
 * DATE: 05/09/2026
 */
package com.coreyrobinson.inventory.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import com.coreyrobinson.inventory.model.User;
import com.coreyrobinson.inventory.service.UserService;
import com.coreyrobinson.inventory.session.Session;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	
	@FXML private TextField usernameField;
	@FXML private PasswordField passwordField;
	@FXML private Label messageLabel;
	private final UserService userService = new UserService();
	
	@FXML
	private void handleLogin() {
		String username = usernameField.getText();
		String password = passwordField.getText();
		try {
			Optional<User> result = userService.authenticate(username, password);
			if (result.isPresent()) {
				User loggedInUser = result.get();
				Session.setCurrentUser(loggedInUser);
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
				Parent root = loader.load();
				Stage stage = (Stage) usernameField.getScene().getWindow();
				stage.setScene(new Scene(root));
			} else {
				messageLabel.setText("Invalid username or password.");
				messageLabel.setStyle("-fx-text-fill: red;");
			}
		} catch (SQLException | IOException e) {
			messageLabel.setText("Database error. Please try again.");
			messageLabel.setStyle("-fx-text-fill: red;");
			e.printStackTrace();
		}
	}

}
