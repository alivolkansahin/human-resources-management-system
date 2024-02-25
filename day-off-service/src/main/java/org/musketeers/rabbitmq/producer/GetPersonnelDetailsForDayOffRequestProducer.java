package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.DayOffServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.rabbitmq.model.GetPersonnelDetailsForDayOffRequestModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPersonnelDetailsForDayOffRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${day-off-service-config.rabbitmq.day-off-exchange}")
    private String directExchange;

    @Value("${day-off-service-config.rabbitmq.get-personnel-details-for-day-off-request-binding-key}")
    private String bindingKey;

    @Value("${day-off-service-config.rabbitmq.expiration-timeout}")
    private String expirationTimeout;

    public List<GetPersonnelDetailsForDayOffRequestModel> getPersonnelDetailsFromPersonnelService(List<String> personnelIds) {
        Object response = rabbitTemplate.convertSendAndReceive(directExchange, bindingKey, personnelIds, message -> {
            message.getMessageProperties().setExpiration(expirationTimeout);
            return message;
        });
        if(response != null) {
            return (List<GetPersonnelDetailsForDayOffRequestModel>) response;
        } else {
            throw new DayOffServiceException(ErrorType.SERVICE_NOT_RESPONDING);
        }
    }


}
