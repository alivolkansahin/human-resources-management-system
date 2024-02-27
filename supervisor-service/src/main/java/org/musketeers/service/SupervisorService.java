package org.musketeers.service;

import org.musketeers.entity.Phone;
import org.musketeers.entity.Supervisor;
import org.musketeers.entity.enums.ActivationStatus;
import org.musketeers.entity.enums.EGender;
import org.musketeers.entity.enums.PhoneType;
import org.musketeers.exception.ErrorType;
import org.musketeers.exception.SupervisorServiceException;
import org.musketeers.rabbitmq.model.*;
import org.musketeers.rabbitmq.producer.CreateCompanyProducer;
import org.musketeers.rabbitmq.producer.CreatePersonnelFromSupervisorProducer;
import org.musketeers.repository.SupervisorRepository;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupervisorService extends ServiceManager<Supervisor, String> {

    private final SupervisorRepository supervisorRepository;

    private final JwtTokenManager jwtTokenManager;

    private final CreateCompanyProducer createCompanyProducer;

    private final CreatePersonnelFromSupervisorProducer createPersonnelFromSupervisorProducer;

    public SupervisorService(SupervisorRepository supervisorRepository, JwtTokenManager jwtTokenManager, CreateCompanyProducer createCompanyProducer, CreatePersonnelFromSupervisorProducer createPersonnelFromSupervisorProducer) {
        super(supervisorRepository);
        this.supervisorRepository = supervisorRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.createCompanyProducer = createCompanyProducer;
        this.createPersonnelFromSupervisorProducer = createPersonnelFromSupervisorProducer;
    }

    public Supervisor register(Supervisor supervisor) {
        return save(supervisor);
    }

    public Supervisor getSupervisorById(String id) {
//        jwtTokenManager.getClaimsFromToken(id).get(0)
        return findById(id);
    }

    public Supervisor getSupervisorByAuthId(String authId) {
        return supervisorRepository.findOptionalByAuthId(authId).orElseThrow(() -> new SupervisorServiceException(ErrorType.SUPERVISOR_NOT_FOUND));
    }

    public Boolean softDeleteSupervisorById(String id) {
        return softDeleteById(id);
    }

    public void activate(Supervisor supervisor) {
        boolean haveContractRequest = supervisor.getIsCompanyFirstRegistration();
        CreateCompanyRequestModel createCompanyRequestModel = CreateCompanyRequestModel.builder()
                .supervisorId(supervisor.getId())
                .companyName(supervisor.getCompanyName())
                .contractName(haveContractRequest ? supervisor.getContractName() : "")
                .contractDuration(haveContractRequest ? supervisor.getContractDuration() : 0)
                .contractCost(haveContractRequest ? supervisor.getContractCost() : 0d)
                .contractCurrency(haveContractRequest ? supervisor.getContractCurrency() : "")
                .build();
        CreateCompanyResponseModel createCompanyResponseModel = createCompanyProducer.createCompanyAndReturn(createCompanyRequestModel);
        supervisor.setCompanyId(createCompanyResponseModel.getCompanyId());
        supervisor.setActivationStatus(ActivationStatus.ACTIVATED);
        supervisor.setIsCompanyFirstRegistration(null);
        supervisor.setContractName(null);
        supervisor.setContractDuration(null);
        supervisor.setContractCost(null);
        supervisor.setContractCurrency(null);
        update(supervisor);
        preparePersonnelModelFromSupervisor(supervisor);
    }

    private void preparePersonnelModelFromSupervisor(Supervisor supervisor){
        CreatePersonnelFromSupervisorModel model = CreatePersonnelFromSupervisorModel.builder()
                .authId(supervisor.getAuthId())
                .name(supervisor.getName())
                .lastName(supervisor.getLastName())
                .gender(supervisor.getEGender().toString())
                .identityNumber(supervisor.getIdentityNumber())
                .email(supervisor.getEmail())
                .image(supervisor.getImage())
                .address(supervisor.getAddresses().get(0))
                .phone(supervisor.getPhones().get(0).getPhoneNumber())
                .companyId(supervisor.getCompanyId())
                .dateOfBirth(supervisor.getDateOfBirth())
                .build();
        createPersonnelFromSupervisorProducer.createPersonnelFromSupervisor(model);
    }

    public List<GetCompanySupervisorResponseModel> getSupervisorByIds(List<String> supervisorIds) {
        List<Supervisor> supervisors = supervisorRepository.findAllById(supervisorIds);
        return supervisors.stream().map(supervisor -> GetCompanySupervisorResponseModel.builder()
                .name(supervisor.getName())
                .lastName(supervisor.getLastName())
                .gender(supervisor.getEGender().toString())
                .image(supervisor.getImage())
                .dateOfBirth(supervisor.getDateOfBirth().toString())
                .build()).toList();
    }

    public void updateSupervisorInfo(UpdateSupervisorModel model) {
        Supervisor supervisor = supervisorRepository.findOptionalByAuthId(model.getAuthId()).orElseThrow(() -> new SupervisorServiceException(ErrorType.SUPERVISOR_NOT_FOUND));
        supervisor.setName(model.getName());
        supervisor.setLastName(model.getLastName());
        supervisor.setEmail(model.getEmail());
        Phone personalPhone = Phone.builder().phoneType(PhoneType.PERSONAL).phoneNumber(model.getPhones().get(0)).build();
        if(model.getPhones().size() > 1) {
            Phone workPhone = Phone.builder().phoneType(PhoneType.WORK).phoneNumber(model.getPhones().get(1)).build();
            supervisor.setPhones(List.of(personalPhone, workPhone));
        } else {
            supervisor.setPhones(List.of(personalPhone));
        }
        supervisor.setImage(model.getImage());
        update(supervisor);
    }

    public void createSupervisor(RegisterSupervisorModel model) {
        List<String> address = new ArrayList<>();
        List<Phone> phones = new ArrayList<>();
        address.add(model.getAddress());
        phones.add(Phone.builder().phoneType(PhoneType.PERSONAL).phoneNumber(model.getPhone()).build());
        boolean isMale = model.getGender().equalsIgnoreCase("MALE");
        Supervisor supervisor = Supervisor.builder()
                .authId(model.getAuthid())
                .name(model.getName())
                .lastName(model.getSurName())
                .EGender(EGender.valueOf(model.getGender()))
                .identityNumber(model.getIdentityNumber())
                .email(model.getEmail())
                .image(isMale ? "https://i.imgur.com/ltRBj9D.png" : "https://i.imgur.com/BNXkMgI.png")
                .addresses(address)
                .phones(phones)
                .dateOfBirth(model.getDateOfBirth())
                .companyName(model.getCompanyName())
                .isCompanyFirstRegistration(model.getIsCompanyFirstRegistration())
                .build();
        if(model.getIsCompanyFirstRegistration()){
            supervisor.setContractName(model.getContractName());
            supervisor.setContractDuration(model.getContractDuration());
            supervisor.setContractCost(model.getContractCost());
            supervisor.setContractCurrency(model.getContractCurrency());
        }
        save(supervisor);
    }

    public List<GetSupervisorResponseModel> getPendingSupervisors(GetSupervisorRequestModel model) {
        List<Supervisor> pendingSupervisors = findAll().stream().filter(supervisor -> supervisor.getActivationStatus().equals(ActivationStatus.PENDING)).toList();
        List<GetSupervisorResponseModel> pendingSupervisorsModel = new ArrayList<>();
        pendingSupervisors.forEach(supervisor -> {
            boolean haveContractRequest = supervisor.getIsCompanyFirstRegistration();
            GetSupervisorResponseModel pendingSupervisorModel = GetSupervisorResponseModel.builder()
                    .id(supervisor.getId())
                    .authId(supervisor.getAuthId())
                    .name(supervisor.getName())
                    .lastName(supervisor.getLastName())
                    .gender(supervisor.getEGender().toString())
                    .identityNumber(supervisor.getIdentityNumber())
                    .email(supervisor.getEmail())
                    .image(supervisor.getImage())
                    .addresses(supervisor.getAddresses())
                    .phones(supervisor.getPhones().stream().map(Phone::toString).toList())
                    .companyName(supervisor.getCompanyName())
                    .dateOfBirth(supervisor.getDateOfBirth())
                    .activationStatus(supervisor.getActivationStatus().toString())
                    .isCompanyFirstRegistration(supervisor.getIsCompanyFirstRegistration())
                    .contractName(haveContractRequest ? supervisor.getContractName() : "")
                    .contractDuration(haveContractRequest ? supervisor.getContractDuration() : 0)
                    .contractCost(haveContractRequest ? supervisor.getContractCost() : 0d)
                    .contractCurrency(haveContractRequest ? supervisor.getContractCurrency() : "")
                    .build();
            pendingSupervisorsModel.add(pendingSupervisorModel);
        });
        return pendingSupervisorsModel;
    }

    public void handleAdminDecisionForSupervisorRegistration(SupervisorRegistrationDecisionModel model) {
        Supervisor supervisor = getSupervisorByAuthId(model.getSupervisorAuthId());
        if(supervisor.getActivationStatus().equals(ActivationStatus.ACTIVATED)) return;
        if(model.getDecision()) {
            activate(supervisor);
        } else {
            hardDelete(supervisor);
        }
    }

    public String sendSupervisorsCompanyId(String authId) {
        return getSupervisorByAuthId(authId).getCompanyId();
    }

    public String sendSupervisorsCompanyId(GetCompanyIdFromSupervisorTokenModel model) {
        String authId = jwtTokenManager.getClaimsFromToken(model.getToken()).get(0);
        return getSupervisorByAuthId(authId).getCompanyId();
    }
}
