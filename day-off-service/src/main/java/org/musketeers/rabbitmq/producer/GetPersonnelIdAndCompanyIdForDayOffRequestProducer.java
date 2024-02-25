package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.DayOffServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.rabbitmq.model.GetPersonnelIdAndCompanyIdForDayOffRequestModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetPersonnelIdAndCompanyIdForDayOffRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${day-off-service-config.rabbitmq.day-off-exchange}")
    private String directExchange;

    @Value("${day-off-service-config.rabbitmq.get-personnel-id-and-company-id-for-day-off-request-binding-key}")
    private String bindingKey;

    @Value("${day-off-service-config.rabbitmq.expiration-timeout}")
    private String expirationTimeout;

    public GetPersonnelIdAndCompanyIdForDayOffRequestModel getPersonnelIdAndCompanyIdFromPersonnelService(String authId) {
        Object response = rabbitTemplate.convertSendAndReceive(directExchange, bindingKey, authId, message -> {
            message.getMessageProperties().setExpiration(expirationTimeout);
            return message;
        });
        if(response != null) {
            return (GetPersonnelIdAndCompanyIdForDayOffRequestModel) response;
        } else {
            throw new DayOffServiceException(ErrorType.SERVICE_NOT_RESPONDING);
        }
    }

}
