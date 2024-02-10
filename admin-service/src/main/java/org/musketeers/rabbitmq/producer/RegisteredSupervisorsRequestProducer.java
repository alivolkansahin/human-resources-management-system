package org.musketeers.rabbitmq.producer;

import org.musketeers.dto.response.RegisteredSupervisorsResponseDTO;
import org.musketeers.mapper.IAdminMapper;
import org.musketeers.rabbitmq.model.GetSupervisorResponseModel;
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
    public List<RegisteredSupervisorsResponseDTO> convertSendAndReceive (String token){
        List<GetSupervisorResponseModel> responseList = (List<GetSupervisorResponseModel>) rabbitTemplate.convertSendAndReceive("adminExchange", "getSupervisorBindingKey", token);
        List<RegisteredSupervisorsResponseDTO> dtoList = new ArrayList<>();
        if (responseList!=null) {
            responseList.forEach((model) -> {
                dtoList.add(IAdminMapper.INSTANCE.supervisorModelToDto(model));
            });
        }
        return dtoList;
    }
}
