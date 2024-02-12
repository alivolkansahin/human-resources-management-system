package org.musketeers.rabbitmq.producer;

import org.musketeers.rabbitmq.model.CreatePersonnelAuthModel;
import org.musketeers.rabbitmq.model.CreatePersonnelMailModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PersonnelMailSendProducer {
    private final RabbitTemplate rabbitTemplate;

    public PersonnelMailSendProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${rabbitmq.mail-bindingKey-personnel}")
    private String mailPersonnelBindingKey;

    @Value("${rabbitmq.auth-exchange}")
    private String exchange;

    public void sendMail(CreatePersonnelMailModel model){
        rabbitTemplate.convertAndSend(exchange,mailPersonnelBindingKey,model);
    }
}
