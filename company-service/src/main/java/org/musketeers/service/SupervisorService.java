package org.musketeers.service;

import lombok.RequiredArgsConstructor;
import org.musketeers.repository.SupervisorRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupervisorService {
    private final SupervisorRepository supervisorRepository;
}
