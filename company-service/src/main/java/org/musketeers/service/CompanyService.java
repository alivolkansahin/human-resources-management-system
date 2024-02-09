package org.musketeers.service;

import org.musketeers.dto.request.CompanyUpdateRequestDTO;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.repository.CompanyRepository;
import org.musketeers.repository.entity.Company;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
@Service
public class CompanyService extends ServiceManager<Company, Long> {
    private final CompanyRepository companyRepository;
    private final JwtTokenManager jwtTokenManager;

    public CompanyService(CompanyRepository companyRepository,JwtTokenManager jwtTokenManager) {
        super(companyRepository);
        this.companyRepository = companyRepository;
        this.jwtTokenManager=jwtTokenManager;
    }

    public boolean createCompany(Company company) {
        if (companyRepository.findOptionalByCompanyName(company.getCompanyName()).isPresent()){
            Company savedCompany = save(company);
            return true;
        }else{
            return false;
        }
    }

    public Optional<Company> findByCompanyName(String companyName){
        return companyRepository.findOptionalByCompanyName(companyName);
    }

    public Boolean updateCompany(CompanyUpdateRequestDTO dto) {
        Optional<String> companyName = jwtTokenManager.decodeToken(dto.getToken());
        if (companyName.isEmpty()){
            throw new CompanyServiceException(ErrorType.INVALID_TOKEN);
        }
        Optional<Company> optionalByCompanyName = companyRepository.findOptionalByCompanyName(companyName.get());
        if (optionalByCompanyName.isEmpty()){
            throw new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND);
        }
        Company updatedCompany = optionalByCompanyName.get();
        updatedCompany.setCompanyName(dto.getCompanyName());
        updatedCompany.setHolidays(dto.getHolidays());
        updatedCompany.setHrInfos(dto.getHrInfos());
        updatedCompany.setAddress(dto.getAddress());
        updatedCompany.setDepartments(dto.getDepartments());
        updatedCompany.setExpenses(dto.getExpenses());
        updatedCompany.setIncomes(dto.getIncomes());
        updatedCompany.setSupervisorIds(dto.getSupervisorIds());
        update(updatedCompany);
        return true;
    }

    public Boolean softDelete(String companyName) {
        Optional<Company> optionalCompany = findByCompanyName(companyName);
        if (optionalCompany.isPresent()){
            optionalCompany.get().setStatus(false);
            return true;
        }else {
            throw new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND);
        }
    }

    public Boolean hardDelete(String companyName){
        Optional<Company> optionalCompany = findByCompanyName(companyName);
        if (optionalCompany.isPresent()){
            companyRepository.deleteByCompanyName(companyName);
            return true;
        }else {
            throw new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND);
        }
    }
}
