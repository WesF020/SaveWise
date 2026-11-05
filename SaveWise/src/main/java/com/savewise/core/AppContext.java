package com.savewise.core;

import com.savewise.dao.ITransactionDAO;
import com.savewise.dao.TransactionDAOImpl;
import com.savewise.service.TransactionService;

public class AppContext {
    private static AppContext instance;

    private final TransactionService transactionService;

    private AppContext() {
        ITransactionDAO transactionDAO = new TransactionDAOImpl();
        this.transactionService = new TransactionService(transactionDAO);
    }

    public static synchronized AppContext getInstance() {
        if (instance == null) {
            instance = new AppContext();
        }
        return instance;
    }

    public TransactionService getTransactionService() {
        return transactionService;
    }

}
