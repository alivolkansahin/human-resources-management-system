package org.musketeers.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.SendSpendingExpenseToCompanyServiceModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendSpendingExpenseToCompanyServiceProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${personnel-service-config.rabbitmq.create-personnel-exchange}")
    private String directExchange;

    @Value("${personnel-service-config.rabbitmq.send-spending-expense-to-company-service-binding-key}")
    private String bindingKey;

    public void sendExpense(SendSpendingExpenseToCompanyServiceModel model) {
        rabbitTemplate.convertAndSend(directExchange, bindingKey, model);
    }

}
