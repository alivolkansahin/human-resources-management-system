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
public class CreatePersonnelFromSupervisorModel {

    private String authId;

    private String name;

    private String lastName;

    private String gender;

    private String identityNumber;

    private String email;

    private String image;

    private String address;

    private String phone;

    private String companyId;

    private LocalDate dateOfBirth;

}
