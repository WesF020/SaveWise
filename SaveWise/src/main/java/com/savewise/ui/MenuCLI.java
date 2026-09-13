package com.savewise.ui;

import com.savewise.model.Transaction;
import com.savewise.model.TransactionType;
import com.savewise.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;


public class MenuCLI {

    private final TransactionService transactionService;
    private final Scanner scanner;

    public MenuCLI(TransactionService transactionService){
        this.transactionService = transactionService;
        this.scanner = new Scanner(System.in);
    }

    public void start(){
        int option = -1;

        while (option != 0){
            printMenu();
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please enter a number: ");
                option = -1;        // try/catch logic applied; if the user types a String, the system can restart the scanner
            }

            switch(option) {
                case 1 -> addTransaction();
                case 2 -> listTransactions();
                case 3 -> deleteTransaction();
                case 4 -> showBalance();
                case 0 -> System.out.println("Closing SaveWise...");
                default -> System.out.println("Invalid option. Choose between 0-4");
            }
        }

        scanner.close();
    }

    private void printMenu(){
        System.out.println("\n===== Main Menu | SaveWise=====");
        System.out.println("1. Add Transaction");
        System.out.println("2. List Transactions");
        System.out.println("3. Delete Transaction");
        System.out.println("4. Show Balance");
        System.out.println("0. Exit SaveWise");
        System.out.println("Choose an option: ");
    }

    private void addTransaction(){
        try {
            System.out.println("Description: ");
            String description = scanner.nextLine();

            System.out.println("Amount: ");
            BigDecimal amount = new BigDecimal(scanner.nextLine());

            System.out.println("Type (INCOME/EXPENSE): ");
            TransactionType transactionType = null;
            try {
                transactionType = TransactionType.valueOf(scanner.nextLine().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid type. Please enter INCOME or EXPENSE");
                return;
            }

            System.out.println("Category: ");
            String category = scanner.nextLine();

            Transaction transaction = new Transaction(description, amount, transactionType, category, LocalDate.now());
            transactionService.addTransaction(transaction);
            System.out.println("Transaction added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error while saving transaction.");
        }

    }
    private void listTransactions(){
        List<Transaction> transactions = transactionService.getAllTransactions();

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("\n====== Transactions ======");
        transactions.forEach(System.out::println);
    }

    private void deleteTransaction(){
        System.out.println("Enter transaction ID to delete: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            transactionService.deleteTransaction(id);
            System.out.println("Transaction deleted successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a valid number.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showBalance(){
        BigDecimal balance = transactionService.getBalance();
        System.out.printf("Current Balance: R$ %.2f%n", balance);
    }


}
