package org.musketeers.rabbitmq.producer;

import org.musketeers.rabbitmq.model.RegisterSupervisorModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RegisterSupervisorProducer {
    private final RabbitTemplate rabbitTemplate;

    public RegisterSupervisorProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    @Value("${rabbitmq.auth-exchange}")
    private String exchange;
    @Value("${rabbitmq.register-supervisor-bindingKey}")
    private String registerSupervisorBindingKey;

    public void sendNewSupervisor(RegisterSupervisorModel registerSupervisorModel){
        rabbitTemplate.convertAndSend(exchange,registerSupervisorBindingKey,registerSupervisorModel);
    }
}
