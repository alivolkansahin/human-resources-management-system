package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.ActivationGuestModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivationGuestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${guest-service-config.rabbitmq.activation-guest-exchange}")
    private String directExchange;

    @Value("${guest-service-config.rabbitmq.activation-guest-binding-key}")
    private String bindingKey;

    public void sendActivate(ActivationGuestModel model) {
        rabbitTemplate.convertAndSend(directExchange, bindingKey, model);
    }



}
