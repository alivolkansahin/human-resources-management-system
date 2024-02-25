package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.dto.response.RegisteredSupervisorsResponseDTO;
import org.musketeers.exception.AdminServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.mapper.IAdminMapper;
import org.musketeers.rabbitmq.model.GetSupervisorResponseModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisteredSupervisorsRequestProducer {
    private final RabbitTemplate rabbitTemplate;

    @Value("${admin-service-config.rabbitmq.admin-exchange}")
    private String exchangeName;

    @Value("${admin-service-config.rabbitmq.get-supervisor-binding-key}")
    private String bindingKey;

    @Value("${admin-service-config.rabbitmq.expiration-timeout}")
    private String expirationTimeout;

    public List<GetSupervisorResponseModel> convertSendAndReceive(){
        Object response = rabbitTemplate.convertSendAndReceive(exchangeName, bindingKey, "", message -> {
            message.getMessageProperties().setExpiration(expirationTimeout);
            return message;
        });
        if(response != null) {
            return (List<GetSupervisorResponseModel>) response;
        } else {
            throw new AdminServiceException(ErrorType.SERVICE_NOT_RESPONDING);
        }
    }
}
