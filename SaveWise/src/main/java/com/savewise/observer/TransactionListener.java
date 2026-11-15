package com.savewise.observer;

public interface TransactionListener {
    void onTransactionsChanged(TransactionEvent event);
}
