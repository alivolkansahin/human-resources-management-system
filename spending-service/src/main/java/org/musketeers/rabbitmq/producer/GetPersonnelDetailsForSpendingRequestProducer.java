package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.ErrorType;
import org.musketeers.exception.SpendingServiceException;
import org.musketeers.rabbitmq.model.GetPersonnelDetailsForSpendingRequestModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPersonnelDetailsForSpendingRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spending-service-config.rabbitmq.spending-exchange}")
    private String directExchange;

    @Value("${spending-service-config.rabbitmq.get-personnel-details-for-spending-request-binding-key}")
    private String bindingKey;

    @Value("${spending-service-config.rabbitmq.expiration-timeout}")
    private String expirationTimeout;

    public List<GetPersonnelDetailsForSpendingRequestModel> getPersonnelDetailsFromPersonnelService(List<String> personnelIds) {
        Object response = rabbitTemplate.convertSendAndReceive(directExchange, bindingKey, personnelIds, message -> {
            message.getMessageProperties().setExpiration(expirationTimeout);
            return message;
        });
        if(response != null) {
            return (List<GetPersonnelDetailsForSpendingRequestModel>) response;
        } else {
            throw new SpendingServiceException(ErrorType.SERVICE_NOT_RESPONDING);
        }
    }


}
