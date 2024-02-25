package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.rabbitmq.model.GetCompanyDetailsByPersonnelResponseModel;
import org.musketeers.service.CompanyService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCompanyDetailsByPersonnelRequestConsumer {

    private final CompanyService companyService;

    @RabbitListener(queues = "${rabbitmq.get-company-details-by-personnel-queue}")
    public GetCompanyDetailsByPersonnelResponseModel getCompanyDetailsByPersonnel(List<String> personnelInfos) {
        try {
            return companyService.getCompanyDetailsByPersonnel(personnelInfos);
        } catch (CompanyServiceException ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }


}
