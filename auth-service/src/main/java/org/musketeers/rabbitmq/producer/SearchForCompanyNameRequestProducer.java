package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.AuthServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.rabbitmq.model.CompanyStatusCheckRequestModel;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchForCompanyNameRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.auth-exchange}")
    private String exchange;

    @Value("${rabbitmq.search-for-company-name-bindingKey}")
    private String bindingKey;

    @Value("${rabbitmq.expiration-timeout}")
    private String expirationTimeout;

    public Boolean searchForCompanyName(String companyName) {
            Object response = rabbitTemplate.convertSendAndReceive(exchange, bindingKey, companyName, message -> {
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
