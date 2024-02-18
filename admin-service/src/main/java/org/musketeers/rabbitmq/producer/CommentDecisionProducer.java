package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.CommentDecisionModel;
import org.musketeers.rabbitmq.model.SupervisorRegistrationDecisionModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentDecisionProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${admin-service-config.rabbitmq.admin-exchange}")
    private String directExchange;

    @Value("${admin-service-config.rabbitmq.admin-handle-comment-decision-binding-key}")
    private String bindingKey;

    public void sendCommentDecision(CommentDecisionModel model){
        rabbitTemplate.convertAndSend(directExchange, bindingKey, model);
    }

}
