package org.musketeers.service;

import org.musketeers.dto.request.AddIncomeRequestDto;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.repository.IncomeRepository;
import org.musketeers.repository.entity.Company;
import org.musketeers.repository.entity.Income;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class IncomeService extends ServiceManager<Income, String> {
    private final IncomeRepository incomeRepository;

    private final CompanyService companyService;

    public IncomeService(IncomeRepository incomeRepository, CompanyService companyService) {
        super(incomeRepository);
        this.incomeRepository = incomeRepository;
        this.companyService = companyService;
    }

    public Boolean saveIncome(AddIncomeRequestDto dto) {
        Company company = companyService.findById(dto.getCompanyId()).orElseThrow(() -> new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND));
        Income income = Income.builder()
                .company(company)
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .build();
        save(income);
        return true;
    }
}
