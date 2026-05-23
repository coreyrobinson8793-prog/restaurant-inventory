/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Handles the logic of the edit item window.
 * DATE: 05/22/26
 */
package com.coreyrobinson.inventory.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.coreyrobinson.inventory.dao.CategoryDAO;
import com.coreyrobinson.inventory.dao.UnitDAO;
import com.coreyrobinson.inventory.model.Category;
import com.coreyrobinson.inventory.model.Item;
import com.coreyrobinson.inventory.model.Unit;
import com.coreyrobinson.inventory.service.ItemService;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class EditItemController {
	
	@FXML private TextField nameField;
	@FXML private ChoiceBox<Unit> unitChoiceBox;
	@FXML private TextField currentStockField;
	@FXML private TextField parstockField;
	@FXML private ChoiceBox<Category> categoryChoiceBox;
	@FXML private Label messageLabel;
	private CategoryDAO catDao = new CategoryDAO();
	private UnitDAO unitDao = new UnitDAO();
	private ItemService itemService = new ItemService();
	private Item itemBeingEdited;	// item being edited
	private boolean saved = false;
	
	@FXML
	private void initialize() {
		try {
			List<Unit> units = unitDao.findAll();	// loads units into drop-down
			unitChoiceBox.setConverter(new StringConverter<Unit>() {
				@Override
				public String toString(Unit unit) {
					return unit == null ? "" : unit.getUnitName();
				}
				@Override
				public Unit fromString(String s) {
					return null;
				}
			});
			unitChoiceBox.getItems().setAll(units);
			List<Category> cats = catDao.findAll();	// loads categories into drop-down
			categoryChoiceBox.setConverter(new StringConverter<Category>() {
				@Override
				public String toString(Category cat) {
					return  cat == null ? "" : cat.getCategoryName();
				}
				@Override
				public Category fromString(String s) {
					return null;
				}
			});
			categoryChoiceBox.getItems().setAll(cats);
		} catch (SQLException e) {
			showError("Failed to load form data: " + e.getMessage());
		}		
	}

	public void setItem(Item item) {
		this.itemBeingEdited = item;
		nameField.setText(item.getItemName());
		currentStockField.setText(item.getCurrentStock().toString());
		parstockField.setText(item.getParstock().toString());
		for (Unit u : unitChoiceBox.getItems()) {
			if (u.getUnitId() == item.getUnitId()) {
				unitChoiceBox.setValue(u);
				break;
			}
		}
		for (Category c : categoryChoiceBox.getItems()) {
			if (c.getCategoryId() == item.getCategoryId()) {
				categoryChoiceBox.setValue(c);
				break;
			}
		}
	}
	
	@FXML
	public void handleSave() {
		String itemName = nameField.getText();
		Unit unit = unitChoiceBox.getValue();
		Category cat = categoryChoiceBox.getValue();
		String currentStock = currentStockField.getText();
		String currentParstock = parstockField.getText();
		if (cat == null || unit == null) {
			showError("Please select a unit and category");
			return;
		}
		try {
			BigDecimal stock = new BigDecimal(currentStock);
			BigDecimal parstock = new BigDecimal(currentParstock);
			itemBeingEdited.setItemName(itemName);
			itemBeingEdited.setUnitId(unit.getUnitId());
			itemBeingEdited.setCategoryId(cat.getCategoryId());
			itemBeingEdited.setCurrentStock(stock);
			itemBeingEdited.setParstock(parstock);
			itemService.updateItem(itemBeingEdited);
			saved = true;
			Stage stage = (Stage) nameField.getScene().getWindow();
			stage.close();
		} catch (NumberFormatException e) {
			showError("Stock and parstock must be numbers");
		} catch (IllegalArgumentException e) {
			showError(e.getMessage());
		} catch (SQLException e) {
			showError("Database error: " + e.getMessage());
		}
	}
	
	@FXML
	public void handleCancel() {
		Stage stage = (Stage) nameField.getScene().getWindow();
		stage.close();		
	}
	
	public boolean isSaved() {
		return saved;
	}
	
	/**
	 * Shows an error message.
	 * @param message	The error message.
	 */	
	private void showError(String message) {
		messageLabel.setText(message);
		messageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: red;");
	}
	
}
