package org.musketeers.service;

import lombok.RequiredArgsConstructor;
import org.musketeers.repository.PersonnelRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonnelService {
    private final PersonnelRepository personnelRepository;
}
