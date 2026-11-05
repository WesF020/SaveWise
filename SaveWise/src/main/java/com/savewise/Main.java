package com.savewise;

import com.savewise.core.AppContext;
import com.savewise.ui.MenuCLI;
import com.savewise.service.TransactionService;


public class Main {
    public static void main(String[] args) {
        TransactionService transactionService = AppContext.getInstance().getTransactionService();
        MenuCLI menuCLI = new MenuCLI(transactionService);
        menuCLI.start();
    }
}