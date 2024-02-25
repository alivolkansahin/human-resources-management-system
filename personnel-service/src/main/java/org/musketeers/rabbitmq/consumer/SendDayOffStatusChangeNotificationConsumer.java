package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.SendDayOffStatusChangeNotificationModel;
import org.musketeers.service.PersonnelService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendDayOffStatusChangeNotificationConsumer {

    private final PersonnelService personnelService;

    @RabbitListener(queues = "${personnel-service-config.rabbitmq.send-day-off-status-change-notification-to-personnel-service-queue}")
    public void handleDayOffRequestStatusChangeInPersonnelService(SendDayOffStatusChangeNotificationModel model) {
        personnelService.handleDayOffRequestStatusChange(model);
    }
}
