package org.musketeers.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GuestRegisterRequestDto {
    @NotBlank(message = "Name cannot be left blank !")
    private String name;
    @NotBlank(message = "SurName cannot be left blank !")
    private String surName;
    @Email(message = "The entered entry is not in email format !")
    private String email;


    /*@NotBlank(message = "Password cannot be left blank !")
    @Pattern(regexp="^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[^\\w\\d\\s])(?!.*\\s).{8,32}$",message =
            "The password must be between 8-32 characters and contain 1 number, 1 uppercase character, 1 lowercase character and 1 special character.")*/

    @NotBlank(message = "Password cannot be left blank !")
    @Pattern(regexp="^(?=.*[0-9])(?=.*[A-Z]).{8,32}$",message =
            "The password must be between 8-32 characters and contain 1 number, 1 uppercase character.")
    private String password;
    private String rePassword;
    @NotBlank(message = "identityNumber cannot be left blank !")
    @Size(min = 11, max = 11, message = "Identity number must be 11 characters long")
    @Pattern(regexp = "[0-9]+", message = "Identity number must contain only digits")
    private String identityNumber;
    @NotNull(message = "Date of birth cannot be left blank !")
    private LocalDate dateOfBirth;
    @NotNull(message = "Gender must not be null")
    private String gender;
    @NotBlank(message = "phone cannot be left blank !")
    @Size(min = 11, max = 11, message = "Phone must be 11 characters long")
    private String phone;
}
