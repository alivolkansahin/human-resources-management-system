package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.AdvanceServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.rabbitmq.model.GetPersonnelIdAndCompanyIdForAdvanceRequestModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetPersonnelIdAndCompanyIdForAdvanceRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${advance-service-config.rabbitmq.advance-exchange}")
    private String directExchange;

    @Value("${advance-service-config.rabbitmq.get-personnel-id-and-company-id-for-advance-request-binding-key}")
    private String bindingKey;

    @Value("${advance-service-config.rabbitmq.expiration-timeout}")
    private String expirationTimeout;

    public GetPersonnelIdAndCompanyIdForAdvanceRequestModel getPersonnelIdAndCompanyIdFromPersonnelService(String authId) {
        Object response = rabbitTemplate.convertSendAndReceive(directExchange, bindingKey, authId, message -> {
            message.getMessageProperties().setExpiration(expirationTimeout);
            return message;
        });
        if(response != null) {
            return (GetPersonnelIdAndCompanyIdForAdvanceRequestModel) response;
        } else {
            throw new AdvanceServiceException(ErrorType.SERVICE_NOT_RESPONDING);
        }
    }

}
