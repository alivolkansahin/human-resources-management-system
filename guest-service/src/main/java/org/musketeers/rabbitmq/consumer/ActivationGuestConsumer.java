package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.AuthActivationGuestModel;
import org.musketeers.service.GuestService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivationGuestConsumer {

    private final GuestService guestService;

//    @RabbitListener(queues = "${guest-service-config.rabbitmq.activation-guest-queue}")
//    public void activateGuestFromQueue(AuthActivationGuestModel model) {
////        guestService.activateGuest(model.getAuthId());
//    }

}
