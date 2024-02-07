package org.musketeers.service;

import org.musketeers.entity.Supervisor;
import org.musketeers.repository.SupervisorRepository;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupervisorService extends ServiceManager<Supervisor, String> {

    private final SupervisorRepository supervisorRepository;

    public SupervisorService(SupervisorRepository supervisorRepository) {
        super(supervisorRepository);
        this.supervisorRepository = supervisorRepository;
    }

    public Supervisor register(Supervisor supervisor) {
        return save(supervisor);
    }

    public Supervisor getSupervisorById(String id) {
        return findById(id);
    }

    public List<Supervisor> getAllSupervisors() {
        return findAll();
    }

    public Boolean softDeleteSupervisorById(String id) {
        return softDeleteById(id);
    }
}
