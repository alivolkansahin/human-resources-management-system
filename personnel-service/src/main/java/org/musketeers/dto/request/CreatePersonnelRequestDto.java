package org.musketeers.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreatePersonnelRequestDto {

    private String name;

    private String lastName;

    private String gender;

    private String identityNumber;

    private String email;

    private String image;

    private String address;

    private String phone;

    private String companyId; //fronttan company bilgilerinden gelcek

    private String departmentId; // fronttan company bilgilerinden gelcek.

    private String position; // fronttan company bilgilerinden gelcek

    private LocalDate dateOfEmployment;

    private LocalDate dateOfBirth;

    private Double salary;

}
