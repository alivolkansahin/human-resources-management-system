package org.musketeers.rabbitmq.producer;


import lombok.RequiredArgsConstructor;
import org.musketeers.exception.ErrorType;
import org.musketeers.exception.PersonnelServiceException;
import org.musketeers.rabbitmq.model.CreatePersonnelAuthModel;
import org.musketeers.rabbitmq.model.CreatePersonnelCompanyModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePersonnelProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${personnel-service-config.rabbitmq.create-personnel-exchange}")
    private String directExchange;

    @Value("${personnel-service-config.rabbitmq.create-personnel-auth-binding-key}")
    private String authBindingKey;

    @Value("${personnel-service-config.rabbitmq.create-personnel-company-binding-key}")
    private String companyBindingKey;

    @Value("${personnel-service-config.rabbitmq.expiration-timeout}")
    private String expirationTimeout;

    public String sendPersonnelInfoToAuth(CreatePersonnelAuthModel model){
        Object response = rabbitTemplate.convertSendAndReceive(directExchange, authBindingKey, model, message -> {
            message.getMessageProperties().setExpiration(expirationTimeout);
            return message;
        });
        if(response != null) {
            return (String) response;
        } else {
            throw new PersonnelServiceException(ErrorType.SERVICE_NOT_RESPONDING);
        }
    }

    public void sendPersonnelInfoToCompany(CreatePersonnelCompanyModel model){
        rabbitTemplate.convertAndSend(directExchange, companyBindingKey, model);
    }

}
