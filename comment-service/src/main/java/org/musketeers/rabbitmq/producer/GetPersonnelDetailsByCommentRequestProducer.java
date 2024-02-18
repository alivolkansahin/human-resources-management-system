package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.GetPersonnelDetailsByCommentResponseModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPersonnelDetailsByCommentRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${comment-service-config.rabbitmq.comment-exchange}")
    private String directExchange;

    @Value("${comment-service-config.rabbitmq.get-personnel-details-by-comment-personnel-binding-key}")
    private String bindingKey;

    public List<GetPersonnelDetailsByCommentResponseModel> getPersonnelInfo(List<String> personnelIds){
        return (List<GetPersonnelDetailsByCommentResponseModel>) rabbitTemplate.convertSendAndReceive(directExchange, bindingKey, personnelIds);
    }




}
