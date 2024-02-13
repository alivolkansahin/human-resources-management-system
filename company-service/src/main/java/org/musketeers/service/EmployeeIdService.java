package org.musketeers.service;

import lombok.RequiredArgsConstructor;
import org.musketeers.repository.EmployeeIdRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeIdService {
    private final EmployeeIdRepository employeeIdRepository;
}
