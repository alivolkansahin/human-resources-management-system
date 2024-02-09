package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.entity.Phone;
import org.musketeers.entity.Supervisor;
import org.musketeers.entity.enums.ActivationStatus;
import org.musketeers.rabbitmq.model.GetSupervisorModelRequest;
import org.musketeers.rabbitmq.model.GetSupervisorModelResponse;
import org.musketeers.service.SupervisorService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetSupervisorRequestConsumer {

    private final SupervisorService supervisorService;

    @RabbitListener(queues = "${supervisor-service-config.rabbitmq.get-supervisor-queue}")
    public List<GetSupervisorModelResponse>  getSupervisorsAndReturn(GetSupervisorModelRequest model){
        // jwt verification
        List<Supervisor> supervisors = supervisorService.getAllSupervisors();
        List<GetSupervisorModelResponse> modelSupervisors = new ArrayList<>();

        supervisors.forEach(supervisor -> {
            if(supervisor.getActivationStatus().equals(ActivationStatus.PENDING)){
                GetSupervisorModelResponse supervisorModelBuild = GetSupervisorModelResponse.builder()
                        .id(supervisor.getId())
                        .authId(supervisor.getAuthId())
                        .name(supervisor.getName())
                        .lastName(supervisor.getLastName())
                        .gender(supervisor.getGender().toString())
                        .identityNumber(supervisor.getIdentityNumber())
                        .email(supervisor.getEmail())
                        .image(supervisor.getImage())
                        .addresses(supervisor.getAddresses())
                        .phones(supervisor.getPhones().stream().map(Phone::toString).toList())
                        .companyName(supervisor.getCompanyName())
                        .dateOfBirth(supervisor.getDateOfBirth())
                        .activationStatus(supervisor.getActivationStatus().toString())
                        .build();
                modelSupervisors.add(supervisorModelBuild);
            }
        });
        return modelSupervisors;
    }
}
