package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.rabbitmq.model.CompanyStatusCheckRequestModel;
import org.musketeers.service.CompanyService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyStatusCheckRequestConsumer {

    private final CompanyService companyService;

    @RabbitListener(queues = "${rabbitmq.company-status-check-queue}")
    public Boolean checkCompanyStatus(CompanyStatusCheckRequestModel model) {
        try {
            return companyService.checkCompanyStatus(model);
        } catch (CompanyServiceException ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }

}
