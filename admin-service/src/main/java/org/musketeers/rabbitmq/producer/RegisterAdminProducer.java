package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.RegisterAdminModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterAdminProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("adminExchange")
    String exchangeName;

    @Value("${admin-service-config.rabbitmq.admin-register-auth-binding-key}")
    private String adminRegisterAuthBindingKey;

    public String getAuthId(RegisterAdminModel model){
        return (String) rabbitTemplate.convertSendAndReceive(exchangeName, adminRegisterAuthBindingKey, model);
    }


}
