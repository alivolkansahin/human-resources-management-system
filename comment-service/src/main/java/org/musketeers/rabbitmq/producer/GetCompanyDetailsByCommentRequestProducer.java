package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.GetAllPendingCommentsResponseModel;
import org.musketeers.rabbitmq.model.GetCompanyDetailsByCommentResponseModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCompanyDetailsByCommentRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${comment-service-config.rabbitmq.comment-exchange}")
    private String directExchange;

    @Value("${comment-service-config.rabbitmq.get-company-details-by-comment-company-binding-key}")
    private String bindingKey;

    public List<GetCompanyDetailsByCommentResponseModel> getCompanyInfo(List<String> companyIds) {
        return (List<GetCompanyDetailsByCommentResponseModel>) rabbitTemplate.convertSendAndReceive(directExchange, bindingKey, companyIds);
    }


}
