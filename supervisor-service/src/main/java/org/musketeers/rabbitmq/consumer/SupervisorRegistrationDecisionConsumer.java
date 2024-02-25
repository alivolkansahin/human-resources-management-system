package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.SupervisorRegistrationDecisionModel;
import org.musketeers.service.SupervisorService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupervisorRegistrationDecisionConsumer {

    private final SupervisorService supervisorService;

    @RabbitListener(queues = "${supervisor-service-config.rabbitmq.registration-decision-supervisor-queue}")
    public void handleAdminDecisionForSupervisorRegistration(SupervisorRegistrationDecisionModel model) {
        supervisorService.handleAdminDecisionForSupervisorRegistration(model);
    }

}
