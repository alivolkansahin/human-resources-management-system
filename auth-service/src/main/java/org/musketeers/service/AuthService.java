package org.musketeers.service;


import org.musketeers.entity.Auth;
import org.musketeers.repository.IAuthRepository;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService extends ServiceManager<Auth, UUID> {
    private final IAuthRepository repository;
    private final JwtTokenManager tokenManager;

    public AuthService(IAuthRepository repository, JwtTokenManager tokenManager) {
        super(repository);
        this.repository = repository;
        this.tokenManager = tokenManager;
    }

    public Auth registerDeneme(Auth auth) {
        return save(auth);
    }
}
