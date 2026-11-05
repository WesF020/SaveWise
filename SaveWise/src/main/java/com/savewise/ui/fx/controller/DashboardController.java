package com.savewise.ui.fx.controller;

import com.savewise.core.AppContext;
import com.savewise.dao.TransactionDAOImpl;
import com.savewise.model.Transaction;
import com.savewise.service.TransactionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TableCell;

import java.math.BigDecimal;
import java.util.List;

public class DashboardController {
    @FXML private Label balanceLabel;
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, Integer>     co1ID;
    @FXML private TableColumn<Transaction, String>      co1Description;
    @FXML private TableColumn<Transaction, BigDecimal>  co1Amount;
    @FXML private TableColumn<Transaction, String> co1Type;
    @FXML private TableColumn<Transaction, String> co1Category;
    @FXML private TableColumn<Transaction, String> co1Date;

    private final TransactionService transactionService =
            AppContext.getInstance().getTransactionService();

    @FXML
    public void initialize(){
        co1ID.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getId()).asObject());


        co1Description.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getDescription()));


        co1Amount.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        data.getValue().getAmount()));
        co1Amount.setCellFactory(column -> new TableCell<Transaction, BigDecimal>(){
            @Override
            protected void updateItem(BigDecimal value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value  == null) {
                    setText(null);
                } else {
                    setText(String.format("R$ %,.2f", value));
                }
            }
        });

        co1Type.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getType().name()));


        co1Category.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCategory()));


        co1Date.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getDate().toString()));

        loadTransactions();
    }

    private void loadTransactions() {
        List<Transaction> list = transactionService.getAllTransactions();
        ObservableList<Transaction> observable =
                FXCollections.observableArrayList(list);
        transactionTable.setItems(observable);

        BigDecimal balance = transactionService.getBalance();
        balanceLabel.setText(String.format("Balance: R$ %.2f", balance));
    }

    @FXML
    private void handleAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fx/view/transaction-form.fxml"));

            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Add Transaction");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            loadTransactions();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error opening form " + e.getCause());
        }
    }

    @FXML
    private void handleDelete() {
        Transaction selected = transactionTable.getSelectionModel()
                                               .getSelectedItem();
        if (selected == null) {
            showAlert("Please select a transaction to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Transaction");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to delete \""
                + selected.getDescription() + "\"?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                transactionService.deleteTransaction(selected.getId());
                loadTransactions();
            }
        });
    }

    @FXML
    private void handleRefresh() {loadTransactions();}

    private void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("SaveWise");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
