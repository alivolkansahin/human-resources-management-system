package org.musketeers.service;

import org.musketeers.dto.request.CreatePersonnelRequestDto;
import org.musketeers.dto.response.*;
import org.musketeers.entity.Personnel;
import org.musketeers.entity.Phone;
import org.musketeers.entity.enums.Gender;
import org.musketeers.entity.enums.PhoneType;
import org.musketeers.exception.ErrorType;
import org.musketeers.exception.PersonnelServiceException;
import org.musketeers.rabbitmq.model.*;
import org.musketeers.rabbitmq.producer.CreatePersonnelProducer;
import org.musketeers.rabbitmq.producer.GetCompanyDetailsByPersonnelRequestProducer;
import org.musketeers.rabbitmq.producer.GetCompanyIdFromSupervisorTokenProducer;
import org.musketeers.repository.PersonnelRepository;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PersonnelService extends ServiceManager<Personnel, String> {

    private final PersonnelRepository personnelRepository;

    private final JwtTokenManager jwtTokenManager;

    private final CreatePersonnelProducer createPersonnelProducer;

    private final GetCompanyIdFromSupervisorTokenProducer getCompanyIdFromSupervisorTokenProducer;

    private final GetCompanyDetailsByPersonnelRequestProducer getCompanyDetailsByPersonnelRequestProducer;

    public PersonnelService(PersonnelRepository personnelRepository, JwtTokenManager jwtTokenManager, CreatePersonnelProducer createPersonnelProducer, GetCompanyIdFromSupervisorTokenProducer getCompanyIdFromSupervisorTokenProducer, GetCompanyDetailsByPersonnelRequestProducer getCompanyDetailsByPersonnelRequestProducer) {
        super(personnelRepository);
        this.personnelRepository = personnelRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.createPersonnelProducer = createPersonnelProducer;
        this.getCompanyIdFromSupervisorTokenProducer = getCompanyIdFromSupervisorTokenProducer;
        this.getCompanyDetailsByPersonnelRequestProducer = getCompanyDetailsByPersonnelRequestProducer;
    }

    public GetPersonnelDetailsResponseDto getPersonnelDetailsByToken(String token) {
        List<String> claimsFromToken = jwtTokenManager.getClaimsFromToken(token);
        Personnel personnel = personnelRepository.findOptionalByAuthId(claimsFromToken.get(0)).orElseThrow(() -> new PersonnelServiceException(ErrorType.PERSONNEL_NOT_FOUND));
        String isSupervisor = claimsFromToken.get(1).equals("SUPERVISOR") ? "true" : "false";
        GetCompanyDetailsByPersonnelResponseModel companyDetailsResponseModel = GetCompanyDetailsByPersonnel(personnel, isSupervisor);
        return preparePersonnelDetailsResponseDtoFromModel(personnel, companyDetailsResponseModel);
    }

    private GetPersonnelDetailsResponseDto preparePersonnelDetailsResponseDtoFromModel(Personnel personnel, GetCompanyDetailsByPersonnelResponseModel model) {
/*        List<List<String>> holidayList = model.getHolidays().stream().map(holiday -> Arrays.asList(holiday.split("\\*"))).toList();
        List<HolidayResponseDto> holidayDtoList = holidayList.stream().map(holidayListItem -> HolidayResponseDto.builder()
                        .name(holidayListItem.get(0))
                        .duration(Integer.valueOf(holidayListItem.get(1)))
                        .build())
                .toList();
        System.out.println("HOLIDAYDTO: " + holidayDtoList.toString());

        List<List<String>> hrInfoList = model.getHrInfos().stream().map(hrInfo -> Arrays.asList(hrInfo.split("\\*"))).toList();
        List<HRInfoResponseDto> hrInfoDtoList = hrInfoList.stream().map(hrInfoListItem -> HRInfoResponseDto.builder()
                        .firstName(hrInfoListItem.get(0))
                        .lastName(hrInfoListItem.get(1))
                        .email(hrInfoListItem.get(2))
                        .phone(hrInfoListItem.get(3))
                        .build())
                .toList();
        System.out.println("HRINFODTO: " + hrInfoDtoList.toString());

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
                        .shifts(model.getShifts())
                        .breaks(model.getBreaks())
                        .build())
                .companyHolidays(holidayDtoList)
                .hrInfos(hrInfoDtoList)
                .dateOfBirth(personnel.getDateOfBirth())
                .dateOfEmployment(personnel.getDateOfEmployment())
                .salary(personnel.getSalary())
                .dayOff(personnel.getDayOff())
                .build();*/

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
                        .shifts(model.getShifts())
                        .breaks(model.getBreaks())
                        .build())
                .companyHolidays(model.getHolidays().stream()
                        .map(holidayString -> holidayString.split("\\*"))
                        .map(holidayStringArray -> HolidayResponseDto.builder()
                                .name(holidayStringArray[0])
                                .duration(Integer.valueOf(holidayStringArray[1]))
                                .build())
                        .toList())
                .hrInfos(model.getHrInfos().stream()
                        .map(hrInfoString -> hrInfoString.split("\\*"))
                        .map(hrInfoStringArray -> HRInfoResponseDto.builder()
                                .firstName(hrInfoStringArray[0])
                                .lastName(hrInfoStringArray[1])
                                .email(hrInfoStringArray[2])
                                .phone(hrInfoStringArray[3])
                                .build())
                        .toList())
                .dateOfBirth(personnel.getDateOfBirth())
                .dateOfEmployment(personnel.getDateOfEmployment())
                .salary(personnel.getSalary())
                .dayOff(personnel.getDayOff())
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
                .gender(dto.getGender().equalsIgnoreCase("male") ? Gender.MALE : Gender.FEMALE)
                .identityNumber(dto.getIdentityNumber())
                .email(dto.getEmail())
                .image(dto.getImage() != null ? dto.getImage() : (dto.getGender()).equalsIgnoreCase("male") ?
                        "https://us.123rf.com/450wm/thesomeday123/thesomeday1231712/thesomeday123171200009/91087331-default-avatar-profile-icon-for-male-grey-photo-placeholder-illustrations-vector.jpg?ver=6"
                        :
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ6SR5emlvKa5Trq207GwkpiamFuQFskm8zLniDY04frA&s")
                .addresses(Arrays.asList(dto.getAddress()))
                .phones(Arrays.asList(Phone.builder().phoneType(PhoneType.PERSONAL).phoneNumber(dto.getPhone()).build()))
                .companyId(dto.getCompanyId())
                .departmentId(dto.getDepartmentId())
                .position(dto.getPosition())
                .dateOfEmployment(dto.getDateOfEmployment())
                .dateOfBirth(dto.getDateOfBirth())
                .salary(dto.getSalary())
                .build();
    }

    private void sendPersonnelInfoToCompanyQueue(Personnel personnel){
        createPersonnelProducer.sendPersonnelInfoToCompany(CreatePersonnelCompanyModel.builder()
                .personnelId(personnel.getId())
                .companyId(personnel.getCompanyId())
                .departmentId(personnel.getDepartmentId())
                .build());
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
        List<Personnel> personnelList = personnelRepository.findAllById(personnelIds);
        List<GetPersonnelDetailsByCommentResponseModel> personnelModelList = new ArrayList<>();
        personnelList.forEach(personnel -> {
            personnelModelList.add(GetPersonnelDetailsByCommentResponseModel.builder()
                    .name(personnel.getName())
                    .lastName(personnel.getLastName())
                    .gender(personnel.getGender().toString())
                    .image(personnel.getImage())
                    .dateOfEmployment(personnel.getDateOfEmployment())
                    .build());
        });
        return personnelModelList;
    }

    public void createPersonnelFromSupervisor(CreatePersonnelFromSupervisorModel model) {
        Personnel personnel = convertModelToPersonnel(model);
        save(personnel);
    }

    private Personnel convertModelToPersonnel(CreatePersonnelFromSupervisorModel model) {
        return Personnel.builder()
                .authId(model.getAuthId())
                .name(model.getName())
                .lastName(model.getLastName())
                .gender(model.getGender().equalsIgnoreCase("male") ? Gender.MALE : Gender.FEMALE)
                .identityNumber(model.getIdentityNumber())
                .email(model.getEmail())
                .image(model.getImage())
                .addresses(Arrays.asList(model.getAddress()))
                .phones(Arrays.asList(Phone.builder().phoneType(PhoneType.PERSONAL).phoneNumber(model.getPhone()).build()))
                .companyId(model.getCompanyId())
                .dateOfBirth(model.getDateOfBirth())
                .build();
    }
}
