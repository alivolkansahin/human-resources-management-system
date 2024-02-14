package org.musketeers.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisteredSupervisorsResponseDTO {

    private String authId;

    private String name;

    private String lastName;

    private String gender;

    private String identityNumber;

    private String email;

    private String password;

    private String image;

    private List<String> addresses;

    private List<String> phones;

    private String companyName;

    private LocalDate dateOfBirth;

    private String activationStatus;
}
