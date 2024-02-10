package org.musketeers.service;

import org.musketeers.entity.Supervisor;
import org.musketeers.entity.enums.ActivationStatus;
import org.musketeers.exception.ErrorType;
import org.musketeers.exception.SupervisorServiceException;
import org.musketeers.rabbitmq.model.CreateCompanyRequestModel;
import org.musketeers.rabbitmq.model.CreateCompanyResponseModel;
import org.musketeers.rabbitmq.producer.CreateCompanyProducer;
import org.musketeers.repository.SupervisorRepository;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupervisorService extends ServiceManager<Supervisor, String> {

    private final SupervisorRepository supervisorRepository;

    private final JwtTokenManager jwtTokenManager;

    private final CreateCompanyProducer createCompanyProducer;

    public SupervisorService(SupervisorRepository supervisorRepository, JwtTokenManager jwtTokenManager, CreateCompanyProducer createCompanyProducer) {
        super(supervisorRepository);
        this.supervisorRepository = supervisorRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.createCompanyProducer = createCompanyProducer;
    }

    public Supervisor register(Supervisor supervisor) {
        return save(supervisor);
    }

    public Supervisor getSupervisorById(String id) {
//        jwtTokenManager.getClaimsFromToken(id).get(0)
        return findById(id);
    }

    public Supervisor getSupervisorByAuthId(String authId) {
        return supervisorRepository.findOptionalByAuthId(authId).orElseThrow(() -> new SupervisorServiceException(ErrorType.SUPERVISOR_NOT_FOUND));
    }

    public List<Supervisor> getAllSupervisors() {
        return findAll();
    }

    public Boolean softDeleteSupervisorById(String id) {
        return softDeleteById(id);
    }

    public void activate(Supervisor supervisor) {
        CreateCompanyRequestModel model = CreateCompanyRequestModel.builder().supervisorId(supervisor.getId()).companyName(supervisor.getCompanyName()).build();
        CreateCompanyResponseModel responseModel = createCompanyProducer.createCompanyAndReturn(model);
        supervisor.setCompanyId(responseModel.getCompanyId());
        // belki supervisorda companyName fieldı silinebilir artık ??? supervisor.setCompanyName(null);
        supervisor.setActivationStatus(ActivationStatus.ACTIVATED);
        update(supervisor);
    }

}
