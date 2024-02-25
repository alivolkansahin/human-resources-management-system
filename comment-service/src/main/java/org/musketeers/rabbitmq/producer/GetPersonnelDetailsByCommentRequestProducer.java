package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.CommentServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.rabbitmq.model.GetCompanyDetailsByCommentResponseModel;
import org.musketeers.rabbitmq.model.GetPersonnelDetailsByCommentResponseModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPersonnelDetailsByCommentRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${comment-service-config.rabbitmq.comment-exchange}")
    private String directExchange;

    @Value("${comment-service-config.rabbitmq.get-personnel-details-by-comment-personnel-binding-key}")
    private String bindingKey;

    @Value("${comment-service-config.rabbitmq.expiration-timeout}")
    private String expirationTimeout;

    public List<GetPersonnelDetailsByCommentResponseModel> getPersonnelInfo(List<String> personnelIds){
        Object response = rabbitTemplate.convertSendAndReceive(directExchange, bindingKey, personnelIds, message -> {
            message.getMessageProperties().setExpiration(expirationTimeout);
            return message;
        });
        if(response != null) {
            return (List<GetPersonnelDetailsByCommentResponseModel>) response;
        } else {
            throw new CommentServiceException(ErrorType.SERVICE_NOT_RESPONDING);
        }
    }




}
