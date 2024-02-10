package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.entity.Phone;
import org.musketeers.entity.Supervisor;
import org.musketeers.entity.enums.ActivationStatus;
import org.musketeers.rabbitmq.model.GetSupervisorRequestModel;
import org.musketeers.rabbitmq.model.GetSupervisorResponseModel;
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
    public List<GetSupervisorResponseModel>  getSupervisorsAndReturn(GetSupervisorRequestModel model){
        List<Supervisor> pendingSupervisors = supervisorService.getAllSupervisors().stream().filter(supervisor -> supervisor.getActivationStatus().equals(ActivationStatus.PENDING)).toList();
        List<GetSupervisorResponseModel> pendingSupervisorsModel = new ArrayList<>();
        pendingSupervisors.forEach(supervisor -> {
            GetSupervisorResponseModel pendingSupervisorModel = GetSupervisorResponseModel.builder()
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
            pendingSupervisorsModel.add(pendingSupervisorModel);
        });
        return pendingSupervisorsModel;
    }
}
