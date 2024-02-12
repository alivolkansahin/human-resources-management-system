package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCompanyIdFromSupervisorProducer {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange-name}")
    private String exchange;

    @Value("${rabbitmq.get-company-id-from-supervisor-binding-key}")
    private String getCompanyIdFromSupervisorBindingKey;

    public String getCompanyIdFromSupervisor(String authId){
        return (String) rabbitTemplate.convertSendAndReceive(exchange, getCompanyIdFromSupervisorBindingKey, authId);
    }
}
