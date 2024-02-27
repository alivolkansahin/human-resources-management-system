package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.SendDayOffStatusChangeMailModel;
import org.musketeers.service.MailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendDayOffStatusChangeMailConsumer {

    private final MailService mailService;

    @RabbitListener(queues = "${mail-service-config.rabbitmq.send-day-off-status-change-notification-to-mail-service-queue}")
    public void sendDayOffStatusChangeMailToPersonnel(SendDayOffStatusChangeMailModel model) {
        mailService.sendDayOffRequestStatusChangeMailToPersonnel(model);
    }
}
