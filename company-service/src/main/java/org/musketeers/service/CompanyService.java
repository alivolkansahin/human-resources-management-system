package org.musketeers.service;

import org.musketeers.dto.request.CompanyUpdateRequestDTO;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.rabbitmq.producer.GetCompanyIdFromSupervisorProducer;
import org.musketeers.repository.CompanyRepository;
import org.musketeers.repository.entity.Company;
import org.musketeers.repository.enums.Status;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class CompanyService extends ServiceManager<Company, Long> {
    private final CompanyRepository companyRepository;
    private final JwtTokenManager jwtTokenManager;

    private final GetCompanyIdFromSupervisorProducer getCompanyIdFromSupervisorProducer;

    public CompanyService(CompanyRepository companyRepository, JwtTokenManager jwtTokenManager, GetCompanyIdFromSupervisorProducer getCompanyIdFromSupervisorProducer) {
        super(companyRepository);
        this.companyRepository = companyRepository;
        this.jwtTokenManager=jwtTokenManager;
        this.getCompanyIdFromSupervisorProducer = getCompanyIdFromSupervisorProducer;
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
        Company byCompanyId = findByCompanyId(dto.getToken());
        Company updatedCompany = getUpdatedCompany(dto,byCompanyId);
        update(updatedCompany);
        return true;
    }

    private Company getUpdatedCompany(CompanyUpdateRequestDTO dto,Company byCompanyId) {
        byCompanyId.setHolidays(dto.getHolidays());
        byCompanyId.setHrInfos(dto.getHrInfos());
        byCompanyId.setAddress(dto.getAddress());
        byCompanyId.setDepartments(dto.getDepartments());
        byCompanyId.setExpenses(dto.getExpenses());
        byCompanyId.setIncomes(dto.getIncomes());
        byCompanyId.setSupervisorIds(dto.getSupervisorIds());
        byCompanyId.setCompanyLogo(dto.getCompanyLogo());
        byCompanyId.setEstablishmentDate(dto.getEstablishmentDate());
        byCompanyId.setStatus(Status.ACTIVE);
        return byCompanyId;
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

    public Company findByCompanyId(String supervisorToken) {
        List<String> claimsFromToken = jwtTokenManager.getClaimsFromToken(supervisorToken);
        String companyIdFromSupervisor = getCompanyIdFromSupervisorProducer.getCompanyIdFromSupervisor(claimsFromToken.get(0));
        Optional<Company> optionalCompanyById = companyRepository.findOptionalById(companyIdFromSupervisor);
        if (optionalCompanyById.isPresent()){
            return optionalCompanyById.get();
        }else {
            throw new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND);
        }
    }
}
