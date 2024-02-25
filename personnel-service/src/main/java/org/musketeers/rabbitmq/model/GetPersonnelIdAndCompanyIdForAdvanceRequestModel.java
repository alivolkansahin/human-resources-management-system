package org.musketeers.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetPersonnelIdAndCompanyIdForAdvanceRequestModel {

    private String personnelId;

    private String companyId;

}
