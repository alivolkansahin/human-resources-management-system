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
public class CreatePersonnelCompanyModel {

    private String personnelId;

    private String companyId;

    private String departmentId;

    private String expenseDescription;

    private Double expenseAmount;

    private LocalDate expenseDate;

}
