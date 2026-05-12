/*
 * AUTHOR: Corey Robinson
 * PURPOSE: 
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
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainController {
	
	@FXML private Label welcomeLabel;
	@FXML private Label roleLabel;
	
	@FXML 
	private void initialize() {
		User currentUser = Session.getCurrentUser();
		if (currentUser != null) {
			welcomeLabel.setText("Welcome " + currentUser.getUsername() + ".");
			roleLabel.setText("Logged in as " + currentUser.getPosition() + ".");
		}
	}
	@FXML
	private void handleLogout (ActionEvent event) throws IOException {
		Session.clear();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
		Parent root = loader.load();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(root));
	}

}
