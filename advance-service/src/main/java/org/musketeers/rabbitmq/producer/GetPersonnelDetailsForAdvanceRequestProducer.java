package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.AdvanceServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.rabbitmq.model.GetPersonnelDetailsForAdvanceRequestModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPersonnelDetailsForAdvanceRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${advance-service-config.rabbitmq.advance-exchange}")
    private String directExchange;

    @Value("${advance-service-config.rabbitmq.get-personnel-details-for-advance-request-binding-key}")
    private String bindingKey;

    @Value("${advance-service-config.rabbitmq.expiration-timeout}")
    private String expirationTimeout;

    public List<GetPersonnelDetailsForAdvanceRequestModel> getPersonnelDetailsFromPersonnelService(List<String> personnelIds) {
        Object response = rabbitTemplate.convertSendAndReceive(directExchange, bindingKey, personnelIds, message -> {
            message.getMessageProperties().setExpiration(expirationTimeout);
            return message;
        });
        if(response != null) {
            return (List<GetPersonnelDetailsForAdvanceRequestModel>) response;
        } else {
            throw new AdvanceServiceException(ErrorType.SERVICE_NOT_RESPONDING);
        }
    }


}
