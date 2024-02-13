package org.musketeers.service;

import org.musketeers.dto.request.AddExpenseRequestDto;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.repository.ExpenseRepository;
import org.musketeers.repository.entity.Company;
import org.musketeers.repository.entity.Expense;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService extends ServiceManager<Expense, String> {
    private final ExpenseRepository expenseRepository;

    private final CompanyService companyService;

    public ExpenseService(ExpenseRepository expenseRepository, CompanyService companyService) {
        super(expenseRepository);
        this.expenseRepository = expenseRepository;
        this.companyService = companyService;
    }

    public Boolean saveExpense(AddExpenseRequestDto dto) {
        Company company = companyService.findById(dto.getCompanyId()).orElseThrow(() -> new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND));
        Expense income = Expense.builder()
                .company(company)
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .build();
        save(income);
        return true;
    }
}
