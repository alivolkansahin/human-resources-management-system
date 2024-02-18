package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.GetCompanySupervisorResponseModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCompanySupervisorRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange-name}")
    private String exchangeName;

    @Value("${rabbitmq.get-company-supervisors-supervisor-binding-key}")
    private String bindingKey;

    public List<GetCompanySupervisorResponseModel> getCompanySupervisorInfo(List<String> supervisorIds) {
        return (List<GetCompanySupervisorResponseModel>) rabbitTemplate.convertSendAndReceive(exchangeName, bindingKey, supervisorIds);
    }

}
