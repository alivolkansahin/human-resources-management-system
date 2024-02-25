package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.rabbitmq.model.GetCompanySupervisorResponseModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCompanySupervisorRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange-name}")
    private String exchangeName;

    @Value("${rabbitmq.get-company-supervisors-supervisor-binding-key}")
    private String bindingKey;

    @Value("${rabbitmq.expiration-timeout}")
    private String expirationTimeout;

    public List<GetCompanySupervisorResponseModel> getCompanySupervisorInfo(List<String> supervisorIds) {
        Object response = rabbitTemplate.convertSendAndReceive(exchangeName, bindingKey, supervisorIds, message -> {
            message.getMessageProperties().setExpiration(expirationTimeout);
            return message;
        });
        if(response != null) {
            return (List<GetCompanySupervisorResponseModel>) response;
        } else {
            throw new CompanyServiceException(ErrorType.SERVICE_NOT_RESPONDING);
        }
    }

}
