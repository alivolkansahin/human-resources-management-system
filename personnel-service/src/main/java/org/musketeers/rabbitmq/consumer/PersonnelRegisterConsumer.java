package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.entity.Personnel;
import org.musketeers.rabbitmq.model.AuthPersonnelRegisterModel;
import org.musketeers.service.PersonnelService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonnelRegisterConsumer {

    private final PersonnelService personnelService;

//    @RabbitListener(queues = "${personnel-service-config.rabbitmq.personnel-register-queue}")
//    public void createGuestFromQueue(AuthPersonnelRegisterModel model) {
//        personnelService.save(Personnel.builder()
//                .build());
//    }

}
