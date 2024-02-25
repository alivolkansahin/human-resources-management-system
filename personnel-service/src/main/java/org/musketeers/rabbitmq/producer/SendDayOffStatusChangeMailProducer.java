package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.ErrorType;
import org.musketeers.exception.PersonnelServiceException;
import org.musketeers.rabbitmq.model.GetCompanyIdFromSupervisorTokenModel;
import org.musketeers.rabbitmq.model.SendDayOffStatusChangeMailModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendDayOffStatusChangeMailProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${personnel-service-config.rabbitmq.create-personnel-exchange}")
    private String directExchange;

    @Value("${personnel-service-config.rabbitmq.send-day-off-status-change-notification-to-mail-service-binding-key}")
    private String bindingKey;

    public void sendMailToPersonnel(SendDayOffStatusChangeMailModel model) {
        rabbitTemplate.convertAndSend(directExchange, bindingKey, model);
    }
}
