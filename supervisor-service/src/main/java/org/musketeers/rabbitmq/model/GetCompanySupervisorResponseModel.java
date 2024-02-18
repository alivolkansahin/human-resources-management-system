package org.musketeers.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetCompanySupervisorResponseModel {

    private String name;

    private String lastName;

    private String gender;

    private String image;

    private String dateOfBirth;

}
