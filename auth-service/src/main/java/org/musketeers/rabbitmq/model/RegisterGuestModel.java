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
public class RegisterGuestModel {
    private String authid;
    private String name;
    private String surName;
    private String email;
    private String identityNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String phone;
}
