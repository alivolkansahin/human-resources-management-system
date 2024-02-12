package org.musketeers.rabbitmq.producer;


import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.CreatePersonnelAuthModel;
import org.musketeers.rabbitmq.model.CreatePersonnelCompanyModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePersonnelProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${personnel-service-config.rabbitmq.create-personnel-exchange}")
    private String directExchange;

    @Value("${personnel-service-config.rabbitmq.create-personnel-auth-binding-key}")
    private String authBindingKey;

    @Value("${personnel-service-config.rabbitmq.create-personnel-company-binding-key}")
    private String companyBindingKey;

    public String sendPersonnelInfoToAuth(CreatePersonnelAuthModel model){
        return (String) rabbitTemplate.convertSendAndReceive(directExchange, authBindingKey, model);
    }

    public void sendPersonnelInfoToCompany(CreatePersonnelCompanyModel model){
        rabbitTemplate.convertAndSend(directExchange, companyBindingKey, model);
    }

}
