package org.musketeers.rabbitmq.producer;


import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.ActivationGuestModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderForGuestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.auth-exchange}")
    private String directExchange;

    @Value("${rabbitmq.mail-bindingKey}")
    private String mailBindingKey;

    public void convertAndSendToRabbit(ActivationGuestModel model){
        rabbitTemplate.convertAndSend(directExchange,mailBindingKey,model);
    }
}
