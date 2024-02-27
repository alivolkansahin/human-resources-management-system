package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.SendSpendingStatusChangeMailModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendSpendingStatusChangeMailProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${personnel-service-config.rabbitmq.create-personnel-exchange}")
    private String directExchange;

    @Value("${personnel-service-config.rabbitmq.send-spending-status-change-notification-to-mail-service-binding-key}")
    private String bindingKey;

    public void sendMailToPersonnel(SendSpendingStatusChangeMailModel model) {
        rabbitTemplate.convertAndSend(directExchange, bindingKey, model);
    }

}
