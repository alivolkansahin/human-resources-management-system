package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.entity.Phone;
import org.musketeers.entity.Supervisor;
import org.musketeers.entity.enums.Gender;
import org.musketeers.entity.enums.PhoneType;
import org.musketeers.rabbitmq.model.RegisterSupervisorModel;
import org.musketeers.service.SupervisorService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterSupervisorConsumer {

    private final SupervisorService supervisorService;

    @RabbitListener(queues = "${supervisor-service-config.rabbitmq.register-supervisor-queue}")
    public void createSupervisorFromQueue(RegisterSupervisorModel model) {
        List<String> address = new ArrayList<>();
        List<Phone> phones = new ArrayList<>();
        address.add(model.getAddress());
        phones.add(Phone.builder().phoneType(PhoneType.PERSONAL).build());
        Supervisor supervisor = Supervisor.builder()
                .authId(model.getAuthid())
                .name(model.getName())
                .lastName(model.getSurName())
                .gender(model.getGender().equals("male") ? Gender.MALE : Gender.FEMALE)
                .identityNumber(model.getIdentityNumber())
                .email(model.getEmail())
                .image("asd")
                .addresses(address)
                .phones(phones)
                .dateOfBirth(model.getDateOfBirth())
                .companyName(model.getCompanyName())
                .build();
        supervisorService.save(supervisor);
    }



}
