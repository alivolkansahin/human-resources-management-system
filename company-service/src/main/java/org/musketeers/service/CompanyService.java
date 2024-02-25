package org.musketeers.service;

import org.musketeers.dto.request.CompanyUpdateRequestDTO;
import org.musketeers.dto.response.GetCompanyDetailedInfoResponseDto;
import org.musketeers.dto.response.GetCompanySummaryInfoResponseDto;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.mapper.ICompanyMapper;
import org.musketeers.rabbitmq.model.*;
import org.musketeers.rabbitmq.producer.GetCompanyIdFromSupervisorProducer;
import org.musketeers.rabbitmq.producer.GetCompanySupervisorRequestProducer;
import org.musketeers.repository.CompanyRepository;
import org.musketeers.repository.entity.*;
import org.musketeers.repository.enums.ECurrency;
import org.musketeers.repository.enums.EGender;
import org.musketeers.repository.enums.EStatus;
import org.musketeers.repository.enums.ETurkishHoliday;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
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

    private final PersonnelService personnelService;

    @Autowired
    public void setDepartmentService(@Lazy DepartmentService departmentService){
        this.departmentService = departmentService;
    }

    public CompanyService(CompanyRepository companyRepository, JwtTokenManager jwtTokenManager, GetCompanyIdFromSupervisorProducer getCompanyIdFromSupervisorProducer, GetCompanySupervisorRequestProducer getCompanySupervisorRequestProducer, PersonnelService personnelService) {
        super(companyRepository);
        this.companyRepository = companyRepository;
        this.jwtTokenManager=jwtTokenManager;
        this.getCompanyIdFromSupervisorProducer = getCompanyIdFromSupervisorProducer;
        this.getCompanySupervisorRequestProducer = getCompanySupervisorRequestProducer;
        this.personnelService = personnelService;
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
        addTurkishHolidaysToCompany(updatedCompany);
        update(updatedCompany);
        return true;
    }

    private void addTurkishHolidaysToCompany(Company company) {
        List<Holiday> companyHolidays = company.getHolidays();
        for (ETurkishHoliday turkishHoliday : ETurkishHoliday.values()) {
            companyHolidays.add(Holiday.builder()
                    .company(company)
                    .name(turkishHoliday.getName())
                    .startDate(turkishHoliday.getStartDate())
                    .endDate(turkishHoliday.getEndDate())
                    .build());
        }
    }

    private Company getUpdatedCompany(CompanyUpdateRequestDTO dto,Company company) {
        Long time = System.currentTimeMillis();
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
                        .gender(EGender.valueOf(hrInfoDto.getGender()))
                        .image(hrInfoDto.getGender().equals("MALE") ? "https://i.imgur.com/ltRBj9D.png" : "https://i.imgur.com/BNXkMgI.png")
                        .createdAt(time)
                        .updatedAt(time)
                        .build())
                .collect(Collectors.toList()));
        company.setDepartments(dto.getDepartments().stream()
                .map(departmentDto -> Department.builder()
                        .company(company)
                        .name(departmentDto.getName())
                        .shiftHour(departmentDto.getShiftHour())
                        .breakHour(departmentDto.getBreakHour())
                        .createdAt(time)
                        .updatedAt(time)
                        .build())
                .collect(Collectors.toList()));
        company.setHolidays(dto.getHolidays().stream()
                .map(holidayDto -> Holiday.builder()
                        .company(company)
                        .name(holidayDto.getName())
                        .startDate(holidayDto.getStartDate())
                        .endDate(holidayDto.getEndDate())
                        .build())
                .collect(Collectors.toList()));
        company.setIncomes(dto.getIncomes().stream()
                .map(incomeDto -> Income.builder()
                        .company(company)
                        .description(incomeDto.getDescription())
                        .amount(incomeDto.getAmount())
                        .incomeDate(incomeDto.getIncomeDate())
                        .createdAt(time)
                        .updatedAt(time)
                        .build())
                .collect(Collectors.toList()));
        company.setExpenses(dto.getExpenses().stream()
                .map(expenseDto -> Expense.builder()
                        .company(company)
                        .description(expenseDto.getDescription())
                        .amount(expenseDto.getAmount())
                        .expenseDate(expenseDto.getExpenseDate())
                        .createdAt(time)
                        .updatedAt(time)
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
        return companyRepository.findOptionalById(companyIdFromSupervisor).orElseThrow(() -> new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND));
    }

    public List<GetCompanyDetailsByCommentResponseModel> getCompanyInfoByCompanyIds(List<String> companyIds) {
        List<Company> companies = new ArrayList<>();
        companyIds.forEach(companyId -> companies.add(findById(companyId).get()));
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

    public GetCompanyDetailsByPersonnelResponseModel getCompanyDetailsByPersonnel(List<String> personnelInfos) {
        Company company = findById(personnelInfos.get(1)).orElseThrow(() -> new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND));
        if (personnelInfos.get(2).equals("true")) return prepareCompanyDetailsResponseModelForSupervisor(company);
        Personnel personnel = personnelService.findByPersonnelId(personnelInfos.get(0));
        Department department = departmentService.findById(personnel.getDepartment().getId()).orElseThrow(() -> new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND)); // department not found
        return prepareCompanyDetailsResponseModelForPersonnel(company, department);
    }

    private GetCompanyDetailsByPersonnelResponseModel prepareCompanyDetailsResponseModelForPersonnel(Company company, Department department) {
        return GetCompanyDetailsByPersonnelResponseModel.builder()
                .companyName(company.getCompanyName())
                .companyLogo(company.getCompanyLogo())
                .departmentName(department.getName())
                .shiftHour(department.getShiftHour())
                .breakHour(department.getBreakHour())
                .holidays(company.getHolidays().stream()
                        .map(holiday -> holiday.getName() + "*" + holiday.getStartDate() + "*" + holiday.getEndDate())
                        .toList())
                .hrInfos(company.getHrInfos().stream()
                        .map(hrInfo -> hrInfo.getFirstName() + "*" + hrInfo.getLastName() + "*" + hrInfo.getEmail() + "*" +hrInfo.getPhone() + "*" + hrInfo.getImage())
                        .toList())
                .build();
    }

    private GetCompanyDetailsByPersonnelResponseModel prepareCompanyDetailsResponseModelForSupervisor(Company company) {
        return GetCompanyDetailsByPersonnelResponseModel.builder()
                .companyName(company.getCompanyName())
                .companyLogo(company.getCompanyLogo())
                .departmentName("")
                .shiftHour("")
                .breakHour("")
                .holidays(company.getHolidays().stream()
                        .map(holiday -> holiday.getName() + "*" + holiday.getStartDate() + "*" + holiday.getEndDate())
                        .toList())
                .hrInfos(company.getHrInfos().stream()
                        .map(hrInfo -> hrInfo.getFirstName() + "*" + hrInfo.getLastName() + "*" + hrInfo.getEmail() + "*" +hrInfo.getPhone() + "*" + hrInfo.getImage())
                        .toList())
                .build();
    }

    public CreateCompanyResponseModel createCompanyWithApprovedSupervisor(CreateCompanyRequestModel model) {
        Optional<Company> optionalCompany = findByCompanyName(model.getCompanyName());
        Company company = null;
        if(optionalCompany.isEmpty()) {
            company = createCompanyWithFirstSupervisor(model);
        } else {
            company = optionalCompany.get();
            company.getSupervisors().add(Supervisor.builder()
                    .company(company)
                    .supervisorId(model.getSupervisorId())
                    .build());
        }
        update(company);
        return CreateCompanyResponseModel.builder().companyId(company.getId()).build();
    }

    private Company createCompanyWithFirstSupervisor(CreateCompanyRequestModel model) {
        Company company = Company.builder()
                .companyName(model.getCompanyName())
                .build();
        save(company);
        company.setSupervisors(Arrays.asList(Supervisor.builder()
                .company(company)
                .supervisorId(model.getSupervisorId())
                .build()));
        company.setContract(Contract.builder()
                .name(model.getCompanyName())
                .startDate(Instant.ofEpochMilli(company.getCreatedAt())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate())
                .endDate(Instant.ofEpochMilli(company.getCreatedAt())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .plusDays(model.getContractDuration()))
                .cost(model.getContractCost())
                .currency(ECurrency.valueOf(model.getContractCurrency()))
                .build());
        return company;
    }

    public Boolean checkCompanyStatus(CompanyStatusCheckRequestModel model) {
        Company company = findById(model.getCompanyId()).orElseThrow(() -> new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND));
        if(company.getCompanyStatus().equals(EStatus.PASSIVE)) return false;
        LocalDate contractEndDate = company.getContract().getEndDate();
        LocalDate checkDate = Instant.ofEpochMilli(model.getCurrentTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        if(checkDate.isAfter(contractEndDate)) {
            company.setCompanyStatus(EStatus.PASSIVE);
            update(company);
            return false;
        }
        return true;
    }

    public Boolean searchCompanyName(String companyName) {
        return findByCompanyName(companyName).isPresent();
    }
}

