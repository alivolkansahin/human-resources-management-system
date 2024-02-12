package org.musketeers.rabbitmq.producer;

import org.musketeers.rabbitmq.model.RegisterGuestActivationModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RegisterGuestActivationProducer {
    private final RabbitTemplate rabbitTemplate;

    public RegisterGuestActivationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    @Value("${rabbitmq.auth-exchange}")
    private String exchange;
    
    @Value("${rabbitmq.register-guest-activation-bindingKey}")
    private String registerGuestActivationBindingKey;
    
    public void changeGuestStatus(RegisterGuestActivationModel registerGuestActivationModel){
        rabbitTemplate.convertAndSend(exchange,registerGuestActivationBindingKey,registerGuestActivationModel);
    }
}
