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
public class GetCompanyIdForLoginRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.auth-exchange}")
    private String exchange;

    @Value("${rabbitmq.get-company-id-for-user-login-bindingKey}")
    private String bindingKey;

    @Value("${rabbitmq.expiration-timeout}")
    private String expirationTimeout;

    public String getCompanyIdFromUser(String authId) {
        Object response = rabbitTemplate.convertSendAndReceive(exchange, bindingKey, authId, message -> {
            message.getMessageProperties().setExpiration(expirationTimeout);
            return message;
        });

        if(response != null) {
            return (String) response;
        } else {
            throw new AuthServiceException(ErrorType.SERVICE_NOT_RESPONDING);
        }
    }


}
