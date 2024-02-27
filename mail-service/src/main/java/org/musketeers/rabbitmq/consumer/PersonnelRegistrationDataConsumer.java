package org.musketeers.rabbitmq.consumer;

import org.musketeers.rabbitmq.model.CreatePersonnelMailModel;
import org.musketeers.service.MailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class PersonnelRegistrationDataConsumer {
    private final MailService mailService;

    public PersonnelRegistrationDataConsumer(MailService mailService) {
        this.mailService = mailService;
    }

    @RabbitListener(queues = "${mail-service-config.rabbitmq.mail-queue-personnel}")
    public void sendRegistrationDataForPersonnel(CreatePersonnelMailModel model) {
        mailService.sendAccountCreatedInformationToPersonnel(model);
    }
}
