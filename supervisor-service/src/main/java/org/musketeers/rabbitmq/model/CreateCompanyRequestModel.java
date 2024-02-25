package org.musketeers.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateCompanyRequestModel {

    private String supervisorId;

    private String companyName;

    private String contractName;

    private Integer contractDuration;

    private Double contractCost;

    private String contractCurrency;

}
