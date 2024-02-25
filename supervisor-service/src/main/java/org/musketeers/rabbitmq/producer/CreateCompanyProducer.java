package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.ErrorType;
import org.musketeers.exception.SupervisorServiceException;
import org.musketeers.rabbitmq.model.CreateCompanyRequestModel;
import org.musketeers.rabbitmq.model.CreateCompanyResponseModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCompanyProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${supervisor-service-config.rabbitmq.supervisor-activation-exchange}")
    private String directExchange;

    @Value("${supervisor-service-config.rabbitmq.supervisor-activation-company-binding-key}")
    private String bindingKey;

    @Value("${supervisor-service-config.rabbitmq.expiration-timeout}")
    private String expirationTimeout;

    public CreateCompanyResponseModel createCompanyAndReturn(CreateCompanyRequestModel model){
        Object response = rabbitTemplate.convertSendAndReceive(directExchange,bindingKey,model, message -> {
            message.getMessageProperties().setExpiration(expirationTimeout);
            return message;
        });
        if(response != null) {
            return (CreateCompanyResponseModel) response;
        } else {
            throw new SupervisorServiceException(ErrorType.SERVICE_NOT_RESPONDING);
        }
    }

}
