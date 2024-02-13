package org.musketeers.service;

import lombok.RequiredArgsConstructor;
import org.musketeers.repository.SupervisorIdRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupervisorIdService {
    private final SupervisorIdRepository supervisorIdRepository;
}
