package org.musketeers.rabbitmq.consumer;

import org.musketeers.entity.Auth;
import org.musketeers.entity.enums.EStatus;
import org.musketeers.exception.AuthServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.rabbitmq.model.SupervisorRegistrationDecisionModel;
import org.musketeers.service.AuthService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SupervisorRegistrationDecisionConsumer {
    private final AuthService authService;

    public SupervisorRegistrationDecisionConsumer(AuthService authService) {
        this.authService = authService;
    }
    @RabbitListener(queues = "${rabbitmq.registration-decision-auth-queue}")
    public void handleAdminDecisionForSupervisorRegistration(SupervisorRegistrationDecisionModel model) {
        authService.handleAdminDecisionForSupervisorRegistration(model);
    }
}
