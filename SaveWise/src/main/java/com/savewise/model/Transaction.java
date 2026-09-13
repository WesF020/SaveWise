package com.savewise.model;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    private int id;
    private String description;
    private BigDecimal amount;
    private TransactionType type;
    private String category;
    private LocalDate date;

    // Construction without ID, made by the constructor.
    public Transaction(String description, BigDecimal amount, TransactionType type,
                       String category, LocalDate date){
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
    }

    // MariaDB Constructor
    public Transaction(int id, String description, BigDecimal amount, TransactionType type, String category, LocalDate date){
        this(description, amount, type, category, date);
        this.id = id;
    }

    // Getters
    public int getId() {return id;}
    public String getDescription() {return description;}
    public BigDecimal getAmount() {return amount;}
    public TransactionType getType() {return type;}
    public String getCategory() {return category;}
    public LocalDate getDate() {return date;}

    // Setters
    public void setId(int id) {this.id = id;}
    public void setDescription(String description) {this.description = description;}
    public void setAmount(BigDecimal amount) {this.amount = amount;}
    public void setType(TransactionType type) {this.type = type;}
    public void setCategory(String category) {this.category = category;}
    public void setDate(LocalDate date) {this.date = date;}

    @Override
    public String toString(){
        return String.format("[%d] %s | %s | R$ %.2f | %s | %s,",
                id, type, description, amount, category, date);
    }
}