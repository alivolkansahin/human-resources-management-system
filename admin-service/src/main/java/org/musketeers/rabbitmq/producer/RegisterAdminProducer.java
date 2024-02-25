package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.AdminServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.rabbitmq.model.GetAllPendingCommentsResponseModel;
import org.musketeers.rabbitmq.model.RegisterAdminModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterAdminProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${admin-service-config.rabbitmq.admin-exchange}")
    private String exchangeName;

    @Value("${admin-service-config.rabbitmq.admin-register-auth-binding-key}")
    private String adminRegisterAuthBindingKey;

    @Value("${admin-service-config.rabbitmq.expiration-timeout}")
    private String expirationTimeout;

    public String getAuthId(RegisterAdminModel model){
        Object response = rabbitTemplate.convertSendAndReceive(exchangeName, adminRegisterAuthBindingKey, model, message -> {
            message.getMessageProperties().setExpiration(expirationTimeout);
            return message;
        });
        if(response != null) {
            return (String) response;
        } else {
            throw new AdminServiceException(ErrorType.SERVICE_NOT_RESPONDING);
        }
    }


}
