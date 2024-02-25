package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.PersonnelServiceException;
import org.musketeers.rabbitmq.model.GetPersonnelDetailsByCommentResponseModel;
import org.musketeers.service.PersonnelService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPersonnelDetailsByCommentRequestConsumer {

    private final PersonnelService personnelService;

    @RabbitListener(queues = "${personnel-service-config.rabbitmq.get-personnel-details-by-comment-personnel-queue}")
    public List<GetPersonnelDetailsByCommentResponseModel> getPersonnelInfoByPersonnelId(List<String> personnelIds) {
        try {
            return personnelService.getPersonnelInfoByPersonnelId(personnelIds);
        } catch (PersonnelServiceException ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }

}
