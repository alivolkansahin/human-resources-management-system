package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.GetPersonnelIdAndCompanyIdByTokenRequestModel;
import org.musketeers.rabbitmq.model.GetPersonnelIdAndCompanyIdByTokenResponseModel;
import org.musketeers.service.PersonnelService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetPersonnelIdAndCompanyIdByTokenRequestConsumer {

    private final PersonnelService personnelService;

    @RabbitListener(queues = "${personnel-service-config.rabbitmq.add-comment-personnel-queue}")
    public GetPersonnelIdAndCompanyIdByTokenResponseModel getPersonnelIdAndCompanyIdByToken(GetPersonnelIdAndCompanyIdByTokenRequestModel model) {
        return personnelService.getPersonnelIdAndCompanyIdByToken(model);
    }


}
