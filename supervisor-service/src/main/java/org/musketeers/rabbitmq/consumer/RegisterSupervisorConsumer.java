package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.RegisterSupervisorModel;
import org.musketeers.service.SupervisorService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterSupervisorConsumer {

    private final SupervisorService supervisorService;

    @RabbitListener(queues = "${supervisor-service-config.rabbitmq.register-supervisor-queue}")
    public void createSupervisorFromQueue(RegisterSupervisorModel model) {
        supervisorService.createSupervisor(model);
    }

}
