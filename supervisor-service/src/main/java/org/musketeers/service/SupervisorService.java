package org.musketeers.service;

import org.musketeers.entity.Supervisor;
import org.musketeers.repository.SupervisorRepository;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupervisorService extends ServiceManager<Supervisor, String> {

    private final SupervisorRepository supervisorRepository;

    private final JwtTokenManager jwtTokenManager;

    public SupervisorService(SupervisorRepository supervisorRepository, JwtTokenManager jwtTokenManager) {
        super(supervisorRepository);
        this.supervisorRepository = supervisorRepository;
        this.jwtTokenManager = jwtTokenManager;
    }

    public Supervisor register(Supervisor supervisor) {
        return save(supervisor);
    }

    public Supervisor getSupervisorById(String id) {
//        jwtTokenManager.getClaimsFromToken(id).get(0)
        return findById(id);
    }

    public List<Supervisor> getAllSupervisors() {
        return findAll();
    }

    public Boolean softDeleteSupervisorById(String id) {
        return softDeleteById(id);
    }
}
