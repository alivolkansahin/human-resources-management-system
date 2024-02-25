package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.SendAdvanceStatusChangeNotificationModel;
import org.musketeers.rabbitmq.model.SendDayOffStatusChangeNotificationModel;
import org.musketeers.service.PersonnelService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendAdvanceStatusChangeNotificationConsumer {

    private final PersonnelService personnelService;

    @RabbitListener(queues = "${personnel-service-config.rabbitmq.send-advance-status-change-notification-to-personnel-service-queue}")
    public void handleAdvanceRequestStatusChangeInPersonnelService(SendAdvanceStatusChangeNotificationModel model) {
        personnelService.handleAdvanceRequestStatusChange(model);
    }
}
