package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.entity.Guest;
import org.musketeers.rabbitmq.model.AuthRegisterGuestModel;
import org.musketeers.service.GuestService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterGuestConsumer {

    private final GuestService guestService;

    @RabbitListener(queues = "${guest-service-config.rabbitmq.register-guest-queue}")
    public void createGuestFromQueue(AuthRegisterGuestModel model) {
        guestService.save(Guest.builder()
                .build());
    }

}
