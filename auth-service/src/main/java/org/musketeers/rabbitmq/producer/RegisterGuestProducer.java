package org.musketeers.rabbitmq.producer;

import org.musketeers.rabbitmq.model.RegisterGuestModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RegisterGuestProducer {
    private final RabbitTemplate rabbitTemplate;

    public RegisterGuestProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${rabbitmq.auth-exchange}")
    private String exchange;

    @Value("${rabbitmq.register-guest-bindingKey}")
    private String registerGuestBindingKey;

    public void sendNewGuest(RegisterGuestModel registerGuestModel){
        rabbitTemplate.convertAndSend(exchange,registerGuestBindingKey,registerGuestModel);
    }
}
