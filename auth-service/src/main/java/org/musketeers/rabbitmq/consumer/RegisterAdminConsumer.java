package org.musketeers.rabbitmq.consumer;

import org.musketeers.entity.Auth;
import org.musketeers.entity.enums.ERole;
import org.musketeers.entity.enums.EStatus;
import org.musketeers.rabbitmq.model.RegisterAdminModel;
import org.musketeers.service.AuthService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RegisterAdminConsumer {

    private final AuthService authService;


    public RegisterAdminConsumer(AuthService authService) {
        this.authService = authService;
    }

    @RabbitListener(queues = "${rabbitmq.admin-register-auth-queue}")
    public String saveAdmin(RegisterAdminModel model) {
        Auth auth = Auth.builder()
                .email(model.getEmail())
                .password(model.getPassword())
                .phone(model.getPhone())
                .status(EStatus.ACTIVE)
                .role(ERole.ADMIN)
                .build();
        authService.save(auth);
        return auth.getId();
    }


}
