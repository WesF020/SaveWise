package com.savewise.core;

import com.savewise.dao.ITransactionDAO;
import com.savewise.dao.TransactionDAOImpl;
import com.savewise.service.TransactionService;
import com.savewise.dao.decorator.CachingTransactionsDAO;

public class AppContext {
    private static AppContext instance;

    private final TransactionService transactionService;

    private AppContext() {
        ITransactionDAO transactionDAO = new TransactionDAOImpl();
        transactionDAO = new CachingTransactionsDAO(transactionDAO);
        this.transactionService = new TransactionService(transactionDAO);
        transactionService.addListener(() -> System.out.println("Observer: alteração na transação. "));
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
