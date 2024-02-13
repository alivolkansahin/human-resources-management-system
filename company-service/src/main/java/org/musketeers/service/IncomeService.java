package org.musketeers.service;

import org.mapstruct.ap.internal.model.common.Type;
import org.musketeers.repository.IncomeRepository;
import org.musketeers.repository.entity.Income;
import org.musketeers.utility.ServiceManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IncomeService extends ServiceManager<Income, String> {
    private final IncomeRepository incomeRepository;

    public IncomeService(IncomeRepository incomeRepository) {
        super(incomeRepository);
        this.incomeRepository = incomeRepository;
    }

    public Boolean saveIncome(Income income) {
        save(income);
        return true;
    }
}
