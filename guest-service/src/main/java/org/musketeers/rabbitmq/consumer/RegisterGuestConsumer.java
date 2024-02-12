package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.entity.Guest;
import org.musketeers.entity.Phone;
import org.musketeers.entity.enums.Gender;
import org.musketeers.entity.enums.PhoneType;
import org.musketeers.rabbitmq.model.RegisterGuestModel;
import org.musketeers.service.GuestService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterGuestConsumer {

    private final GuestService guestService;


    @RabbitListener(queues = "${guest-service-config.rabbitmq.register-guest-queue}")
    public void createGuestFromQueue(RegisterGuestModel model) {
        Guest guest = Guest.builder()
                .authId(model.getAuthid())
                .name(model.getName())
                .lastName(model.getSurName())
                .gender(model.getGender().equalsIgnoreCase("MALE") ? Gender.MALE : Gender.FEMALE)
                .identityNumber(model.getIdentityNumber())
                .email(model.getEmail())
                .image("asd")
                .phone(Phone.builder().phoneType(PhoneType.PERSONAL).phoneNumber(model.getPhone()).build())
                .dateOfBirth(model.getDateOfBirth())
                .build();
        guestService.save(guest);
    }

}
