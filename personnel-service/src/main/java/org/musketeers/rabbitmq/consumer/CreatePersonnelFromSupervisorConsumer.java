package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.CreatePersonnelFromSupervisorModel;
import org.musketeers.service.PersonnelService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePersonnelFromSupervisorConsumer {

    private final PersonnelService personnelService;

    @RabbitListener(queues = "${personnel-service-config.rabbitmq.create-personnel-from-supervisor-queue}")
    public void createPersonnelFromSupervisor(CreatePersonnelFromSupervisorModel model) {
        personnelService.createPersonnelFromSupervisor(model);
    }
    
}
