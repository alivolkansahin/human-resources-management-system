package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.CommentServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.rabbitmq.model.GetCompanyDetailsByCommentResponseModel;
import org.musketeers.rabbitmq.model.GetPersonnelIdAndCompanyIdByTokenRequestModel;
import org.musketeers.rabbitmq.model.GetPersonnelIdAndCompanyIdByTokenResponseModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPersonnelIdAndCompanyIdByTokenRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${comment-service-config.rabbitmq.comment-exchange}")
    private String directExchange;

    @Value("${comment-service-config.rabbitmq.add-comment-personnel-binding-key}")
    private String bindingKey;

    @Value("${comment-service-config.rabbitmq.expiration-timeout}")
    private String expirationTimeout;

    public GetPersonnelIdAndCompanyIdByTokenResponseModel getPersonnelIdAndCompanyIdFromPersonnelService(GetPersonnelIdAndCompanyIdByTokenRequestModel model) {
        Object response = rabbitTemplate.convertSendAndReceive(directExchange, bindingKey, model, message -> {
            message.getMessageProperties().setExpiration(expirationTimeout);
            return message;
        });
        if(response != null) {
            return (GetPersonnelIdAndCompanyIdByTokenResponseModel) response;
        } else {
            throw new CommentServiceException(ErrorType.SERVICE_NOT_RESPONDING);
        }
    }

}
