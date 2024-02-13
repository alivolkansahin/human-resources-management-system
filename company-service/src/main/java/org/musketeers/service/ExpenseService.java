package org.musketeers.service;

import org.musketeers.repository.ExpenseRepository;
import org.musketeers.repository.entity.Expense;
import org.musketeers.utility.ServiceManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ExpenseService extends ServiceManager<Expense, String> {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        super(expenseRepository);
        this.expenseRepository = expenseRepository;
    }

    public Boolean saveExpense(Expense expense) {
        save(expense);
        return true;
    }
}
