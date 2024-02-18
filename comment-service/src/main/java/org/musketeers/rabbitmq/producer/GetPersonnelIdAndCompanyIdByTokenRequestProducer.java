package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.GetPersonnelIdAndCompanyIdByTokenRequestModel;
import org.musketeers.rabbitmq.model.GetPersonnelIdAndCompanyIdByTokenResponseModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetPersonnelIdAndCompanyIdByTokenRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${comment-service-config.rabbitmq.comment-exchange}")
    private String directExchange;

    @Value("${comment-service-config.rabbitmq.add-comment-personnel-binding-key}")
    private String bindingKey;

    public GetPersonnelIdAndCompanyIdByTokenResponseModel getPersonnelIdAndCompanyIdFromPersonnelService(GetPersonnelIdAndCompanyIdByTokenRequestModel model) {
        return (GetPersonnelIdAndCompanyIdByTokenResponseModel) rabbitTemplate.convertSendAndReceive(directExchange, bindingKey, model);
    }

}
