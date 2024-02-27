package org.musketeers.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.musketeers.dto.request.CreatePersonnelRequestDto;
import org.musketeers.dto.request.UpdatePersonnelRequestDto;
import org.musketeers.dto.response.*;
import org.musketeers.entity.Personnel;
import org.musketeers.entity.Phone;
import org.musketeers.entity.enums.EGender;
import org.musketeers.entity.enums.PhoneType;
import org.musketeers.exception.ErrorType;
import org.musketeers.exception.PersonnelServiceException;
import org.musketeers.rabbitmq.model.*;
import org.musketeers.rabbitmq.producer.*;
import org.musketeers.repository.PersonnelRepository;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class PersonnelService extends ServiceManager<Personnel, String> {

    private final PersonnelRepository personnelRepository;

    private final JwtTokenManager jwtTokenManager;

    private final Cloudinary cloudinary;

    private final CreatePersonnelProducer createPersonnelProducer;

    private final GetCompanyIdFromSupervisorTokenProducer getCompanyIdFromSupervisorTokenProducer;

    private final GetCompanyDetailsByPersonnelRequestProducer getCompanyDetailsByPersonnelRequestProducer;

    private final UpdatePersonnelRequestProducer updatePersonnelRequestProducer;

    private final UpdateSupervisorProducer updateSupervisorProducer;

    private final SendDayOffStatusChangeMailProducer sendDayOffStatusChangeMailProducer;

    private final SendAdvanceStatusChangeMailProducer sendAdvanceStatusChangeMailProducer;

    private final SendAdvanceExpenseToCompanyServiceProducer sendAdvanceExpenseToCompanyServiceProducer;

    private final SendSpendingStatusChangeMailProducer sendSpendingStatusChangeMailProducer;

    private final SendSpendingExpenseToCompanyServiceProducer sendSpendingExpenseToCompanyServiceProducer;

    public PersonnelService(PersonnelRepository personnelRepository, JwtTokenManager jwtTokenManager, Cloudinary cloudinary, CreatePersonnelProducer createPersonnelProducer, GetCompanyIdFromSupervisorTokenProducer getCompanyIdFromSupervisorTokenProducer, GetCompanyDetailsByPersonnelRequestProducer getCompanyDetailsByPersonnelRequestProducer, UpdatePersonnelRequestProducer updatePersonnelRequestProducer, UpdateSupervisorProducer updateSupervisorProducer, SendDayOffStatusChangeMailProducer sendDayOffStatusChangeMailProducer, SendAdvanceStatusChangeMailProducer sendAdvanceStatusChangeMailProducer, SendAdvanceExpenseToCompanyServiceProducer sendAdvanceExpenseToCompanyServiceProducer, SendSpendingStatusChangeMailProducer sendSpendingStatusChangeMailProducer, SendSpendingExpenseToCompanyServiceProducer sendSpendingExpenseToCompanyServiceProducer) {
        super(personnelRepository);
        this.personnelRepository = personnelRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.cloudinary = cloudinary;
        this.createPersonnelProducer = createPersonnelProducer;
        this.getCompanyIdFromSupervisorTokenProducer = getCompanyIdFromSupervisorTokenProducer;
        this.getCompanyDetailsByPersonnelRequestProducer = getCompanyDetailsByPersonnelRequestProducer;
        this.updatePersonnelRequestProducer = updatePersonnelRequestProducer;
        this.updateSupervisorProducer = updateSupervisorProducer;
        this.sendDayOffStatusChangeMailProducer = sendDayOffStatusChangeMailProducer;
        this.sendAdvanceStatusChangeMailProducer = sendAdvanceStatusChangeMailProducer;
        this.sendAdvanceExpenseToCompanyServiceProducer = sendAdvanceExpenseToCompanyServiceProducer;
        this.sendSpendingStatusChangeMailProducer = sendSpendingStatusChangeMailProducer;
        this.sendSpendingExpenseToCompanyServiceProducer = sendSpendingExpenseToCompanyServiceProducer;
    }

    public GetPersonnelDetailsResponseDto getPersonnelDetailsByToken(String token) {
        List<String> claimsFromToken = jwtTokenManager.getClaimsFromToken(token);
        Personnel personnel = personnelRepository.findOptionalByAuthId(claimsFromToken.get(0)).orElseThrow(() -> new PersonnelServiceException(ErrorType.PERSONNEL_NOT_FOUND));
        String isSupervisor = claimsFromToken.get(1).equals("SUPERVISOR") ? "true" : "false";
        GetCompanyDetailsByPersonnelResponseModel companyDetailsResponseModel = GetCompanyDetailsByPersonnel(personnel, isSupervisor);
        return preparePersonnelDetailsResponseDtoFromModel(personnel, companyDetailsResponseModel);
    }

    private GetPersonnelDetailsResponseDto preparePersonnelDetailsResponseDtoFromModel(Personnel personnel, GetCompanyDetailsByPersonnelResponseModel model) {
        return GetPersonnelDetailsResponseDto.builder()
                .name(personnel.getName())
                .lastName(personnel.getLastName())
                .image(personnel.getImage())
                .email(personnel.getEmail())
                .phones(personnel.getPhones().stream().map(phone -> PhoneResponseDto.builder()
                        .phoneType(phone.getPhoneType().toString())
                        .phoneNumber(phone.getPhoneNumber())
                        .build()).toList())
                .addresses(personnel.getAddresses())
                .companyName(model.getCompanyName())
                .companyLogo(model.getCompanyLogo())
                .department(DepartmentResponseDto.builder()
                        .name(model.getDepartmentName())
                        .shiftHour(model.getShiftHour())
                        .breakHour(model.getBreakHour())
                        .build())
                .companyHolidays(model.getHolidays().stream()
                        .map(holidayString -> holidayString.split("\\*"))
                        .map(holidayStringArray -> HolidayResponseDto.builder()
                                .name(holidayStringArray[0])
                                .startTime(holidayStringArray[1])
                                .endTime(holidayStringArray[2])
                                .build())
                        .toList())
                .hrInfos(model.getHrInfos().stream()
                        .map(hrInfoString -> hrInfoString.split("\\*"))
                        .map(hrInfoStringArray -> HRInfoResponseDto.builder()
                                .firstName(hrInfoStringArray[0])
                                .lastName(hrInfoStringArray[1])
                                .email(hrInfoStringArray[2])
                                .phone(hrInfoStringArray[3])
                                .image(hrInfoStringArray[4])
                                .build())
                        .toList())
                .dateOfBirth(personnel.getDateOfBirth())
                .dateOfEmployment(personnel.getDateOfEmployment())
                .salary(personnel.getSalary())
                .dayOff(personnel.getDayOff())
                .advanceQuota(personnel.getAdvanceQuota())
                .build();
    }

    private GetCompanyDetailsByPersonnelResponseModel GetCompanyDetailsByPersonnel(Personnel personnel, String isSupervisor) {
        return getCompanyDetailsByPersonnelRequestProducer.getCompanyDetailsByPersonnelFromCompanyService(List.of(personnel.getId(), personnel.getCompanyId(), isSupervisor));
    }

    public List<Personnel> getAllPersonnel() {
        return findAll();
    }

    public Boolean softDeletePersonnelById(String id) {
        return softDeleteById(id);
    }

    public String createPersonnel(CreatePersonnelRequestDto dto) {
        String authId = getPersonnelAuthId(dto);
        Personnel personnel = convertDtoToPersonnel(authId, dto);
        save(personnel);
        sendPersonnelInfoToCompanyQueue(personnel);
        return "Adding personnel successful!";
    }

    private String getPersonnelAuthId(CreatePersonnelRequestDto dto){
        return createPersonnelProducer.sendPersonnelInfoToAuth(CreatePersonnelAuthModel.builder()
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .name(dto.getName())
                .build());
    }

    private Personnel convertDtoToPersonnel(String authId, CreatePersonnelRequestDto dto) {
        return Personnel.builder()
                .authId(authId)
                .name(dto.getName())
                .lastName(dto.getLastName())
                .EGender(EGender.valueOf(dto.getGender()))
                .identityNumber(dto.getIdentityNumber())
                .email(dto.getEmail())
                .image(dto.getImage())
                .addresses(Arrays.asList(dto.getAddress()))
                .phones(Arrays.asList(Phone.builder()
                        .phoneType(PhoneType.PERSONAL)
                        .phoneNumber(dto.getPhone())
                        .build()))
                .companyId(dto.getCompanyId())
                .departmentId(dto.getDepartmentId())
                .position(dto.getPosition())
                .dateOfEmployment(dto.getDateOfEmployment())
                .dateOfBirth(dto.getDateOfBirth())
                .salary(dto.getSalary())
                .advanceQuota(dto.getSalary())
                .build();
    }

    private void sendPersonnelInfoToCompanyQueue(Personnel personnel){
        CreatePersonnelCompanyModel requestModel = CreatePersonnelCompanyModel.builder()
                .personnelId(personnel.getId())
                .companyId(personnel.getCompanyId())
                .departmentId(personnel.getDepartmentId())
                .expenseDescription("SALARY")
                .expenseAmount(personnel.getSalary())
                .expenseDate(Instant.ofEpochMilli(personnel.getCreatedAt())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate())
                .build();
        createPersonnelProducer.sendPersonnelInfoToCompany(requestModel);
    }

    public List<Personnel> getAllByCompanyId(String token) {
        GetCompanyIdFromSupervisorTokenModel model = GetCompanyIdFromSupervisorTokenModel.builder().token(token).build();
        String companyId = getCompanyIdFromSupervisorTokenProducer.getCompanyIdFromSupervisorToken(model);
        return personnelRepository.findAllByCompanyId(companyId);
    }

    public GetPersonnelIdAndCompanyIdByTokenResponseModel getPersonnelIdAndCompanyIdByToken(GetPersonnelIdAndCompanyIdByTokenRequestModel model) {
        String authId = jwtTokenManager.getClaimsFromToken(model.getToken()).get(0);
        Personnel personnel = personnelRepository.findOptionalByAuthId(authId).orElseThrow(() -> new PersonnelServiceException(ErrorType.PERSONNEL_NOT_FOUND));
        return GetPersonnelIdAndCompanyIdByTokenResponseModel.builder()
                .personnelId(personnel.getId())
                .companyId(personnel.getCompanyId())
                .build();
    }

    public List<GetPersonnelDetailsByCommentResponseModel> getPersonnelInfoByPersonnelId(List<String> personnelIds) {
        List<Personnel> personnelList = new ArrayList<>();
        personnelIds.forEach(personnelId -> personnelList.add(findById(personnelId)));
        List<GetPersonnelDetailsByCommentResponseModel> personnelModelList = new ArrayList<>();
        personnelList.forEach(personnel -> {
            personnelModelList.add(GetPersonnelDetailsByCommentResponseModel.builder()
                    .name(personnel.getName())
                    .lastName(personnel.getLastName())
                    .gender(personnel.getEGender().toString())
                    .image(personnel.getImage())
                    .build());
        });
        return personnelModelList;
    }

    public void createPersonnelFromSupervisor(CreatePersonnelFromSupervisorModel model) {
        Personnel personnel = convertSupervisorModelToPersonnel(model);
        save(personnel);
    }

    private Personnel convertSupervisorModelToPersonnel(CreatePersonnelFromSupervisorModel model) {
        return Personnel.builder()
                .authId(model.getAuthId())
                .name(model.getName())
                .lastName(model.getLastName())
                .EGender(EGender.valueOf(model.getGender()))
                .identityNumber(model.getIdentityNumber())
                .email(model.getEmail())
                .image(model.getImage())
                .addresses(Arrays.asList(model.getAddress()))
                .phones(Arrays.asList(Phone.builder()
                        .phoneType(PhoneType.PERSONAL)
                        .phoneNumber(model.getPhone())
                        .build()))
                .companyId(model.getCompanyId())
                .dateOfBirth(model.getDateOfBirth())
                .build();
    }

    public Boolean updatePersonnelProfile(UpdatePersonnelRequestDto dto) {
        List<String> authIdAndRole = jwtTokenManager.getClaimsFromToken(dto.getToken());
        Personnel personnel = personnelRepository.findOptionalByAuthId(authIdAndRole.get(0)).orElseThrow(() -> new PersonnelServiceException(ErrorType.PERSONNEL_NOT_FOUND));
        UpdatePersonnelRequestModel requestModelForAuth = UpdatePersonnelRequestModel.builder()
                .authId(authIdAndRole.get(0))
                .phone(dto.getPhones().get(0))
                .email(dto.getEmail())
                .build();
        // BURAYA BAKILACAK...
        if(!updatePersonnelRequestProducer.updateAuthService(requestModelForAuth)) return false;
        preparePersonnelForUpdate(personnel, dto);
        update(personnel);
        if(authIdAndRole.get(1).equalsIgnoreCase("SUPERVISOR")) sendUpdateRequestToSupervisorService(authIdAndRole.get(0), personnel);
        return true;
    }

    private void sendUpdateRequestToSupervisorService(String authId, Personnel personnel) {
        UpdateSupervisorModel requestModelForSupervisor = UpdateSupervisorModel.builder()
                .authId(authId)
                .name(personnel.getName())
                .lastName(personnel.getLastName())
                .email(personnel.getEmail())
                .phones(personnel.getPhones().stream()
                        .map(Phone::getPhoneNumber)
                        .toList())
                .image(personnel.getImage())
                .build();
        updateSupervisorProducer.updateSupervisorService(requestModelForSupervisor);
    }

    private void preparePersonnelForUpdate(Personnel personnel, UpdatePersonnelRequestDto dto) {
        long startTime = System.currentTimeMillis();
        personnel.setName(dto.getName());
        personnel.setLastName(dto.getLastName());
        personnel.setEmail(dto.getEmail());
        List<Phone> personnelPhones = new ArrayList<>();
        personnelPhones.add(Phone.builder()
                .phoneType(PhoneType.PERSONAL)
                .phoneNumber(dto.getPhones().get(0))
                .build());
        if(dto.getPhones().size() > 1) {
            personnelPhones.add(Phone.builder()
                    .phoneType(PhoneType.WORK)
                    .phoneNumber(dto.getPhones().get(1))
                    .build());
        }
        personnel.setPhones(personnelPhones);
//        String profilePictureFileName = personnel.getId() + ".jpg";
//        String profilePicturePath = "H:\\Program Files\\PROJECTS\\human-resources-management-system\\personnel-service\\src\\main\\resources\\uploads\\" + profilePictureFileName;
//        try {
//            dto.getProfileImage().transferTo(new File(profilePicturePath));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        if(Optional.ofNullable(dto.getProfileImageUrl()).isEmpty()) {
            try {
                byte[] fileBytes = dto.getProfileImage().getBytes();
                Map<?, ?> response = cloudinary.uploader().upload(fileBytes, ObjectUtils.emptyMap());
                String url = (String) response.get("url");
                personnel.setImage(url);
                return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        personnel.setImage(dto.getProfileImageUrl());
    }

    public String getCompanyIdFromAuthId(String authId) {
        Personnel personnel = personnelRepository.findOptionalByAuthId(authId).orElseThrow(() -> new PersonnelServiceException(ErrorType.PERSONNEL_NOT_FOUND));
        return personnel.getCompanyId();
    }

    public GetPersonnelIdAndCompanyIdForDayOffRequestModel getPersonnelIdAndCompanyIdForDayOffRequest(String authId) {
        Personnel personnel = personnelRepository.findOptionalByAuthId(authId).orElseThrow(() -> new PersonnelServiceException(ErrorType.PERSONNEL_NOT_FOUND));
        return GetPersonnelIdAndCompanyIdForDayOffRequestModel.builder()
                .personnelId(personnel.getId())
                .companyId(personnel.getCompanyId())
                .build();
    }

    public void handleDayOffRequestStatusChange(SendDayOffStatusChangeNotificationModel model) {
        Personnel personnel = findById(model.getPersonnelId());
        if(model.getUpdatedStatus().equals("ACCEPTED")) {
            long reduceAmount = ChronoUnit.DAYS.between(model.getRequestStartDate(), model.getRequestEndDate());
            personnel.setDayOff(personnel.getDayOff() - (reduceAmount+1L));
            update(personnel);
        }
        sendDayOffStatusChangeNotificationToPersonnelMail(model, personnel);
    }

    private void sendDayOffStatusChangeNotificationToPersonnelMail(SendDayOffStatusChangeNotificationModel model, Personnel personnel) {
        SendDayOffStatusChangeMailModel requestModel = SendDayOffStatusChangeMailModel.builder()
                .name(personnel.getName())
                .lastName(personnel.getLastName())
                .email(personnel.getEmail())
                .requestReason(model.getRequestReason())
                .requestDescription(model.getRequestDescription())
                .requestStartDate(model.getRequestStartDate())
                .requestEndDate(model.getRequestEndDate())
                .updatedStatus(model.getUpdatedStatus())
                .requestCreatedAt(model.getRequestCreatedAt())
                .requestUpdatedAt(model.getRequestUpdatedAt())
                .build();
        sendDayOffStatusChangeMailProducer.sendMailToPersonnel(requestModel);
    }

    public List<GetPersonnelDetailsForDayOffRequestModel> getPersonnelDetailsForDayOffRequest(List<String> personnelIds) {
        List<Personnel> personnelList = new ArrayList<>();
        personnelIds.forEach(personnelId -> personnelList.add(findById(personnelId)));
        List<GetPersonnelDetailsForDayOffRequestModel> personnelModelList = new ArrayList<>();
        personnelList.forEach(personnel -> {
            personnelModelList.add(GetPersonnelDetailsForDayOffRequestModel.builder()
                    .personnelId(personnel.getId())
                    .name(personnel.getName())
                    .lastName(personnel.getLastName())
                    .image(personnel.getImage())
                    .email(personnel.getEmail())
                    .dayOff(String.valueOf(personnel.getDayOff()))
                    .build());
        });
        return personnelModelList;
    }

    public GetPersonnelIdAndCompanyIdForAdvanceRequestModel getPersonnelIdAndCompanyIdForAdvanceRequest(String authId) {
        Personnel personnel = personnelRepository.findOptionalByAuthId(authId).orElseThrow(() -> new PersonnelServiceException(ErrorType.PERSONNEL_NOT_FOUND));
        return GetPersonnelIdAndCompanyIdForAdvanceRequestModel.builder()
                .personnelId(personnel.getId())
                .companyId(personnel.getCompanyId())
                .build();
    }

    public void handleAdvanceRequestStatusChange(SendAdvanceStatusChangeNotificationModel model) {
        Personnel personnel = findById(model.getPersonnelId());
        if(model.getUpdatedStatus().equals("ACCEPTED")) {
            Double reduceAmount = model.getRequestAmount();
            personnel.setAdvanceQuota(personnel.getAdvanceQuota() - reduceAmount);
            update(personnel);
            sendAdvanceExpenseToCompanyServiceProducer.sendExpense(SendAdvanceExpenseToCompanyServiceModel.builder()
                    .companyId(personnel.getCompanyId())
                    .description("ADVANCE")
                    .amount(reduceAmount)
                    .expenseDate(Instant.ofEpochMilli(personnel.getUpdatedAt())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate())
                    .build());
        }
        sendAdvanceStatusChangeNotificationToPersonnelMail(model, personnel);
    }

    private void sendAdvanceStatusChangeNotificationToPersonnelMail(SendAdvanceStatusChangeNotificationModel model, Personnel personnel) {
        SendAdvanceStatusChangeMailModel requestModel = SendAdvanceStatusChangeMailModel.builder()
                .name(personnel.getName())
                .lastName(personnel.getLastName())
                .email(personnel.getEmail())
                .requestReason(model.getRequestReason())
                .requestDescription(model.getRequestDescription())
                .requestAmount(model.getRequestAmount())
                .updatedStatus(model.getUpdatedStatus())
                .requestCreatedAt(model.getRequestCreatedAt())
                .requestUpdatedAt(model.getRequestUpdatedAt())
                .build();
        sendAdvanceStatusChangeMailProducer.sendMailToPersonnel(requestModel);
    }

    public List<GetPersonnelDetailsForAdvanceRequestModel> getPersonnelDetailsForAdvanceRequest(List<String> personnelIds) {
        List<Personnel> personnelList = new ArrayList<>();
        personnelIds.forEach(personnelId -> personnelList.add(findById(personnelId)));
        List<GetPersonnelDetailsForAdvanceRequestModel> personnelModelList = new ArrayList<>();
        personnelList.forEach(personnel -> {
            personnelModelList.add(GetPersonnelDetailsForAdvanceRequestModel.builder()
                    .personnelId(personnel.getId())
                    .name(personnel.getName())
                    .lastName(personnel.getLastName())
                    .image(personnel.getImage())
                    .email(personnel.getEmail())
                    .advanceQuota(String.valueOf(personnel.getAdvanceQuota()))
                    .build());
        });
        return personnelModelList;
    }

    public GetPersonnelIdAndCompanyIdForSpendingRequestModel getPersonnelIdAndCompanyIdForSpendingRequest(String authId) {
        Personnel personnel = personnelRepository.findOptionalByAuthId(authId).orElseThrow(() -> new PersonnelServiceException(ErrorType.PERSONNEL_NOT_FOUND));
        return GetPersonnelIdAndCompanyIdForSpendingRequestModel.builder()
                .personnelId(personnel.getId())
                .companyId(personnel.getCompanyId())
                .build();
    }

    public void handleSpendingRequestStatusChange(SendSpendingStatusChangeNotificationModel model) {
        Personnel personnel = findById(model.getPersonnelId());
        if(model.getUpdatedStatus().equals("ACCEPTED")) {
            sendSpendingExpenseToCompanyServiceProducer.sendExpense(SendSpendingExpenseToCompanyServiceModel.builder()
                    .companyId(personnel.getCompanyId())
                    .description("SPENDING")
                    .amount(model.getRequestAmount())
                    .currency(model.getRequestCurrency())
                    .expenseDate(Instant.ofEpochMilli(personnel.getUpdatedAt())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate())
                    .build());
        }
        sendSpendingStatusChangeNotificationToPersonnelMail(model, personnel);
    }

    private void sendSpendingStatusChangeNotificationToPersonnelMail(SendSpendingStatusChangeNotificationModel model, Personnel personnel) {
        SendSpendingStatusChangeMailModel requestModel = SendSpendingStatusChangeMailModel.builder()
                .name(personnel.getName())
                .lastName(personnel.getLastName())
                .email(personnel.getEmail())
                .requestReason(model.getRequestReason())
                .requestDescription(model.getRequestDescription())
                .requestAmount(model.getRequestAmount())
                .requestCurrency(model.getRequestCurrency())
                .requestSpendingDate(model.getRequestSpendingDate())
                .requestAttachments(model.getRequestAttachments())
                .updatedStatus(model.getUpdatedStatus())
                .requestCreatedAt(model.getRequestCreatedAt())
                .requestUpdatedAt(model.getRequestUpdatedAt())
                .build();
        sendSpendingStatusChangeMailProducer.sendMailToPersonnel(requestModel);
    }

    public List<GetPersonnelDetailsForSpendingRequestModel> getPersonnelDetailsForSpendingRequest(List<String> personnelIds) {
        List<Personnel> personnelList = new ArrayList<>();
        personnelIds.forEach(personnelId -> personnelList.add(findById(personnelId)));
        List<GetPersonnelDetailsForSpendingRequestModel> personnelModelList = new ArrayList<>();
        personnelList.forEach(personnel -> {
            personnelModelList.add(GetPersonnelDetailsForSpendingRequestModel.builder()
                    .personnelId(personnel.getId())
                    .name(personnel.getName())
                    .lastName(personnel.getLastName())
                    .image(personnel.getImage())
                    .email(personnel.getEmail())
                    .build());
        });
        return personnelModelList;

    }
}
