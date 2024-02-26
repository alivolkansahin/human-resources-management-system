package org.musketeers.service;

import org.musketeers.entity.Spending;
import org.musketeers.repository.SpendingRepository;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class SpendingService extends ServiceManager<Spending, String> {

    private final SpendingRepository spendingRepository;

    private final JwtTokenManager jwtTokenManager;

    public SpendingService(SpendingRepository spendingRepository, JwtTokenManager jwtTokenManager) {
        super(spendingRepository);
        this.spendingRepository = spendingRepository;
        this.jwtTokenManager = jwtTokenManager;
    }


}
