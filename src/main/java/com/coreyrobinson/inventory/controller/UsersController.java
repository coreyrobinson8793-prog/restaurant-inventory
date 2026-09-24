/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Handles the logic on the users screen.
 * DATE: 05/14/2026
 */
package com.coreyrobinson.inventory.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.coreyrobinson.inventory.model.User;
import com.coreyrobinson.inventory.service.UserService;
import com.coreyrobinson.inventory.session.Session;
import com.coreyrobinson.inventory.util.ConfirmDialog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;




public class UsersController {

	@FXML private TextField usernameField;
	@FXML private PasswordField passwordField;
	@FXML private ChoiceBox<String> roleChoiceBox;
	@FXML private Label messageLabel;
	@FXML private TableView<User> usersTable;
	@FXML private TableColumn<User, String> usernameColumn;
	@FXML private TableColumn<User, String> positionColumn;
	@FXML private TableColumn<User, String> activeColumn;
	@FXML private TableColumn<User, String> createdAtColumn;
	@FXML private TableColumn<User, Void> actionsColumn;
	private UserService userService = new UserService();
	private ObservableList<User> usersList = FXCollections.observableArrayList();

	@FXML
	private void initialize() {
		roleChoiceBox.getItems().addAll("Manager", "Staff");
		roleChoiceBox.setValue("Staff");	// default
		usernameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getUsername()));
		positionColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPosition()));
		activeColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().isActive() ? "Active" : "Inactive"));
		// formats the date in a more readable format
		createdAtColumn.setCellValueFactory(cellData -> {
			LocalDateTime created = cellData.getValue().getCreatedAt();
			String formatted = created == null ? "" : created.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));	
			return new SimpleStringProperty(formatted);
		});
		actionsColumn.setCellFactory(column -> new TableCell<User, Void>() {	// Each cell creates a button.
			private final Button deactivateButton = new Button("Deactivate");
			private final Button resetPasswordButton = new Button("Reset Password");
			private final HBox buttons = new HBox(5, deactivateButton, resetPasswordButton);
			{
				deactivateButton.getStyleClass().add("action-button");
				deactivateButton.setOnAction(event -> {	// Gets the user from that row and calls "handleDeactivate".
					User user = getTableView().getItems().get(getIndex());
					handleDeactivate(user);
				});
				resetPasswordButton.getStyleClass().add("action-button");
				resetPasswordButton.setOnAction(event -> {	// Gets the user from that row and calls "handleResetPassword".
					User user = getTableView().getItems().get(getIndex());
					handleResetPassword(user);
				});
			}
			@Override
			/**
			 * Only shows the button on non-empty rows.
			 * @param item	The row.
			 * @param empty	If the row is empty or not.
			 */
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(buttons);
				}
			}

		});
		usersTable.setItems(usersList);
		refreshUsers();
	}

	private void showSuccess(String message) {
		messageLabel.setText(message);
		messageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: green;");
	}

	private void showError(String message) {
		messageLabel.setText(message);
		messageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: red;");
	}

	private void refreshUsers() {
		try {
			List<User> users = userService.findAllUsers();
			usersList.setAll(users);
		} catch (SQLException e) {
			showError("Couldn't load users.");
			e.printStackTrace();
		}		
	}

	@FXML
	private void handleAddUser() {
		String username = usernameField.getText();
		String password = passwordField.getText();
		String role = roleChoiceBox.getValue();
		try {
			userService.registerUser(username, password, role);
			showSuccess("User '" + username + "' added.");
			usernameField.clear();
			passwordField.clear();
			refreshUsers();
		} catch (IllegalArgumentException e) {
			showError(e.getMessage()); 
		} catch (SQLException e) {
			showError("Could not add user.");
			e.printStackTrace();
		}
	}

	private void handleDeactivate(User user) {
		int loggedInUserId = Session.getCurrentUser().getUserId();
		if (!ConfirmDialog.confirm("Deactivate User", "Deactivate '" + user.getUsername() + "'?")) {
			return;
		}
		try {
			userService.deactivateUser(user.getUserId(), loggedInUserId);
			showSuccess("User '" + user.getUsername() + "' deactivated.");
			refreshUsers();
		} catch (IllegalArgumentException e) {
			showError(e.getMessage());
		} catch (SQLException e) {
			showError("Could not deactivate user.");
			e.printStackTrace();
		}
	}

	/**
	 * Opens a modal dialog for resetting the password of the specified user.
	 * @param user The user whose password is to be reset.
	 */
	private void handleResetPassword(User user) {
		if (!ConfirmDialog.confirm("Reset Password", "Reset the password for '" + user.getUsername() + "'?")) {
			return;
		}		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ResetPassword.fxml"));
			Parent root = loader.load();
			ResetPasswordController controller = loader.getController();
			controller.setTargetUser(user);
			Stage dialog = new Stage();
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.initOwner(usersTable.getScene().getWindow());
			dialog.setTitle("Reset Password");
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}				
}


