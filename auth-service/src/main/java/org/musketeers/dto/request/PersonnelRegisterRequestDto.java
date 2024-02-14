//package org.musketeers.dto.request;
//
//import jakarta.validation.constraints.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.musketeers.entity.enums.EGender;
//
//import java.time.LocalDate;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//@Builder                   // VOLKAN: KULLANMIYORUZ, SİLİNEBİLİR... (supervisor ekliyor personel-servisine istek atıp)
//public class PersonnelRegisterRequestDto {
//    @NotBlank(message = "Name cannot be left blank !")
//    private String name;
//    @NotBlank(message = "SurName cannot be left blank !")
//    private String surName;
//    @Email(message = "The entered entry is not in email format !")
//    private String email;
//    @NotBlank(message = "Address cannot be left blank !")
//    private String address;
//    @NotBlank(message = "identityNumber cannot be left blank !")
//    @Size(min = 11, max = 11, message = "Identity number must be 11 characters long")
//    @Pattern(regexp = "[0-9]+", message = "Identity number must contain only digits")
//    private String identityNumber;
//    @NotNull(message = "Date of birth cannot be left blank !")
//    private LocalDate dateOfBirth;
//    @NotNull(message = "Date of employment cannot be left blank !")
//    private LocalDate dateOfEmployment;
//    @NotBlank(message = "Salary cannot be left blank !")
//    private Double salary;
//    @NotNull(message = "Gender must not be null")
//    private String gender;
//    @NotBlank(message = "phone cannot be left blank !")
//    @Size(min = 11, max = 11, message = "Phone must be 11 characters long")
//    private String phone;
//    @NotBlank(message = "CompanyName cannot be left blank !")
//    private String companyName;
//    @NotBlank(message = "Department cannot be left blank !")
//    private String department;
//    @NotBlank(message = "Position cannot be left blank !")
//    private String position;
//}
