package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.AdminServiceException;
import org.musketeers.exception.ErrorType;
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

    @Value("${admin-service-config.rabbitmq.expiration-timeout}")
    private String expirationTimeout;

    public List<GetAllPendingCommentsResponseModel> getPendingComments() {
        Object response = rabbitTemplate.convertSendAndReceive(directExchange, bindingKey,"", message -> {
            message.getMessageProperties().setExpiration(expirationTimeout);
            return message;
        });
        if(response != null) {
            return (List<GetAllPendingCommentsResponseModel>) response;
        } else {
            throw new AdminServiceException(ErrorType.SERVICE_NOT_RESPONDING);
        }
    }

}
