package org.musketeers.service;

import org.musketeers.dto.request.CreatePersonnelRequestDto;
import org.musketeers.entity.Personnel;
import org.musketeers.entity.Phone;
import org.musketeers.entity.enums.Gender;
import org.musketeers.entity.enums.PhoneType;
import org.musketeers.rabbitmq.model.CreatePersonnelAuthModel;
import org.musketeers.rabbitmq.model.CreatePersonnelCompanyModel;
import org.musketeers.rabbitmq.producer.CreatePersonnelProducer;
import org.musketeers.repository.PersonnelRepository;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PersonnelService extends ServiceManager<Personnel, String> {

    private final PersonnelRepository personnelRepository;

    private final JwtTokenManager jwtTokenManager;

    private final CreatePersonnelProducer createPersonnelProducer;

    public PersonnelService(PersonnelRepository personnelRepository, JwtTokenManager jwtTokenManager, CreatePersonnelProducer createPersonnelProducer) {
        super(personnelRepository);
        this.personnelRepository = personnelRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.createPersonnelProducer = createPersonnelProducer;
    }

    public Personnel getPersonnelById(String id) {
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

}
