package org.musketeers.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SendSpendingExpenseToCompanyServiceModel {

    private String companyId;

    private String description;

    private Double amount;

    private String currency;

    private LocalDate expenseDate;

}
