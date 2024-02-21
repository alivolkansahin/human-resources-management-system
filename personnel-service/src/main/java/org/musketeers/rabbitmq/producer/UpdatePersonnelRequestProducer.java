package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.UpdatePersonnelRequestModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdatePersonnelRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${personnel-service-config.rabbitmq.create-personnel-exchange}")
    private String directExchange;

    @Value("${personnel-service-config.rabbitmq.update-personnel-by-id-auth-binding-key}")
    private String bindingKey;

    public Optional<Boolean> updateAuthService(UpdatePersonnelRequestModel model) {
        return Optional.ofNullable((Boolean) rabbitTemplate.convertSendAndReceive(directExchange, bindingKey, model));
    }

}
