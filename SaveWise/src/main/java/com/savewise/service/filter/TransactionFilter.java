package com.savewise.service.filter;

import com.savewise.model.Transaction;
import java.util.List;

public interface TransactionFilter {
    List<Transaction> filter(List<Transaction> transactions);
}