package org.musketeers.service;

import org.musketeers.repository.ExpenseRepository;
import org.musketeers.repository.entity.Expense;
import org.musketeers.utility.ServiceManager;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class ExpenseService extends ServiceManager<Expense, Long> {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        super(expenseRepository);
        this.expenseRepository = expenseRepository;
    }

    public ResponseEntity<Boolean> saveExpense(Expense expense) {
        save(expense);
        return ResponseEntity.ok(true);
    }
}
