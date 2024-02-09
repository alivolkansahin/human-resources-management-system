package org.musketeers.rabbitmq.producer;

import org.musketeers.rabbitmq.model.SupervisorModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegisteredSupervisorsRequestProducer {
    private final RabbitTemplate rabbitTemplate;

    public RegisteredSupervisorsRequestProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public List<SupervisorModel> convertSendAndReceive (String token){
        Object o = rabbitTemplate.convertSendAndReceive("direct-exchange-auth", "save-auth-binding-key", token);
        List<SupervisorModel> modelList = new ArrayList<>();
        SupervisorModel model = SupervisorModel.builder().build();
        modelList.add(model);
        return modelList;
    }
}
