package com.savewise.service.filter;

import com.savewise.model.Transaction;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryFilter implements TransactionFilter {

    private final String category;

    public CategoryFilter(String category) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category cannot be empty.");
        }
        this.category = category.trim();
    }

    @Override
    public List<Transaction> filter(List<Transaction> transactions) {
        return transactions.stream()
                .filter(transaction -> transaction.getCategory() != null
                        && transaction.getCategory().trim().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
}