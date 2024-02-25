package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.entity.Phone;
import org.musketeers.entity.Supervisor;
import org.musketeers.entity.enums.ActivationStatus;
import org.musketeers.exception.SupervisorServiceException;
import org.musketeers.rabbitmq.model.GetSupervisorRequestModel;
import org.musketeers.rabbitmq.model.GetSupervisorResponseModel;
import org.musketeers.service.SupervisorService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetSupervisorRequestConsumer {

    private final SupervisorService supervisorService;

    @RabbitListener(queues = "${supervisor-service-config.rabbitmq.get-supervisor-queue}")
    public List<GetSupervisorResponseModel>  getSupervisorsAndReturn(GetSupervisorRequestModel model){
        try {
            return supervisorService.getPendingSupervisors(model);
        } catch (SupervisorServiceException ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }
}
