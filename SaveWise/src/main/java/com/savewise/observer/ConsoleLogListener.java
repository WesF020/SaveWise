package com.savewise.observer;

import java.time.LocalTime;

public class ConsoleLogListener implements TransactionListener {

    @Override
    public void onTransactionsChanged(TransactionEvent event) {
        String message = switch (event) {
            case ADDED -> "Transaction added";
            case DELETED -> "Transaction deleted";
        };
        System.out.println("[Observer]:" + message + " at " + LocalTime.now().withNano(0));
    }

}
