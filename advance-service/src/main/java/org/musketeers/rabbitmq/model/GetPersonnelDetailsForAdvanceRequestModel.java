package org.musketeers.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetPersonnelDetailsForAdvanceRequestModel {

    private String personnelId;

    private String name;

    private String lastName;

    private String image;

    private String email;

    private String advanceQuota;

}
