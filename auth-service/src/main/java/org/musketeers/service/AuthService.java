package org.musketeers.service;


import jakarta.transaction.Transactional;
import org.musketeers.dto.request.GuestRegisterRequestDto;
import org.musketeers.dto.request.SupervisorRegisterRequestDto;
import org.musketeers.entity.Auth;
import org.musketeers.entity.enums.ERole;
import org.musketeers.entity.enums.EStatus;
import org.musketeers.exception.AuthServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.rabbitmq.model.ActivationGuestModel;
import org.musketeers.rabbitmq.model.RegisterGuestActivationModel;
import org.musketeers.rabbitmq.model.RegisterGuestModel;
import org.musketeers.rabbitmq.model.RegisterSupervisorModel;
import org.musketeers.rabbitmq.producer.MailSenderForGuestProducer;
import org.musketeers.rabbitmq.producer.RegisterGuestActivationProducer;
import org.musketeers.rabbitmq.producer.RegisterGuestProducer;
import org.musketeers.rabbitmq.producer.RegisterSupervisorProducer;
import org.musketeers.repository.IAuthRepository;
import org.musketeers.utility.CodeGenerator;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService extends ServiceManager<Auth, String> {
    private final IAuthRepository repository;
    private final JwtTokenManager tokenManager;
    private final MailSenderForGuestProducer mailSenderForGuestProducer;
    private final RegisterGuestProducer registerGuestProducer;
    private final RegisterSupervisorProducer registerSupervisorProducer;
    private final RegisterGuestActivationProducer registerGuestActivationProducer;

    public AuthService(IAuthRepository repository, JwtTokenManager tokenManager, MailSenderForGuestProducer mailSenderProducerForGuest, RegisterGuestProducer registerGuestProducer, RegisterSupervisorProducer registerSupervisorProducer, RegisterGuestActivationProducer registerGuestActivationProducer) {
        super(repository);
        this.repository = repository;
        this.tokenManager = tokenManager;
        this.mailSenderForGuestProducer = mailSenderProducerForGuest;
        this.registerGuestProducer = registerGuestProducer;
        this.registerSupervisorProducer = registerSupervisorProducer;
        this.registerGuestActivationProducer = registerGuestActivationProducer;
    }

    @Transactional
    public String registerGuest(GuestRegisterRequestDto dto) {
        if (!dto.getPassword().equals(dto.getRePassword())) throw new AuthServiceException(ErrorType.REGISTER_PASSWORD_MISMATCH);
        if (repository.existsByEmail(dto.getEmail())) throw new AuthServiceException(ErrorType.EMAIL_ALREADY_EXISTS);
        if (repository.existsByPhone(dto.getPhone())) throw new AuthServiceException(ErrorType.PHONE_ALREADY_EXISTS);

        Auth auth = Auth.builder()
                        .email(dto.getEmail())
                        .password(dto.getPassword())
                        .phone(dto.getPhone())
                        .activationCode(CodeGenerator.generateCode())
                        .role(ERole.GUEST)
                        .build();
        save(auth);
        RegisterGuestModel registerGuestModel = RegisterGuestModel.builder()
                .authid(auth.getId())
                .name(dto.getName())
                .surName(dto.getSurName())
                .identityNumber(dto.getIdentityNumber())
                .phone(dto.getPhone())
                .dateOfBirth(dto.getDateOfBirth())
                .email(dto.getEmail())
                .gender(dto.getGender().toString())
                .build();
        registerGuestProducer.sendNewGuest(registerGuestModel);

        ActivationGuestModel activationGuestModel = ActivationGuestModel.builder()
                .id(auth.getId())
                .name(registerGuestModel.getName())
                .email(auth.getEmail())
                .build();
        mailSenderForGuestProducer.convertAndSendToRabbit(activationGuestModel);

        return "Successfully registered";
    }
    @Transactional
    public String registerSupervisor(SupervisorRegisterRequestDto dto) {
        if (!dto.getPassword().equals(dto.getRePassword())) throw new AuthServiceException(ErrorType.REGISTER_PASSWORD_MISMATCH);
        if (repository.existsByEmail(dto.getEmail())) throw new AuthServiceException(ErrorType.EMAIL_ALREADY_EXISTS);
        if (repository.existsByPhone(dto.getPhone())) throw new AuthServiceException(ErrorType.PHONE_ALREADY_EXISTS);

        Auth auth = Auth.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phone(dto.getPhone())
                .role(ERole.SUPERVISOR)
                .build();
        save(auth);

        RegisterSupervisorModel registerSupervisorModel = RegisterSupervisorModel.builder()
                .authid(auth.getId())
                .name(dto.getName())
                .surName(dto.getSurName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .companyName(dto.getCompanyName())
                .identityNumber(dto.getIdentityNumber())
                .dateOfBirth(dto.getDateOfBirth())
                .gender(dto.getGender().toString())
                .build();
        registerSupervisorProducer.sendNewSupervisor(registerSupervisorModel);
        return "Successfully registered";
    }

    public void activateSupervisor(Auth auth){
        auth.setStatus(EStatus.ACTIVE);
        update(auth);
    }

    public String activateGuest(String id) {
        Optional<Auth> auth = findById(id);
        if (auth.isEmpty()) throw new AuthServiceException(ErrorType.NOT_FOUND);
        auth.get().setStatus(EStatus.ACTIVE);
        update(auth.get());
        RegisterGuestActivationModel registerGuestActivationModel = RegisterGuestActivationModel.builder()
                .id(auth.get().getId())
                .build();
        registerGuestActivationProducer.changeGuestStatus(registerGuestActivationModel);
        return "updated successfully";
    }
}
