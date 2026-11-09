package com.savewise.dao.decorator;

import com.savewise.dao.ITransactionDAO;
import com.savewise.model.Transaction;

import java.util.List;

public abstract class TransactionDAODecorator implements ITransactionDAO {

    protected final ITransactionDAO wrapped;

    protected TransactionDAODecorator(ITransactionDAO wrapped) {
        if (wrapped == null) {
            throw new IllegalArgumentException("Decorated DAO cannot be null.");
        }
        this.wrapped = wrapped;
    }

    @Override
    public void save(Transaction transaction) {
        wrapped.save(transaction);
    }

    @Override
    public List<Transaction> findAll() {
        return wrapped.findAll();
    }

    @Override
    public void delete(int id) {
        wrapped.delete(id);
    }
}