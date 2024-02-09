package org.musketeers.rabbitmq.producer;

import org.musketeers.dto.response.RegisteredSupervisorsResponseDTO;
import org.musketeers.mapper.IAdminMapper;
import org.musketeers.rabbitmq.model.GetSupervisorModelResponse;
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
        List<GetSupervisorModelResponse> responseList = (List<GetSupervisorModelResponse>) rabbitTemplate.convertSendAndReceive("exchange-admin", "binding-key", token);
        List<RegisteredSupervisorsResponseDTO> dtoList = new ArrayList<>();
        if (responseList!=null) {
            responseList.forEach((model) -> {
                dtoList.add(IAdminMapper.INSTANCE.supervisorModelToDto(model));
            });
        }
        return dtoList;
    }
}
