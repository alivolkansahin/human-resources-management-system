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
public class SendAdvanceExpenseToCompanyServiceModel {

    private String companyId;

    private String description;

    private Double amount;

    private LocalDate expenseDate;

}
