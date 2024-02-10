package org.musketeers.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetSupervisorResponseModel {

    private String id;

    private String authId;

    private String name;

    private String lastName;

    private String gender;

    private String identityNumber;

    private String email;

    private String image;

    private List<String> addresses;

    private List<String> phones;

    private String companyName;

    private String companyId;

    private LocalDate dateOfBirth;

    private String activationStatus;

}
