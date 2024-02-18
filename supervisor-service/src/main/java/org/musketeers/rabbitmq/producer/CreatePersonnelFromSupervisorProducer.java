package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.CreatePersonnelFromSupervisorModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePersonnelFromSupervisorProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${supervisor-service-config.rabbitmq.supervisor-activation-exchange}")
    private String exchange;

    @Value("${supervisor-service-config.rabbitmq.create-personnel-from-supervisor-binding-key}")
    private String bindingKey;

    public void createPersonnelFromSupervisor(CreatePersonnelFromSupervisorModel model) {
        rabbitTemplate.convertAndSend(exchange, bindingKey, model);
    }

}
