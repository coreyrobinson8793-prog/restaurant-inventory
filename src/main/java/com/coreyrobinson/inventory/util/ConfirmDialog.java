/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Displays a confirmation dialog when making consequential changes.
 * DATE: 08/06/26
 */
package com.coreyrobinson.inventory.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public final class ConfirmDialog {
	
	/**
	 * Displays a confirmation dialog with the specified title and message.
	 * @param title	The title of the dialog.
	 * @param message	The message to display in the dialog.
	 * @return	true if the user clicked OK, false otherwise.
	 */
	public static boolean confirm(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(null); 	// remove header
		alert.setContentText(message);
		Optional<ButtonType> result = alert.showAndWait();
		return result.isPresent() && result.get() == ButtonType.OK;
	}
	
	private ConfirmDialog() {
		
	}
	
}
