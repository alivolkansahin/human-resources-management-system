package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.CreateCompanyRequestModel;
import org.musketeers.rabbitmq.model.CreateCompanyResponseModel;
import org.musketeers.repository.entity.Company;
import org.musketeers.repository.entity.Supervisor;
import org.musketeers.service.CompanyService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateCompanyRequestConsumer {

    private final CompanyService companyService;

    @RabbitListener(queues = "${rabbitmq.supervisor-activation-company-queue}")
    public CreateCompanyResponseModel createCompanyFromQueue(CreateCompanyRequestModel model) {
        // VOLKAN:
        // Company name database'de nasıl tutulacak (hem burda, hem supervisorda) ona göre .toLowerCase falan düşünülecek.
        // Ama bu durumda da company getirirken adı hep küçük çıkacak, onun ayarlanması gerekecek...
        Optional<Company> optionalCompany = companyService.findByCompanyName(model.getCompanyName());
        Company company = null;
        if(optionalCompany.isEmpty()) {
            company = Company.builder()
                    .companyName(model.getCompanyName())
                    .build();
            companyService.save(company);
            company.setSupervisors(Arrays.asList(Supervisor.builder().company(company).supervisorId(model.getSupervisorId()).build()));
        } else {
            company = optionalCompany.get();
            company.getSupervisors().add(Supervisor.builder().company(company).supervisorId(model.getSupervisorId()).build());
        }
        companyService.update(company);
        return CreateCompanyResponseModel.builder().companyId(company.getId()).build();
    }

}
