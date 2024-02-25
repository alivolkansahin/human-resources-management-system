package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.entity.Supervisor;
import org.musketeers.exception.SupervisorServiceException;
import org.musketeers.rabbitmq.model.GetCompanyIdFromSupervisorTokenModel;
import org.musketeers.service.SupervisorService;
import org.musketeers.utility.JwtTokenManager;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCompanyIdFromSupervisorTokenConsumer {

    private final SupervisorService supervisorService;

    private final JwtTokenManager jwtTokenManager;

    @RabbitListener(queues = "${supervisor-service-config.rabbitmq.get-company-id-supervisor-queue}")
    public String sendSupervisorsCompanyId(GetCompanyIdFromSupervisorTokenModel model){
        try {
            return supervisorService.sendSupervisorsCompanyId(model);
        } catch (SupervisorServiceException ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }

}
