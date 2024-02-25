package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.exception.ErrorType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCompanyIdFromSupervisorProducer {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange-name}")
    private String exchange;

    @Value("${rabbitmq.get-company-id-from-supervisor-binding-key}")
    private String getCompanyIdFromSupervisorBindingKey;

    @Value("${rabbitmq.expiration-timeout}")
    private String expirationTimeout;

    public String getCompanyIdFromSupervisor(String authId){
        Object response = rabbitTemplate.convertSendAndReceive(exchange, getCompanyIdFromSupervisorBindingKey, authId, message -> {
            message.getMessageProperties().setExpiration(expirationTimeout);
            return message;
        });
        if(response != null) {
            return (String) response;
        } else {
            throw new CompanyServiceException(ErrorType.SERVICE_NOT_RESPONDING);
        }
    }
}
