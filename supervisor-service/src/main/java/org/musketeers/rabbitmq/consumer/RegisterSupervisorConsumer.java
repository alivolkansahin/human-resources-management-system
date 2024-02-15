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
        phones.add(Phone.builder().phoneType(PhoneType.PERSONAL).phoneNumber(model.getPhone()).build());
        Supervisor supervisor = Supervisor.builder()
                .authId(model.getAuthid())
                .name(model.getName())
                .lastName(model.getSurName())
                .gender(model.getGender().equalsIgnoreCase("MALE") ? Gender.MALE : Gender.FEMALE)
                .identityNumber(model.getIdentityNumber())
                .email(model.getEmail())
                .image(model.getGender().equalsIgnoreCase("MALE") ?
                        "https://us.123rf.com/450wm/thesomeday123/thesomeday1231712/thesomeday123171200009/91087331-default-avatar-profile-icon-for-male-grey-photo-placeholder-illustrations-vector.jpg?ver=6"
                        :
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ6SR5emlvKa5Trq207GwkpiamFuQFskm8zLniDY04frA&s") ///
                .addresses(address)
                .phones(phones)
                .dateOfBirth(model.getDateOfBirth())
                .companyName(model.getCompanyName())
                .build();
        supervisorService.save(supervisor);
    }



}
