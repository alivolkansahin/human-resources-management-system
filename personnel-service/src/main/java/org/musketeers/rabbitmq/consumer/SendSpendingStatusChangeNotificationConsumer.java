package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.SendSpendingStatusChangeNotificationModel;
import org.musketeers.service.PersonnelService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendSpendingStatusChangeNotificationConsumer {

    private final PersonnelService personnelService;

    @RabbitListener(queues = "${personnel-service-config.rabbitmq.send-spending-status-change-notification-to-personnel-service-queue}")
    public void handleSpendingRequestStatusChangeInPersonnelService(SendSpendingStatusChangeNotificationModel model) {
        personnelService.handleSpendingRequestStatusChange(model);
    }
}
