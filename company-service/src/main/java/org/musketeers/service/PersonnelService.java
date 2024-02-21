package org.musketeers.service;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.repository.PersonnelRepository;
import org.musketeers.repository.entity.Personnel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonnelService {
    private final PersonnelRepository personnelRepository;

    public Personnel findByPersonnelId(String personnelId) {
        return personnelRepository.findByPersonnelId(personnelId).orElseThrow(() -> new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND)); // personnel not found
    }
}
