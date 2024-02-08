package org.musketeers.service;

import org.musketeers.repository.CompanyRepository;
import org.musketeers.repository.entity.Company;
import org.musketeers.utility.ServiceManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class CompanyService extends ServiceManager<Company, Long> {
    private final CompanyRepository companyRepository;
    public CompanyService(CompanyRepository companyRepository) {
        super(companyRepository);
        this.companyRepository = companyRepository;
    }

    public ResponseEntity<Boolean> createCompany(Company company) {
        Company savedCompany = save(company);
        if (savedCompany.getCompanyName().isEmpty()){
            return ResponseEntity.ok(false);
        }else{
            return ResponseEntity.ok(true);
        }
    }
}
