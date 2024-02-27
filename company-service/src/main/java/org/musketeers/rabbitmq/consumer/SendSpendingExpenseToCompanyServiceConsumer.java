package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.SendSpendingExpenseToCompanyServiceModel;
import org.musketeers.service.ExpenseService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendSpendingExpenseToCompanyServiceConsumer {

    private final ExpenseService expenseService;

    @RabbitListener(queues = "${rabbitmq.send-spending-expense-to-company-service-queue}")
    public void saveSpendingExpense(SendSpendingExpenseToCompanyServiceModel model) {
        expenseService.saveSpendingExpense(model);
    }

}
