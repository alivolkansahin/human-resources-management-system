package org.musketeers.service;

import org.musketeers.entity.Personnel;
import org.musketeers.repository.PersonnelRepository;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonnelService extends ServiceManager<Personnel, String> {

    private final PersonnelRepository personnelRepository;

    public PersonnelService(PersonnelRepository personnelRepository) {
        super(personnelRepository);
        this.personnelRepository = personnelRepository;
    }

    public Personnel register(Personnel personnel) {
        return save(personnel);
    }

    public Personnel getPersonnelById(String id) {
        return findById(id);
    }

    public List<Personnel> getAllPersonnel() {
        return findAll();
    }

    public Boolean softDeletePersonnelById(String id) {
        return softDeleteById(id);
    }
}
