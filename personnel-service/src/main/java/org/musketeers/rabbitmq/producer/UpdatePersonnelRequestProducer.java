package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.ErrorType;
import org.musketeers.exception.PersonnelServiceException;
import org.musketeers.rabbitmq.model.UpdatePersonnelRequestModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdatePersonnelRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${personnel-service-config.rabbitmq.create-personnel-exchange}")
    private String directExchange;

    @Value("${personnel-service-config.rabbitmq.update-personnel-by-id-auth-binding-key}")
    private String bindingKey;

    @Value("${personnel-service-config.rabbitmq.expiration-timeout}")
    private String expirationTimeout;

    public Boolean updateAuthService(UpdatePersonnelRequestModel model) {
        Object response = rabbitTemplate.convertSendAndReceive(directExchange, bindingKey, model, message -> {
            message.getMessageProperties().setExpiration(expirationTimeout);
            return message;
        });
        if(response != null) {
            return (Boolean) response;
        } else {
            throw new PersonnelServiceException(ErrorType.SERVICE_NOT_RESPONDING);
        }
    }

}
