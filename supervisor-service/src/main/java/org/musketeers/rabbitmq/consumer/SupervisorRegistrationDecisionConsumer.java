package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.entity.Phone;
import org.musketeers.entity.Supervisor;
import org.musketeers.entity.enums.ActivationStatus;
import org.musketeers.entity.enums.Gender;
import org.musketeers.entity.enums.PhoneType;
import org.musketeers.exception.ErrorType;
import org.musketeers.exception.SupervisorServiceException;
import org.musketeers.rabbitmq.model.RegisterSupervisorModel;
import org.musketeers.rabbitmq.model.SupervisorRegistrationDecisionModel;
import org.musketeers.service.SupervisorService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupervisorRegistrationDecisionConsumer {

    private final SupervisorService supervisorService;

    @RabbitListener(queues = "${supervisor-service-config.rabbitmq.registration-decision-supervisor-queue}")
    public void handleAdminDecisionForSupervisorRegistration(SupervisorRegistrationDecisionModel model) {
        Supervisor supervisor = supervisorService.getSupervisorByAuthId(model.getSupervisorAuthId());
        if(supervisor.getActivationStatus().equals(ActivationStatus.ACTIVATED)) return; // already activated exception maybe...
        if(!model.getDecision()) {
            supervisorService.hardDelete(supervisor);
        } else {
            supervisor.setActivationStatus(ActivationStatus.ACTIVATED);
            supervisorService.update(supervisor);
        }
    }



}
