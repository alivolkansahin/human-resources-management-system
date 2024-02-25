package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.SendDayOffStatusChangeNotificationModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendDayOffStatusChangeNotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${day-off-service-config.rabbitmq.day-off-exchange}")
    private String directExchange;

    @Value("${day-off-service-config.rabbitmq.send-day-off-status-change-notification-to-personnel-service-binding-key}")
    private String bindingKey;

    public void sendNotificationToPersonnelService(SendDayOffStatusChangeNotificationModel model) {
        rabbitTemplate.convertAndSend(directExchange, bindingKey, model);
    }

}
