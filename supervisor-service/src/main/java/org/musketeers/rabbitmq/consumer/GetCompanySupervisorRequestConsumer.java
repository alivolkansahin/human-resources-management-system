package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.GetCompanySupervisorResponseModel;
import org.musketeers.service.SupervisorService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCompanySupervisorRequestConsumer {

    private final SupervisorService supervisorService;

    @RabbitListener(queues = "${supervisor-service-config.rabbitmq.get-company-supervisors-supervisor-queue}")
    public List<GetCompanySupervisorResponseModel> getCompanySupervisors(List<String> supervisorIds) {
        return supervisorService.getSupervisorByIds(supervisorIds);
    }

}
