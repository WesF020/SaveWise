package com.savewise.service.filter;

import com.savewise.model.Transaction;
import com.savewise.model.TransactionType;
import java.util.List;
import java.util.stream.Collectors;

public class TypeFilter implements TransactionFilter {

    private final TransactionType type;

    public TypeFilter(TransactionType type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null.");
        }
        this.type = type;
    }

    @Override
    public List<Transaction> filter(List<Transaction> transactions) {
        return transactions.stream()
                .filter(transaction -> transaction.getType() == type)
                .collect(Collectors.toList());
    }
}