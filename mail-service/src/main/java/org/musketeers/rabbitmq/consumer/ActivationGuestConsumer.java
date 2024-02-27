package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.ActivationGuestModel;
import org.musketeers.service.MailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivationGuestConsumer {

    private final MailService mailService;

    @RabbitListener(queues = "${mail-service-config.rabbitmq.activation-guest-queue}")
    public void sendActivationMailToGuest(ActivationGuestModel model){
        mailService.sendActivationMailToGuest(model);
    }

}
