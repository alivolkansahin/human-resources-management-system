package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.AuthActivationSupervisorModel;
import org.musketeers.service.GuestService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivationGuestConsumer {

    private final GuestService guestService;

    // activation olmazsa buna gerek yok...
    @RabbitListener(queues = "${guest-service-config.rabbitmq.activation-guest-queue}")
    public void activateGuestFromQueue(AuthActivationSupervisorModel model) {
//        guestService.activateGuest(model);
    }

}
