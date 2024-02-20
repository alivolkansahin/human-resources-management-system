package org.musketeers.service;

import org.musketeers.dto.request.CompanyUpdateRequestDTO;
import org.musketeers.dto.response.GetCompanyDetailedInfoResponseDto;
import org.musketeers.dto.response.GetCompanySummaryInfoResponseDto;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.mapper.ICompanyMapper;
import org.musketeers.rabbitmq.model.GetCompanyDetailsByCommentResponseModel;
import org.musketeers.rabbitmq.model.GetCompanyDetailsByPersonnelResponseModel;
import org.musketeers.rabbitmq.model.GetCompanySupervisorResponseModel;
import org.musketeers.rabbitmq.producer.GetCompanyIdFromSupervisorProducer;
import org.musketeers.rabbitmq.producer.GetCompanySupervisorRequestProducer;
import org.musketeers.repository.CompanyRepository;
import org.musketeers.repository.entity.*;
import org.musketeers.repository.enums.EStatus;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    // The dependencies of some of the beans in the application context form a cycle:
    // Relying upon circular references is discouraged and they are prohibited by default. Update your application to remove the dependency cycle between beans.
    // As a last resort, it may be possible to break the cycle automatically by setting spring.main.allow-circular-references to true.
    // https://stackoverflow.com/questions/40695893/spring-security-circular-bean-dependency :
    // You could replace constructor-based dependency injection with setter-based dependency injection to resolve the cycle, see Spring Framework Reference Documentation:
    private DepartmentService departmentService;

    @Autowired
    public void setDepartmentService(@Lazy DepartmentService departmentService){
        this.departmentService = departmentService;
    }

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
        return companies.stream()
                .map(company -> GetCompanyDetailsByCommentResponseModel.builder()
                    .companyName(company.getCompanyName())
                    .companyLogo(company.getCompanyLogo())
                    .build()).toList();
    }

    public List<GetCompanySummaryInfoResponseDto> getCompanySummaryInfo(String companyName) {
        return companyName.equals("SEARCH_FIELD_EMPTY") ? getAllCompaniesForGuest() : searchCompanyByNameForGuest(companyName);
    }

    private List<GetCompanySummaryInfoResponseDto> getAllCompaniesForGuest(){
        return findAll().stream()
                .map(company -> GetCompanySummaryInfoResponseDto.builder()
                    .id(company.getId())
                    .name(company.getCompanyName())
                    .logo(company.getCompanyLogo())
                    .establishmentDate(company.getEstablishmentDate())
                    .build()).toList();
    }
    private List<GetCompanySummaryInfoResponseDto> searchCompanyByNameForGuest(String companyName) {
        return companyRepository.findAllByCompanyNameLikeIgnoreCase("%" + companyName + "%").stream()
                .map(company -> GetCompanySummaryInfoResponseDto.builder()
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
        List<String> supervisorIds = company.getSupervisors().stream()
                .map(Supervisor::getSupervisorId)
                .toList();
        return getCompanySupervisorRequestProducer.getCompanySupervisorInfo(supervisorIds);
    }

    private GetCompanyDetailedInfoResponseDto prepareCompanyDetailedInfoResponseDto(Company company, List<GetCompanySupervisorResponseModel> supervisorInfosModel) {
        return GetCompanyDetailedInfoResponseDto.builder()
                .companyName(company.getCompanyName())
                .establishmentDate(company.getEstablishmentDate())
                .companyLogo(company.getCompanyLogo())
                .address(company.getAddress())
                .hrInfos(company.getHrInfos().stream()
                        .map(ICompanyMapper.INSTANCE::hrInfosToDto)
                        .toList())
                .departmentNames(company.getDepartments().stream()
                        .map(Department::getName)
                        .toList())
                .personnelCount(company.getDepartments().stream()
                        .map(department -> department.getPersonnel().size())
                        .reduce(0, Integer::sum))
                .supervisors(supervisorInfosModel.stream()
                        .map(ICompanyMapper.INSTANCE::supervisorModelToDto)
                        .toList())
                .build();
    }

    public GetCompanyDetailsByPersonnelResponseModel getCompanyDetailsByPersonnel(String personnelId) {
        Department department = departmentService.findByPersonnelId(personnelId);
        Company company = companyRepository.findOptionalByDepartmentsId(department.getId()).orElseThrow(() -> new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND));
        return prepareCompanyDetailsResponseModel(company, department);
    }

    private GetCompanyDetailsByPersonnelResponseModel prepareCompanyDetailsResponseModel(Company company, Department department) {
        return GetCompanyDetailsByPersonnelResponseModel.builder()
                .companyName(company.getCompanyName())
                .departmentName(department.getName())
                .shifts(department.getShifts())
                .breaks(department.getBreaks())
                .holidays(company.getHolidays().stream()
                        .map(holiday -> holiday.getName() + "*" + holiday.getDuration())
                        .toList())
                .hrInfos(company.getHrInfos().stream()
                        .map(hrInfo -> hrInfo.getFirstName()+"*"+hrInfo.getLastName()+"*"+hrInfo.getEmail()+"*"+hrInfo.getPhone())
                        .toList())
                .build();
    }
}
