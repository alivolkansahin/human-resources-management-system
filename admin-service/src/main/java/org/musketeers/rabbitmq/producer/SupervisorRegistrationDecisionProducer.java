package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.SupervisorRegistrationDecisionModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupervisorRegistrationDecisionProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${admin-service-config.rabbitmq.admin-supervisor-registration-decision-exchange}")
    private String fanoutExchange;

    public void sendRegistrationDecision(SupervisorRegistrationDecisionModel model){
        rabbitTemplate.convertAndSend(fanoutExchange,"", model);
    }

}
