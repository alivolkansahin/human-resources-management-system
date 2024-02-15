package org.musketeers.rabbitmq.producer;


import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.GetCompanyIdFromSupervisorTokenModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCompanyIdFromSupervisorTokenProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${personnel-service-config.rabbitmq.create-personnel-exchange}")
    private String directExchange;

    @Value("${personnel-service-config.rabbitmq.get-company-id-supervisor-binding-key}")
    private String supervisorBindingKey;

    public String getCompanyIdFromSupervisorToken(GetCompanyIdFromSupervisorTokenModel model) {
        return (String) rabbitTemplate.convertSendAndReceive(directExchange, supervisorBindingKey, model);
    }

}
