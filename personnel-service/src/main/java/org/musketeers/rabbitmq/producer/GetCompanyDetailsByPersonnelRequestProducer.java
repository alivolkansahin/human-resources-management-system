package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.GetCompanyDetailsByPersonnelResponseModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCompanyDetailsByPersonnelRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${personnel-service-config.rabbitmq.create-personnel-exchange}")
    private String exchange;

    @Value("${personnel-service-config.rabbitmq.get-company-details-by-personnel-binding-key}")
    private String bindingKey;

    public GetCompanyDetailsByPersonnelResponseModel getCompanyDetailsByPersonnelFromCompanyService(String personnelId){
        return (GetCompanyDetailsByPersonnelResponseModel) rabbitTemplate.convertSendAndReceive(exchange, bindingKey, personnelId);
    }

}
