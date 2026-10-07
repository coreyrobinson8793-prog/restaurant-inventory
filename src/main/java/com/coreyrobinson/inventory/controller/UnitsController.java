/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Handles the logic on the units of measurement screen.
 * DATE: 10/07/26
 */
package com.coreyrobinson.inventory.controller;

import java.sql.SQLException;
import java.util.List;

import com.coreyrobinson.inventory.model.Unit;
import com.coreyrobinson.inventory.service.UnitService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;

public class UnitsController {
	
	@FXML private Label messageLabel;
	@FXML private TextField unitField;
	@FXML private TableView<Unit> unitsTable;
	@FXML private TableColumn<Unit, String> unitNameColumn;
	private UnitService unitService = new UnitService();
	private ObservableList<Unit> unitsList = FXCollections.observableArrayList();
	
	
	@FXML
	public void initialize() {
		unitNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getUnitName()));
		unitNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		unitNameColumn.setOnEditCommit(event -> {
			Unit unit = event.getRowValue();
			String newName = event.getNewValue();
			try {
				unitService.updateUnit(unit.getUnitId(), newName);
				showSuccess("Unit updated successfully.");
			} catch (IllegalArgumentException e) {
				showError(e.getMessage());
			} catch (SQLException e) {
				showError("Error updating unit: " + e.getMessage());
			}
			loadUnits();
		});
		unitsTable.setItems(unitsList);
		loadUnits();
	}

	private void loadUnits() {
		try {
			List<Unit> units = unitService.findAllUnits();
			unitsList.setAll(units);
		} catch (SQLException e) {
			showError("Error loading units: " + e.getMessage());
		}
	}
	
	private void showSuccess(String message) {
		messageLabel.setText(message);
		messageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: green;");
	}

	private void showError(String message) {
		messageLabel.setText(message);
		messageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: red;");
	}
	
	@FXML
	private void handleAddUnit() {
		String unitName = unitField.getText();
		try {
			unitService.addUnit(unitName);
			showSuccess("Unit '" + unitName + "' added.");
			unitField.clear();
			loadUnits();
		} catch (IllegalArgumentException e) {
			showError(e.getMessage());
		} catch (SQLException e) {
			showError("Could not add unit: " + e.getMessage());
		}
	}
}
