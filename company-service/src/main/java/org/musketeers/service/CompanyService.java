package org.musketeers.service;

import org.musketeers.dto.request.CompanyUpdateRequestDTO;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.rabbitmq.producer.GetCompanyIdFromSupervisorProducer;
import org.musketeers.repository.CompanyRepository;
import org.musketeers.repository.entity.*;
import org.musketeers.repository.enums.EStatus;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyService extends ServiceManager<Company, String> {
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

    public Boolean updateCompanyForFirstTime(CompanyUpdateRequestDTO dto) {
        Company company = findByCompanyId(dto.getToken());
        if(company.getCompanyStatus().equals(EStatus.ACTIVE)) return false;
        Company updatedCompany = getUpdatedCompany(dto,company);
        update(updatedCompany);
        return true;
    }

    private Company getUpdatedCompany(CompanyUpdateRequestDTO dto,Company company) {
        company.setEstablishmentDate(dto.getEstablishmentDate());
        company.setCompanyLogo(dto.getCompanyLogo());
        company.setCompanyStatus(EStatus.ACTIVE);
        company.setAddress(dto.getAddress());
        company.setHrInfos(dto.getHrInfos().stream()
                .map(hrInfoDto -> HRInfo.builder()
                        .company(company)
                        .firstName(hrInfoDto.getFirstName())
                        .lastName(hrInfoDto.getLastName())
                        .email(hrInfoDto.getEmail())
                        .phone(hrInfoDto.getPhone())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList()));
        company.setDepartments(dto.getDepartments().stream()
                .map(departmentDto -> Department.builder()
                        .company(company)
                        .name(departmentDto.getName())
                        .shifts(departmentDto.getShifts())
                        .breaks(departmentDto.getBreaks())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList()));
        company.setHolidays(dto.getHolidays().stream()
                .map(holidayDto -> Holiday.builder()
                        .company(company)
                        .name(holidayDto.getName())
                        .duration(holidayDto.getDuration())
                        .build())
                .collect(Collectors.toList()));
        company.setIncomes(dto.getIncomes().stream()
                .map(incomeDto -> Income.builder()
                        .company(company)
                        .description(incomeDto.getDescription())
                        .amount(incomeDto.getAmount())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList()));
        company.setExpenses(dto.getExpenses().stream()
                .map(expenseDto -> Expense.builder()
                        .company(company)
                        .description(expenseDto.getDescription())
                        .amount(expenseDto.getAmount())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList()));
        return company;
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
        } else {
            throw new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND);
        }
    }
}
