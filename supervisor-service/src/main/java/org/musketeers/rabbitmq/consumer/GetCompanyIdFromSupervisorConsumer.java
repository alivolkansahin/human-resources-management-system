package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.entity.Supervisor;
import org.musketeers.exception.SupervisorServiceException;
import org.musketeers.service.SupervisorService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCompanyIdFromSupervisorConsumer {

    private final SupervisorService supervisorService;

    @RabbitListener(queues = "${supervisor-service-config.rabbitmq.get-company-id-from-supervisor-queue}")
    public String sendSupervisorsCompanyId(String authId){
        try {
            return supervisorService.sendSupervisorsCompanyId(authId);
        } catch (SupervisorServiceException ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }



}
