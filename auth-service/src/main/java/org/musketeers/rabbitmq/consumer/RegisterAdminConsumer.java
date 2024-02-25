package org.musketeers.rabbitmq.consumer;

import org.musketeers.exception.AuthServiceException;
import org.musketeers.rabbitmq.model.RegisterAdminModel;
import org.musketeers.service.AuthService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RegisterAdminConsumer {

    private final AuthService authService;


    public RegisterAdminConsumer(AuthService authService) {
        this.authService = authService;
    }

    @RabbitListener(queues = "${rabbitmq.admin-register-auth-queue}")
    public String saveAdmin(RegisterAdminModel model) {
        try {
            return authService.registerAdmin(model);
        } catch (AuthServiceException ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }


}
