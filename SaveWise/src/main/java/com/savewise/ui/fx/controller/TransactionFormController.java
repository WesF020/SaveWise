package com.savewise.ui.fx.controller;

import com.savewise.core.AppContext;
import com.savewise.dao.TransactionDAOImpl;
import com.savewise.model.Transaction;
import com.savewise.model.TransactionType;
import com.savewise.service.TransactionService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionFormController {

    @FXML private TextField descriptionField;
    @FXML private TextField amountField;
    @FXML private ComboBox<TransactionType> typeComboBox;
    @FXML private TextField categoryField;
    @FXML private DatePicker datePicker;

    private final TransactionService transactionService =
            AppContext.getInstance().getTransactionService();

    @FXML
    public void initialize() {
        typeComboBox.setItems(
                FXCollections.observableArrayList(TransactionType.values())
        );
        typeComboBox.getSelectionModel().selectFirst();
        datePicker.setValue(LocalDate.now());
    }

    @FXML
    private void handleSave() {
        String description = descriptionField.getText().trim();
        String amountText = amountField.getText().trim();
        String category = categoryField.getText().trim();
        TransactionType type = typeComboBox.getValue();

        if(description.isEmpty() || amountText.isEmpty() || category.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountText);
        } catch (NumberFormatException e) {
            showError("Invalid amount. Please enter a valid number.");
            return;
        }

        try {

            LocalDate  date = datePicker.getValue();
            if (date == null) {
                showError("Please select a date.");
                return;
            }

            Transaction transaction = new Transaction(
                    description, amount, type, category, date
            );
            transactionService.addTransaction(transaction);
            closeWindow();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }

    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) descriptionField.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        javafx.scene.control.Alert alert =
                new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("SaveWise");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
