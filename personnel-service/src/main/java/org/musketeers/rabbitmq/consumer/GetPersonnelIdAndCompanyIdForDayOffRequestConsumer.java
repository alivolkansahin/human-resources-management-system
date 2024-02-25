package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.PersonnelServiceException;
import org.musketeers.rabbitmq.model.GetPersonnelIdAndCompanyIdForDayOffRequestModel;
import org.musketeers.service.PersonnelService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetPersonnelIdAndCompanyIdForDayOffRequestConsumer {

    private final PersonnelService personnelService;

    @RabbitListener(queues = "${personnel-service-config.rabbitmq.get-personnel-id-and-company-id-for-day-off-request-queue}")
    public GetPersonnelIdAndCompanyIdForDayOffRequestModel getPersonnelIdAndCompanyIdForDayOffRequest(String authId) {
        try {
            return personnelService.getPersonnelIdAndCompanyIdForDayOffRequest(authId);
        } catch (PersonnelServiceException ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }


}
