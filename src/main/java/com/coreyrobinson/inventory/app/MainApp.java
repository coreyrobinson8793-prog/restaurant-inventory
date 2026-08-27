/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Starts the restaurant inventory application.
 * DATE: 05/09/2026
 */
package com.coreyrobinson.inventory.app;

import java.io.IOException;
import java.sql.SQLException;

import com.coreyrobinson.inventory.dao.UserDAO;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MainApp extends Application {

	UserDAO userDao = new UserDAO();


	@Override
	public void start(Stage primaryStage) throws IOException {
		boolean managerUser;
		try {
			managerUser = userDao.managerExists();
		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Database Connection Failed");
			alert.setHeaderText("Cannot connect to the database");
			alert.setContentText("Check that MySQL is running and database.properties is correct.");
			alert.showAndWait();
			Platform.exit();
			return;
		}
		String fxmlPath = managerUser ? "/fxml/Login.fxml" : "/fxml/Setup.fxml";

		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Tailgate Tavern - Inventory Management");
		scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
