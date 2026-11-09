package com.savewise.dao.decorator;

import com.savewise.dao.ITransactionDAO;
import com.savewise.model.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CachingTransactionsDAO extends TransactionDAODecorator {

    // Chave de cada consulta. Hoje só existe findAll() (sem parâmetros), então a chave é fixa.
    // Se surgirem consultas com parâmetros, a chave deve incluí-los, ex.: "findByType:" + type.
    private static final String FIND_ALL_KEY = "findAll";

    private final Map<String, List<Transaction>> cache = new HashMap<>();

    public CachingTransactionsDAO(ITransactionDAO wrapped) {
        super(wrapped);
    }

    @Override
    public synchronized List<Transaction> findAll() {
        List<Transaction> cached = cache.get(FIND_ALL_KEY);
        if (cached != null) {
            return new ArrayList<>(cached);
        }

        List<Transaction> result = wrapped.findAll();
        if (result == null) {
            return null; // resultado inválido do DAO original: não é armazenado
        }
        cache.put(FIND_ALL_KEY, new ArrayList<>(result)); // lista vazia também é um resultado válido
        return result;
    }

    @Override
    public synchronized void save(Transaction transaction) {
        try {
            wrapped.save(transaction);
        } finally {
            cache.clear(); // mesmo que a escrita falhe, não se confia no cache
        }
    }

    @Override
    public synchronized void delete(int id) {
        try {
            wrapped.delete(id);
        } finally {
            cache.clear();
        }
    }
}