package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.GetCompanyDetailsByPersonnelResponseModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCompanyDetailsByPersonnelRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${personnel-service-config.rabbitmq.create-personnel-exchange}")
    private String exchange;

    @Value("${personnel-service-config.rabbitmq.get-company-details-by-personnel-binding-key}")
    private String bindingKey;

    public GetCompanyDetailsByPersonnelResponseModel getCompanyDetailsByPersonnelFromCompanyService(List<String> personnelInfos){
        return (GetCompanyDetailsByPersonnelResponseModel) rabbitTemplate.convertSendAndReceive(exchange, bindingKey, personnelInfos);
    }

}
