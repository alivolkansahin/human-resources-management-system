package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.SendAdvanceStatusChangeNotificationModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendAdvanceStatusChangeNotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${advance-service-config.rabbitmq.advance-exchange}")
    private String directExchange;

    @Value("${advance-service-config.rabbitmq.send-advance-status-change-notification-to-personnel-service-binding-key}")
    private String bindingKey;

    public void sendNotificationToPersonnelService(SendAdvanceStatusChangeNotificationModel model) {
        rabbitTemplate.convertAndSend(directExchange, bindingKey, model);
    }

}
