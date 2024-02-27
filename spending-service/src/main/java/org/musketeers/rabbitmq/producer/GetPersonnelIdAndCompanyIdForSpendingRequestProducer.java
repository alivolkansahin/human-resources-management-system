package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.ErrorType;
import org.musketeers.exception.SpendingServiceException;
import org.musketeers.rabbitmq.model.GetPersonnelIdAndCompanyIdForSpendingRequestModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetPersonnelIdAndCompanyIdForSpendingRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spending-service-config.rabbitmq.spending-exchange}")
    private String directExchange;

    @Value("${spending-service-config.rabbitmq.get-personnel-id-and-company-id-for-spending-request-binding-key}")
    private String bindingKey;

    @Value("${spending-service-config.rabbitmq.expiration-timeout}")
    private String expirationTimeout;

    public GetPersonnelIdAndCompanyIdForSpendingRequestModel getPersonnelIdAndCompanyIdFromPersonnelService(String authId) {
        Object response = rabbitTemplate.convertSendAndReceive(directExchange, bindingKey, authId, message -> {
            message.getMessageProperties().setExpiration(expirationTimeout);
            return message;
        });
        if(response != null) {
            return (GetPersonnelIdAndCompanyIdForSpendingRequestModel) response;
        } else {
            throw new SpendingServiceException(ErrorType.SERVICE_NOT_RESPONDING);
        }
    }

}
