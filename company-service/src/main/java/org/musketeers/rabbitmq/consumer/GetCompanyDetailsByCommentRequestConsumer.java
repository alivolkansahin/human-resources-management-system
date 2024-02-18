package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.GetCompanyDetailsByCommentResponseModel;
import org.musketeers.service.CompanyService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCompanyDetailsByCommentRequestConsumer {

    private final CompanyService companyService;
    @RabbitListener(queues = "${rabbitmq.get-company-details-by-comment-company-queue}")
    public List<GetCompanyDetailsByCommentResponseModel> getCompanyInfoByCompanyIds(List<String> companyIds) {
        return companyService.getCompanyInfoByCompanyIds(companyIds);
    }

}
