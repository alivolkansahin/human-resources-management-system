package org.musketeers.rabbitmq.consumer;

import org.musketeers.rabbitmq.model.CreatePersonnelAuthModel;
import org.musketeers.service.AuthService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class CreatePersonnelConsumer {
    private final AuthService authService;

    public CreatePersonnelConsumer(AuthService authService) {
        this.authService = authService;
    }
    @RabbitListener(queues = "${rabbitmq.create-personnel-auth-queue}")
    public String registerPersonnel(CreatePersonnelAuthModel model) {
       return authService.registerPersonnel(model);
    }
}
