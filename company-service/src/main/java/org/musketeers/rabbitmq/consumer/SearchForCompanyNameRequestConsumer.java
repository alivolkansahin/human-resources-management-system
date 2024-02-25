package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.service.CompanyService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchForCompanyNameRequestConsumer {

    private final CompanyService companyService;

    @RabbitListener(queues = "${rabbitmq.search-for-company-name-queue}")
    public Boolean searchCompanyName(String companyName) {
        try {
            return companyService.searchCompanyName(companyName);
        } catch (CompanyServiceException ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }

    }

}
