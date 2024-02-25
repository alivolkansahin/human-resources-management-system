package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.rabbitmq.model.GetCompanyDetailsByCommentResponseModel;
import org.musketeers.service.CompanyService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCompanyDetailsByCommentRequestConsumer {

    private final CompanyService companyService;
    @RabbitListener(queues = "${rabbitmq.get-company-details-by-comment-company-queue}")
    public List<GetCompanyDetailsByCommentResponseModel> getCompanyInfoByCompanyIds(List<String> companyIds) {
        try {
            return companyService.getCompanyInfoByCompanyIds(companyIds);
        } catch (CompanyServiceException ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }

}
