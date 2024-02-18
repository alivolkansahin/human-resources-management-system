package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.GetAllPendingCommentsResponseModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllPendingCommentsRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${admin-service-config.rabbitmq.admin-exchange}")
    private String directExchange;

    @Value("${admin-service-config.rabbitmq.admin-get-all-pending-comments-comment-binding-key}")
    private String bindingKey;

    public List<GetAllPendingCommentsResponseModel> getPendingComments() {
        return (List<GetAllPendingCommentsResponseModel>) rabbitTemplate.convertSendAndReceive(directExchange, bindingKey,"");
    }

}
