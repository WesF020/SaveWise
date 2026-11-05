package com.savewise.core;

import com.savewise.dao.ITransactionDAO;
import com.savewise.dao.TransactionDAOImpl;
import com.savewise.service.TransactionService;

public class AppContext {
    private static final AppContext INSTANCE = new AppContext();

    private final TransactionService transactionService;

    private AppContext() {
        ITransactionDAO transactionDAO = new TransactionDAOImpl();
        this.transactionService = new TransactionService(transactionDAO);
    }

    public static AppContext getInstance() {
        return INSTANCE;
    }

    public TransactionService getTransactionService() {
        return transactionService;
    }

}
