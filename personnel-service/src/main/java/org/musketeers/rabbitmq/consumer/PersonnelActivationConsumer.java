package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.AuthPersonnelRegisterModel;
import org.musketeers.service.PersonnelService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonnelActivationConsumer {

    private final PersonnelService personnelService;

    // activation olmazsa buna gerek yok...
//    @RabbitListener(queues = "${guest-service-config.rabbitmq.activation-guest-queue}")
//    public void activateGuestFromQueue(AuthPersonnelRegisterModel model) {
////        guestService.activateGuest(model);
//    }

}
