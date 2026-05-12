/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Starts the restaurant inventory application.
 * DATE: 05/09/2026
 */
package com.coreyrobinson.inventory.app;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Tailgate Tavern - Inventory Management");
		primaryStage.setScene(scene);
		primaryStage.show();		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
