package org.musketeers.service;


import jakarta.transaction.Transactional;
import org.musketeers.dto.request.GuestRegisterRequestDto;
import org.musketeers.entity.Auth;
import org.musketeers.exception.AuthServiceException;
import org.musketeers.exception.ErrorType;
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

    public AuthService(IAuthRepository repository, JwtTokenManager tokenManager) {
        super(repository);
        this.repository = repository;
        this.tokenManager = tokenManager;

    }

    @Transactional
    public String registerGuest(GuestRegisterRequestDto dto) {
        if (!dto.getPassword().equals(dto.getRePassword())) throw new AuthServiceException(ErrorType.REGISTER_PASSWORD_MISMATCH);
        if (repository.existsByEmail(dto.getEmail())) throw new AuthServiceException(ErrorType.EMAIL_ALREADY_EXISTS);

        Auth auth = Auth.builder()
                        .email(dto.getEmail())
                        .password(dto.getPassword())
                        .phone(dto.getPhone())
                        .gender(dto.getGender())
                        .activationCode(CodeGenerator.generateCode())
                        .build();
        save(auth);
        //mailSenderProducer.convertAndSendToRabbit(IAuthMapper.INSTANCE.authToMailSenderGuestModel(auth));  -- Yarın bakıcam !!!

        return "Successfully registered";
    }
}
