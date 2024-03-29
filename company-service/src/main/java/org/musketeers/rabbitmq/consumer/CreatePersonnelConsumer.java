package org.musketeers.rabbitmq.consumer;


import lombok.RequiredArgsConstructor;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.rabbitmq.model.CreatePersonnelCompanyModel;
import org.musketeers.repository.entity.Department;
import org.musketeers.repository.entity.Personnel;
import org.musketeers.service.DepartmentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePersonnelConsumer {

    private final DepartmentService departmentService;

    @RabbitListener(queues = "${rabbitmq.create-personnel-company-queue}")
    public void addPersonnelToDepartment(CreatePersonnelCompanyModel model) {
        departmentService.addPersonnelToDepartment(model);
    }
}
