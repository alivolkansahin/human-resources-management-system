package org.musketeers.service;


import jakarta.transaction.Transactional;
import org.musketeers.dto.request.GuestRegisterRequestDto;
import org.musketeers.dto.request.LoginRequestDto;
import org.musketeers.dto.request.SupervisorRegisterRequestDto;
import org.musketeers.dto.response.GetAllActiveResponse;
import org.musketeers.dto.response.LoginResponseDto;
import org.musketeers.entity.Auth;
import org.musketeers.entity.enums.ERole;
import org.musketeers.entity.enums.EStatus;
import org.musketeers.exception.AuthServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.rabbitmq.model.*;
import org.musketeers.rabbitmq.producer.*;
import org.musketeers.repository.IAuthRepository;
import org.musketeers.utility.CodeGenerator;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService extends ServiceManager<Auth, String> {
    private final IAuthRepository repository;
    private final JwtTokenManager tokenManager;
    private final MailSenderForGuestProducer mailSenderForGuestProducer;
    private final RegisterGuestProducer registerGuestProducer;
    private final RegisterSupervisorProducer registerSupervisorProducer;
    private final RegisterGuestActivationProducer registerGuestActivationProducer;

    private final PersonnelMailSendProducer personnelMailSendProducer;

    public AuthService(IAuthRepository repository, JwtTokenManager tokenManager, MailSenderForGuestProducer mailSenderProducerForGuest, RegisterGuestProducer registerGuestProducer, RegisterSupervisorProducer registerSupervisorProducer, RegisterGuestActivationProducer registerGuestActivationProducer, PersonnelMailSendProducer personnelMailSendProducer) {
        super(repository);
        this.repository = repository;
        this.tokenManager = tokenManager;
        this.mailSenderForGuestProducer = mailSenderProducerForGuest;
        this.registerGuestProducer = registerGuestProducer;
        this.registerSupervisorProducer = registerSupervisorProducer;
        this.registerGuestActivationProducer = registerGuestActivationProducer;
        this.personnelMailSendProducer = personnelMailSendProducer;
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
                .gender(dto.getGender())
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
                .gender(dto.getGender())
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
        Path activationSuccessHTMLPath = Paths.get("H:\\Program Files\\PROJECTS\\human-resources-management-system\\auth-service\\src\\main\\resources\\templates\\activation-success-page.html");
        byte[] successfulPage = null;
        try {
            successfulPage = Files.readAllBytes(activationSuccessHTMLPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(successfulPage);
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        Auth auth = repository.findOptionalByEmailOrPhone(dto.getIdentity(), dto.getIdentity()).orElseThrow(() -> new AuthServiceException(ErrorType.NOT_FOUND));
        if (auth.getState().equals(false)) throw new AuthServiceException(ErrorType.ACCOUNT_DELETED);
        if (!auth.getPassword().equals(dto.getPassword())) throw new AuthServiceException(ErrorType.INVALID_PASSWORD_OR_EMAIL);
        if (!auth.getStatus().equals(EStatus.ACTIVE)) throw new AuthServiceException(ErrorType.ACCOUNT_NOT_ACTIVE);

        String token = tokenManager.createToken(auth.getId(), auth.getRole()).orElseThrow(() -> new AuthServiceException(ErrorType.TOKEN_NOT_CREATED));

        return LoginResponseDto.builder().token(token).role(auth.getRole().toString()).build();
    }

    public String registerPersonnel(CreatePersonnelAuthModel model) {
        String code = model.getName() + "5" + CodeGenerator.generateCode();

        Auth auth = Auth.builder()
                .email(model.getEmail())
                .phone(model.getPhone())
                .role(ERole.PERSONNEL)
                .status(EStatus.ACTIVE)
                .password(code)
                .build();
        save(auth);

        CreatePersonnelMailModel mailModel = CreatePersonnelMailModel.builder()
                .email(auth.getEmail())
                .password(auth.getPassword())
                .name(model.getName())
                .build();

        personnelMailSendProducer.sendMail(mailModel);

        return auth.getId();

    }

    public List<GetAllActiveResponse> getAllActive() {
        return findAll().stream()
                .filter(auth -> auth.getStatus().equals(EStatus.ACTIVE))
                .map(auth -> GetAllActiveResponse.builder()
                        .email(auth.getEmail())
                        .phone(auth.getPhone())
                        .password(auth.getPassword())
                        .role(auth.getRole().toString())
                        .build())
                .toList();
    }
}
