package org.musketeers.service;


import jakarta.transaction.Transactional;
import org.musketeers.dto.request.GuestRegisterRequestDto;
import org.musketeers.entity.Auth;
import org.musketeers.exception.AuthServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.rabbitmq.model.RegisterGuestModel;
import org.musketeers.rabbitmq.producer.RegisterGuestProducer;
import org.musketeers.repository.IAuthRepository;
import org.musketeers.utility.CodeGenerator;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class AuthService extends ServiceManager<Auth, String> {
    private final IAuthRepository repository;
    private final JwtTokenManager tokenManager;
    //private final MailSenderProducer mailSenderProducer;
    private final RegisterGuestProducer registerGuestProducer;

    public AuthService(IAuthRepository repository, JwtTokenManager tokenManager, RegisterGuestProducer registerGuestProducer) {
        super(repository);
        this.repository = repository;
        this.tokenManager = tokenManager;

        this.registerGuestProducer = registerGuestProducer;
    }

    @Transactional
    public String registerGuest(GuestRegisterRequestDto dto) {
        if (!dto.getPassword().equals(dto.getRePassword())) throw new AuthServiceException(ErrorType.REGISTER_PASSWORD_MISMATCH);
        if (repository.existsByEmail(dto.getEmail())) throw new AuthServiceException(ErrorType.EMAIL_ALREADY_EXISTS);

        Auth auth = Auth.builder()
                        .email(dto.getEmail())
                        .password(dto.getPassword())
                        .phone(dto.getPhone())
                        .activationCode(CodeGenerator.generateCode())
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

        //mailSenderProducer.convertAndSendToRabbit(IAuthMapper.INSTANCE.authToMailSenderGuestModel(auth));  -- Yarın bakıcam !!!

        return "Successfully registered";
    }
}
