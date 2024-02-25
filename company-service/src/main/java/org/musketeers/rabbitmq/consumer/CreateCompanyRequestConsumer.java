package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.rabbitmq.model.CreateCompanyRequestModel;
import org.musketeers.rabbitmq.model.CreateCompanyResponseModel;
import org.musketeers.service.CompanyService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCompanyRequestConsumer {

    private final CompanyService companyService;

    @RabbitListener(queues = "${rabbitmq.supervisor-activation-company-queue}")
    public CreateCompanyResponseModel createCompanyFromQueue(CreateCompanyRequestModel model) {
        try {
            return companyService.createCompanyWithApprovedSupervisor(model);
        } catch (CompanyServiceException ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }
}
