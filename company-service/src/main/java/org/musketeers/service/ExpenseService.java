package org.musketeers.service;

import org.musketeers.dto.request.AddExpenseRequestDto;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.rabbitmq.model.CreatePersonnelCompanyModel;
import org.musketeers.rabbitmq.model.SendAdvanceExpenseToCompanyServiceModel;
import org.musketeers.repository.ExpenseRepository;
import org.musketeers.repository.entity.Company;
import org.musketeers.repository.entity.Expense;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService extends ServiceManager<Expense, String> {
    private final ExpenseRepository expenseRepository;

    private final CompanyService companyService;

    public ExpenseService(ExpenseRepository expenseRepository, CompanyService companyService) {
        super(expenseRepository);
        this.expenseRepository = expenseRepository;
        this.companyService = companyService;
    }

//    public Boolean saveExpense(AddExpenseRequestDto dto) {
//        Company company = companyService.findById(dto.getCompanyId()).orElseThrow(() -> new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND));
//        Expense expense = Expense.builder()
//                .company(company)
//                .description(dto.getDescription())
//                .amount(dto.getAmount())
//                .build();
//        save(expense);
//        return true;
//    }

    public void addPersonnelSalaryExpense(CreatePersonnelCompanyModel model) {
        Company company = companyService.findById(model.getCompanyId()).orElseThrow(() -> new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND));
        int personnelRegisterDateMonthNumber = model.getExpenseDate().getMonthValue();
        List<Expense> personnelYearlySalaryExpense = new ArrayList<>();
        for (int i = personnelRegisterDateMonthNumber; i <= 12 ; i++) {
            Expense expense = Expense.builder()
                    .company(company)
                    .description(model.getExpenseDescription())
                    .amount(model.getExpenseAmount())
                    .expenseDate(LocalDate.of(model.getExpenseDate().getYear(), i, 1))
                    .build();
            personnelYearlySalaryExpense.add(expense);
        }
        saveAll(personnelYearlySalaryExpense);
    }

    public void saveAdvanceExpense(SendAdvanceExpenseToCompanyServiceModel model) {
        Company company = companyService.findById(model.getCompanyId()).orElseThrow(() -> new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND));
        Expense expense = Expense.builder()
                .company(company)
                .description(model.getDescription())
                .amount(model.getAmount())
                .expenseDate(model.getExpenseDate())
                .build();
        save(expense);
    }
}
