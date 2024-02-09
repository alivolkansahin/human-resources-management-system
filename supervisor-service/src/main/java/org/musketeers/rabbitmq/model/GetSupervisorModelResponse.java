package org.musketeers.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.musketeers.entity.Phone;
import org.musketeers.entity.enums.ActivationStatus;
import org.musketeers.entity.enums.Gender;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetSupervisorModelResponse {

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
