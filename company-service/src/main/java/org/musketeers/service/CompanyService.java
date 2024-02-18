package org.musketeers.service;

import org.musketeers.dto.request.CompanyUpdateRequestDTO;
import org.musketeers.dto.response.GetCompanyDetailedInfoResponseDto;
import org.musketeers.dto.response.GetCompanySummaryInfoResponseDto;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.mapper.ICompanyMapper;
import org.musketeers.rabbitmq.model.GetCompanyDetailsByCommentResponseModel;
import org.musketeers.rabbitmq.model.GetCompanySupervisorResponseModel;
import org.musketeers.rabbitmq.producer.GetCompanyIdFromSupervisorProducer;
import org.musketeers.rabbitmq.producer.GetCompanySupervisorRequestProducer;
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

    private final GetCompanySupervisorRequestProducer getCompanySupervisorRequestProducer;

    public CompanyService(CompanyRepository companyRepository, JwtTokenManager jwtTokenManager, GetCompanyIdFromSupervisorProducer getCompanyIdFromSupervisorProducer, GetCompanySupervisorRequestProducer getCompanySupervisorRequestProducer) {
        super(companyRepository);
        this.companyRepository = companyRepository;
        this.jwtTokenManager=jwtTokenManager;
        this.getCompanyIdFromSupervisorProducer = getCompanyIdFromSupervisorProducer;
        this.getCompanySupervisorRequestProducer = getCompanySupervisorRequestProducer;
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

    public List<GetCompanyDetailsByCommentResponseModel> getCompanyInfoByCompanyIds(List<String> companyIds) {
        List<Company> companies = companyRepository.findAllById(companyIds);
        return companies.stream().map(company -> GetCompanyDetailsByCommentResponseModel.builder()
                .companyName(company.getCompanyName())
                .companyLogo(company.getCompanyLogo())
                .build()).toList();
    }

    public List<GetCompanySummaryInfoResponseDto> getCompanySummaryInfo() {
        return findAll().stream().map(company -> GetCompanySummaryInfoResponseDto.builder()
                .id(company.getId())
                .name(company.getCompanyName())
                .logo(company.getCompanyLogo())
                .establishmentDate(company.getEstablishmentDate())
                .build()).toList();
    }

    public GetCompanyDetailedInfoResponseDto getCompanyDetailedInfoById(String companyId) {
        Company company = findById(companyId).orElseThrow(() -> new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND));
        List<GetCompanySupervisorResponseModel> supervisorInfosModel = getCompanySupervisorSummaryInfoFromSupervisorService(company);
        return prepareCompanyDetailedInfoResponseDto(company, supervisorInfosModel);
    }

    private List<GetCompanySupervisorResponseModel> getCompanySupervisorSummaryInfoFromSupervisorService(Company company) {
        List<String> supervisorIds = company.getSupervisors().stream().map(Supervisor::getSupervisorId).toList();
        return getCompanySupervisorRequestProducer.getCompanySupervisorInfo(supervisorIds);
    }

    private GetCompanyDetailedInfoResponseDto prepareCompanyDetailedInfoResponseDto(Company company, List<GetCompanySupervisorResponseModel> supervisorInfosModel) {
        return GetCompanyDetailedInfoResponseDto.builder()
                .companyName(company.getCompanyName())
                .establishmentDate(company.getEstablishmentDate())
                .companyLogo(company.getCompanyLogo())
                .address(company.getAddress())
                .hrInfos(company.getHrInfos().stream().map(ICompanyMapper.INSTANCE::hrInfosToDto).toList())
                .departmentNames(company.getDepartments().stream().map(Department::getName).toList())
                .personnelCount(company.getDepartments().stream().map(department -> department.getPersonnel().size()).reduce(0, Integer::sum))
                .supervisors(supervisorInfosModel.stream().map(ICompanyMapper.INSTANCE::supervisorModelToDto).toList())
                .build();
    }
}
