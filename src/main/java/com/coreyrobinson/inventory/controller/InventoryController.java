/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Handles the logic on the inventory screen.
 * DATE: 05/19/2026
 */
package com.coreyrobinson.inventory.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coreyrobinson.inventory.dao.CategoryDAO;
import com.coreyrobinson.inventory.dao.UnitDAO;
import com.coreyrobinson.inventory.model.Category;
import com.coreyrobinson.inventory.model.Item;
import com.coreyrobinson.inventory.model.Unit;
import com.coreyrobinson.inventory.service.ItemService;
import com.coreyrobinson.inventory.util.ConfirmDialog;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class InventoryController {

	@FXML private TextField nameField;
	@FXML private ChoiceBox<Unit> unitChoiceBox;
	@FXML private TextField currentStockField;
	@FXML private TextField parstockField;
	@FXML private ChoiceBox<Category> categoryChoiceBox;
	@FXML private TableView<Item> itemsTable;
	@FXML private TableColumn<Item, String> nameColumn;
	@FXML private TableColumn<Item, String> unitColumn;
	@FXML private TableColumn<Item, String> currentStockColumn;
	@FXML private TableColumn<Item, String> parstockColumn;
	@FXML private TableColumn<Item, String> categoryColumn;
	@FXML private TableColumn<Item, Void> actionsColumn;
	@FXML private Label messageLabel;
	private ItemService itemService = new ItemService();
	private UnitDAO unitDao = new UnitDAO();
	private CategoryDAO catDao = new CategoryDAO();
	private ObservableList<Item> itemsList = FXCollections.observableArrayList();
	private Map<Integer, String> unitMap;
	private Map<Integer, String> categoryMap;

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
			unitMap = new HashMap<>();
			for (Unit u : units) {
				unitMap.put(u.getUnitId(), u.getUnitName());
			}
			categoryMap = new HashMap<>();
			for (Category c : cats) {
				categoryMap.put(c.getCategoryId(), c.getCategoryName());
			}
			// Simple direct-field columns
			nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getItemName()));
			currentStockColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCurrentStock().toString()));
			parstockColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getParstock().toString()));
			// Look up columns
			unitColumn.setCellValueFactory(cell -> new SimpleStringProperty(unitMap.getOrDefault(cell.getValue().getUnitId(), "?")));
			categoryColumn.setCellValueFactory(cell -> new SimpleStringProperty(categoryMap.getOrDefault(cell.getValue().getCategoryId(), "?")));
			actionsColumn.setCellFactory(column -> new TableCell<Item, Void>() {
			    private final Button deactivateButton = new Button("Deactivate");
			    private final Button editButton = new Button("Edit");
			    private final HBox buttons = new HBox(5, editButton, deactivateButton);
			    
			    {
			    	deactivateButton.getStyleClass().add("action-button");
			        deactivateButton.setOnAction(event -> {
			            Item item = getTableView().getItems().get(getIndex());
			            handleDeactivate(item);
			        });
			        editButton.getStyleClass().add("action-button");
			        editButton.setOnAction(event -> {
			        	Item item = getTableView().getItems().get(getIndex());
			        	try {
			        		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditItemView.fxml"));
			        		Parent root = loader.load();
			        		EditItemController controller = loader.getController();
			        		controller.setItem(item);
			        		Stage dialog = new Stage();
			        		dialog.initModality(Modality.APPLICATION_MODAL);
			        		dialog.setScene(new Scene(root));
			        		dialog.setTitle("Edit Item");
			        		dialog.showAndWait();
			        		if (controller.isSaved()) {
			        			refreshItems();
			        		}
			        	} catch (IOException e) {
			        		showError("Could not open edit window");
			        		e.printStackTrace();
			        	}
			        });
			    }
			    @Override
			    protected void updateItem(Void item, boolean empty) {
			        super.updateItem(item, empty);
			        if (empty) {
			            setGraphic(null);
			        } else {
			            setGraphic(buttons);
			        }
			    }
			});
			itemsTable.setItems(itemsList);
			refreshItems();
		} catch (SQLException e) {
			showError("Failed to load form data: " + e.getMessage());
		}
	}
	
	private void handleDeactivate(Item item) {
		if (!ConfirmDialog.confirm("Deactivate Item", "Deactivate item '" + item.getItemName() + "'?")) {
			return;
		}
	    try {
	        itemService.deactivateItem(item.getItemId());
	        showSuccess("Item '" + item.getItemName() + "' deactivated.");
	        refreshItems();
	    } catch (IllegalArgumentException e) {
	        showError(e.getMessage());
	    } catch (SQLException e) {
	        showError("Could not deactivate item: " + e.getMessage());
	    }
	}

	/**
	 * Shows an error message.
	 * @param message	The error message.
	 */
	private void showError(String message) {
		messageLabel.setText(message);
		messageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: red;");
	}

	/**
	 * Shows a success message
	 * @param message	The success message.
	 */
	private void showSuccess(String message) {
		messageLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");
		messageLabel.setText(message);
	}

	@FXML 
	private void handleAddItem() {
		String itemName = nameField.getText();
		Unit unit = unitChoiceBox.getValue();
		Category cat = categoryChoiceBox.getValue();
		if (unit == null || cat == null) {
			showError("Please select a unit and category");
			return;
		}
		try {
			BigDecimal stock = new BigDecimal(currentStockField.getText());	// parse text fields to big decimal
			BigDecimal parstock = new BigDecimal(parstockField.getText());
			itemService.addItem(itemName, unit.getUnitId(), stock, parstock, cat.getCategoryId());
			showSuccess("Item added: " + itemName);
			clearForm();
			refreshItems();
		} catch (NumberFormatException e) {
			showError("Stock and parstock must be numbers");
		} catch (IllegalArgumentException e) {
			showError(e.getMessage());
		} catch (SQLException e) {
			showError("Database error: " + e.getMessage());
		}
	}

	private void refreshItems() {
		try {
			List<Item> items = itemService.findAllItems();
			items.removeIf(i -> !i.isActive());
			itemsList.setAll(items);
		} catch (SQLException e) {
			showError("Failed to load items: " + e.getMessage());
		}
	}

	private void clearForm() {
		nameField.clear();
		currentStockField.clear();
		parstockField.clear();
		unitChoiceBox.getSelectionModel().clearSelection();
		categoryChoiceBox.getSelectionModel().clearSelection();
	}

}
