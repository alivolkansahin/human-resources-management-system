package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.AuthServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.rabbitmq.model.CompanyStatusCheckRequestModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyStatusCheckRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.auth-exchange}")
    private String exchange;

    @Value("${rabbitmq.company-status-check-bindingKey}")
    private String bindingKey;

    @Value("${rabbitmq.expiration-timeout}")
    private String expirationTimeout;

    public Boolean isCompanyContractActive(CompanyStatusCheckRequestModel model) {
        Object response = rabbitTemplate.convertSendAndReceive(exchange, bindingKey, model, message -> {
            message.getMessageProperties().setExpiration(expirationTimeout);
            return message;
        });

        if(response != null) {
            return (Boolean) response;
        } else {
            throw new AuthServiceException(ErrorType.SERVICE_NOT_RESPONDING);
        }
    }
}
