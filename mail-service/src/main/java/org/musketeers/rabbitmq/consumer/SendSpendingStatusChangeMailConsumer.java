package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.SendSpendingStatusChangeMailModel;
import org.musketeers.service.MailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendSpendingStatusChangeMailConsumer {

    private final MailService mailService;

    @RabbitListener(queues = "${mail-service-config.rabbitmq.send-spending-status-change-notification-to-mail-service-queue}")
    public void sendSpendingStatusChangeMailToPersonnel(SendSpendingStatusChangeMailModel model) {
        mailService.sendMailForSpendingRequestToPersonnel(model);
    }

}
