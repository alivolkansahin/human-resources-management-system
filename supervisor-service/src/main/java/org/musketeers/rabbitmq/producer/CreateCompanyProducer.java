package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.CreateCompanyRequestModel;
import org.musketeers.rabbitmq.model.CreateCompanyResponseModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCompanyProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${supervisor-service-config.rabbitmq.supervisor-activation-exchange}")
    private String directExchange;

    @Value("${supervisor-service-config.rabbitmq.supervisor-activation-company-binding-key}")
    private String bindingKey;

    public CreateCompanyResponseModel createCompanyAndReturn(CreateCompanyRequestModel model){
        return (CreateCompanyResponseModel) rabbitTemplate.convertSendAndReceive(directExchange,bindingKey,model);
    }

}
