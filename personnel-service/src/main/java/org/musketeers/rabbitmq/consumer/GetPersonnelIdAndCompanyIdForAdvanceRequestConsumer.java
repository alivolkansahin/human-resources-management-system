package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.PersonnelServiceException;
import org.musketeers.rabbitmq.model.GetPersonnelIdAndCompanyIdForAdvanceRequestModel;
import org.musketeers.service.PersonnelService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetPersonnelIdAndCompanyIdForAdvanceRequestConsumer {

    private final PersonnelService personnelService;

    @RabbitListener(queues = "${personnel-service-config.rabbitmq.get-personnel-id-and-company-id-for-advance-request-queue}")
    public GetPersonnelIdAndCompanyIdForAdvanceRequestModel getPersonnelIdAndCompanyIdForAdvanceRequest(String authId) {
        try {
            return personnelService.getPersonnelIdAndCompanyIdForAdvanceRequest(authId);
        } catch (PersonnelServiceException ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }
}
