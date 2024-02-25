package org.musketeers.rabbitmq.producer;


import lombok.RequiredArgsConstructor;
import org.musketeers.exception.ErrorType;
import org.musketeers.exception.PersonnelServiceException;
import org.musketeers.rabbitmq.model.GetCompanyDetailsByPersonnelResponseModel;
import org.musketeers.rabbitmq.model.GetCompanyIdFromSupervisorTokenModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCompanyIdFromSupervisorTokenProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${personnel-service-config.rabbitmq.create-personnel-exchange}")
    private String directExchange;

    @Value("${personnel-service-config.rabbitmq.get-company-id-supervisor-binding-key}")
    private String supervisorBindingKey;

    @Value("${personnel-service-config.rabbitmq.expiration-timeout}")
    private String expirationTimeout;

    public String getCompanyIdFromSupervisorToken(GetCompanyIdFromSupervisorTokenModel model) {
        Object response = rabbitTemplate.convertSendAndReceive(directExchange, supervisorBindingKey, model, message -> {
            message.getMessageProperties().setExpiration(expirationTimeout);
            return message;
        });
        if(response != null) {
            return (String) response;
        } else {
            throw new PersonnelServiceException(ErrorType.SERVICE_NOT_RESPONDING);
        }
    }

}
