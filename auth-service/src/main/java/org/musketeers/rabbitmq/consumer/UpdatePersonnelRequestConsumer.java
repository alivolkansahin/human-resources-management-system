package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.AuthServiceException;
import org.musketeers.rabbitmq.model.UpdatePersonnelRequestModel;
import org.musketeers.service.AuthService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatePersonnelRequestConsumer {

    private final AuthService authService;

    @RabbitListener(queues = "${rabbitmq.update-personnel-by-id-auth-queue}")
    public Boolean updatePersonnelInAuthService(UpdatePersonnelRequestModel model) {
        try {
            return authService.updatePersonnel(model);
        } catch (Exception ex){
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }

}
