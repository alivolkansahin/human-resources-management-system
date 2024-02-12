package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.entity.Guest;
import org.musketeers.entity.enums.ActivationStatus;
import org.musketeers.rabbitmq.model.RegisterGuestActivationModel;
import org.musketeers.service.GuestService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterGuestActivationConsumer {

    private final GuestService guestService;

    @RabbitListener(queues = "${guest-service-config.rabbitmq.register-guest-activation-queue}")
    public void activateGuestFromQueue(RegisterGuestActivationModel model){
        guestService.activateGuest(model.getId());
    }
}
