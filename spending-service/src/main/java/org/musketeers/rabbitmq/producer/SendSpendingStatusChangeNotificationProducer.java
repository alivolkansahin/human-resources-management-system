package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.SendSpendingStatusChangeNotificationModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendSpendingStatusChangeNotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spending-service-config.rabbitmq.spending-exchange}")
    private String directExchange;

    @Value("${spending-service-config.rabbitmq.send-spending-status-change-notification-to-personnel-service-binding-key}")
    private String bindingKey;

    public void sendNotificationToPersonnelService(SendSpendingStatusChangeNotificationModel model) {
        rabbitTemplate.convertAndSend(directExchange, bindingKey, model);
    }

}
