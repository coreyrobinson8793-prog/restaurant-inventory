/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Handles the logic on the price import screen.
 * DATE: 08/04/2026
 */
package com.coreyrobinson.inventory.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.coreyrobinson.inventory.model.CsvImportTemplate;
import com.coreyrobinson.inventory.model.PriceImport;
import com.coreyrobinson.inventory.model.Supplier;
import com.coreyrobinson.inventory.service.PriceImportService;
import com.coreyrobinson.inventory.service.SupplierService;
import com.coreyrobinson.inventory.session.Session;
import com.opencsv.exceptions.CsvException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import javafx.beans.property.SimpleStringProperty;

public class PriceImportController {

	@FXML private Label messageLabel;
	@FXML private ChoiceBox<Supplier> supplierChoiceBox;
	@FXML private TextField skuColumnField;
	@FXML private TextField priceColumnField;
	@FXML private TextField headerField;
	@FXML private Label selectedFileLabel;
	@FXML private Button runImportButton;
	@FXML private TableView<PriceImport> historyTable;
	@FXML private TableColumn<PriceImport, String> dateColumn;
	@FXML private TableColumn<PriceImport, String> fileColumn;
	@FXML private TableColumn<PriceImport, String> rowsProcessedColumn;
	@FXML private TableColumn<PriceImport, String> rowsUpdatedColumn;
	@FXML private TableColumn<PriceImport, String> rowsSkippedColumn;
	private PriceImportService priceImportService = new PriceImportService();
	private SupplierService supplierService = new SupplierService();
	private File selectedFile;
	private ObservableList<PriceImport> historyList = FXCollections.observableArrayList();
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");

	@FXML 
	public void initialize() {
		try {
			List<Supplier> suppliers = supplierService.findAllSuppliers();
			supplierChoiceBox.setConverter(new StringConverter<Supplier>() {
				@Override
				public String toString(Supplier supplier) {
					return supplier == null ? "" : supplier.getSupplierName();
				}
				@Override
				public Supplier fromString(String s) {
					return null;
				}
			});
			supplierChoiceBox.getItems().setAll(suppliers);
			supplierChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
				if (newSel != null) {
					try {
						Optional<CsvImportTemplate> optTemplate = priceImportService.findTemplateBySupplier(newSel.getSupplierId());
						if (optTemplate.isPresent()) {
							CsvImportTemplate template = optTemplate.get();
							skuColumnField.setText(template.getSkuColumnName());
							priceColumnField.setText(template.getPriceColumnName());
							headerField.setText(String.valueOf(template.getHeaderRowNumber()));
						} else {
							skuColumnField.clear();
							priceColumnField.clear();
							headerField.clear();
						}
					} catch (SQLException e) {
						showError("Database error: " + e.getMessage());
					}
				}
				updateRunButton();
			});
		} catch (SQLException e) {
			showError("Error loading suppliers: " + e.getMessage());
		}
		dateColumn.setCellValueFactory(cell -> {
			LocalDateTime d = cell.getValue().getImportDate();
			return new SimpleStringProperty(d == null ? "" : d.format(DATE_FORMAT));
		});
		fileColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getFileName())));
		rowsProcessedColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getRowsProcessed())));
		rowsUpdatedColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getRowsUpdated())));
		rowsSkippedColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getRowsSkipped())));
		historyTable.setItems(historyList);
		refreshHistory();
	}

	/**
	 * Disables the import button if a supplier or file isn't selected.
	 */
	private void updateRunButton() {
		runImportButton.setDisable(supplierChoiceBox.getValue() == null || selectedFile == null);
	}

	private void refreshHistory() {
		List<PriceImport> histTable;
		try {
			histTable = priceImportService.findImportHistory();
			historyList.setAll(histTable);
		} catch (SQLException e) {
			showError("Error finding the history. " + e.getMessage());
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
	 * Shows a success message.
	 * @param message	The error message.
	 */
	private void showSuccess(String message) {
		messageLabel.setText(message);
		messageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: green;");	
	}

	@FXML
	/**
	 * Saves the template for the selected supplier.
	 */
	private void handleSaveTemplate() {
		Supplier supplier = supplierChoiceBox.getValue();
		if (supplier == null) {
			showError("Please select a supplier");
			return;
		}
		String skuString = skuColumnField.getText();
		String priceString = priceColumnField.getText();
		try {
			int headerRow = Integer.parseInt(headerField.getText().trim());
			priceImportService.saveTemplate(supplier.getSupplierId(), skuString, priceString, headerRow, Session.getCurrentUser().getUserId());
			showSuccess("Template saved");

		} catch (NumberFormatException e) {
			showError("Header row must be a number");
		} catch (IllegalArgumentException e) {
			showError("Error saving template: " + e.getMessage());
		} catch (SQLException e) {
			showError("Database error: " + e.getMessage());
		}
	}

	@FXML
	/**
	 * Opens a file chooser to select a CSV file.
	 */
	private void handleChosenFile() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Select price file");
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
		File file = chooser.showOpenDialog(selectedFileLabel.getScene().getWindow());
		if (file == null) {
			return;
		}
		selectedFile = file;
		selectedFileLabel.setText(selectedFile.getName());
		updateRunButton();
	}

	@FXML
	private void handleRunImport() {
		Supplier supplier = supplierChoiceBox.getValue();
		if (supplier == null || selectedFile == null) {
			showError("Please select a supplier and a file");
			return;
		}
		try {
			PriceImport priceImport = priceImportService.importPrices(selectedFile, supplier.getSupplierId(), Session.getCurrentUser().getUserId());
			showSuccess("Processed " + priceImport.getRowsProcessed() + " | Updated " + priceImport.getRowsUpdated() + " | Skipped " + priceImport.getRowsSkipped());
			refreshHistory();
		} catch (SQLException e) {
			showError("Error from the database: " + e.getMessage());
		} catch (IOException e) {
			showError("Error reading the file: " + e.getMessage());
		} catch (CsvException e) {
			showError("Error parsing the CSV file: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			showError("Please create a template first: " + e.getMessage());
		}
		
	}
}
