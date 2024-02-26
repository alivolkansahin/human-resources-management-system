package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.SendAdvanceExpenseToCompanyServiceModel;
import org.musketeers.service.ExpenseService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendAdvanceExpenseToCompanyServiceConsumer {

    private final ExpenseService expenseService;

    @RabbitListener(queues = "${rabbitmq.send-advance-expense-to-company-service-queue}")
    public void saveAdvanceExpense(SendAdvanceExpenseToCompanyServiceModel model) {
        expenseService.saveAdvanceExpense(model);
    }

}
