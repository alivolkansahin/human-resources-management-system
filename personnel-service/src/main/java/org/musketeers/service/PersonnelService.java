package org.musketeers.service;

import org.musketeers.dto.request.CreatePersonnelRequestDto;
import org.musketeers.dto.request.GetPersonnelByCompanyRequestDto;
import org.musketeers.entity.Personnel;
import org.musketeers.entity.Phone;
import org.musketeers.entity.enums.Gender;
import org.musketeers.entity.enums.PhoneType;
import org.musketeers.exception.ErrorType;
import org.musketeers.exception.PersonnelServiceException;
import org.musketeers.rabbitmq.model.*;
import org.musketeers.rabbitmq.producer.CreatePersonnelProducer;
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

    public PersonnelService(PersonnelRepository personnelRepository, JwtTokenManager jwtTokenManager, CreatePersonnelProducer createPersonnelProducer, GetCompanyIdFromSupervisorTokenProducer getCompanyIdFromSupervisorTokenProducer) {
        super(personnelRepository);
        this.personnelRepository = personnelRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.createPersonnelProducer = createPersonnelProducer;
        this.getCompanyIdFromSupervisorTokenProducer = getCompanyIdFromSupervisorTokenProducer;
    }

    public Personnel getPersonnelByToken(String id) { // İŞLEMLER LAZIM PERSONAL SAYFASI İÇİN
//        jwtTokenManager.getClaimsFromToken(id).get(0);
        return findById(id);
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
                .image(dto.getImage() != null ? dto.getImage() : (dto.getGender()).equalsIgnoreCase("male") ? "imagerkek" : "imagekadın") // BU KONTROL FRONTTA DA YAPILABİLİR
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
}
