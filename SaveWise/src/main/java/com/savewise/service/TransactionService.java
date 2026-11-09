package com.savewise.service;

import com.savewise.dao.ITransactionDAO;
import com.savewise.model.Transaction;
import com.savewise.model.TransactionType;
import com.savewise.service.filter.TransactionFilter;

import java.math.BigDecimal;
import java.util.List;

public class TransactionService {

    public final ITransactionDAO transactionDAO;

    // Recebe construtor pelo DAO
    public TransactionService (ITransactionDAO transactionDAO){
        this.transactionDAO = transactionDAO;
    }

    // Adiciona Transações
    public void addTransaction(Transaction transaction){
        if(transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The amount must be greater than Zero.");
        }
        if(transaction.getDescription() == null || transaction.getDescription().isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty.");
        }
        transactionDAO.save(transaction);
    }

    // Listagem de transações
    public List<Transaction> getAllTransactions() {
        return transactionDAO.findAll();
    }

    // Deleta transações
    public void deleteTransaction(int id){
        boolean exists = transactionDAO.findAll()
                .stream()
                .anyMatch(t -> t.getId() == id);
        if (!exists) {
            throw new IllegalArgumentException("Transaction with ID " + id + " not found.");
        }
        transactionDAO.delete(id);
    }

    public BigDecimal getBalance() {
       List<Transaction> transactions = transactionDAO.findAll();
       BigDecimal balance = BigDecimal.ZERO;

       for (Transaction transaction : transactions) {
           if (transaction.getType() == TransactionType.INCOME){
               balance = balance.add(transaction.getAmount());
           } else {
               balance = balance.subtract(transaction.getAmount());
           }
       }
        return balance;
    }

    public List<Transaction> filterTransactions(TransactionFilter... filters) {
        if (filters == null) {
            throw new IllegalArgumentException("Filter cannot be null.");
        }
        for (TransactionFilter filter : filters) {
            if (filter == null) {
                throw new IllegalArgumentException("Filter cannot be null.");
            }
        }

        List<Transaction> result = transactionDAO.findAll();
        for (TransactionFilter filter : filters) {
            result = filter.filter(result);
        }
        return result;
    }
}
