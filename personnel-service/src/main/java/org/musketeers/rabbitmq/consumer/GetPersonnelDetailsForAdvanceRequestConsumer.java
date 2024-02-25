package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.PersonnelServiceException;
import org.musketeers.rabbitmq.model.GetPersonnelDetailsForAdvanceRequestModel;
import org.musketeers.rabbitmq.model.GetPersonnelDetailsForDayOffRequestModel;
import org.musketeers.service.PersonnelService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPersonnelDetailsForAdvanceRequestConsumer {

    private final PersonnelService personnelService;

    @RabbitListener(queues = "${personnel-service-config.rabbitmq.get-personnel-details-for-advance-request-queue}")
    public List<GetPersonnelDetailsForAdvanceRequestModel> getPersonnelDetailsForAdvanceRequest(List<String> personnelIds) {
        try {
            return personnelService.getPersonnelDetailsForAdvanceRequest(personnelIds);
        } catch (PersonnelServiceException ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }
}
