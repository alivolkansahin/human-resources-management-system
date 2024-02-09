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
public class AuthRegisterSupervisorModel {

    private String name;
    private String lastName;
    private String email;
//    private String password;

    private String identityNumber;
    private LocalDate dateOfBirth;
    //    private EGender gender; // string ???
    private String phone;

//    private Address address; // hmm... parça parça string almak daha mantıklı gibi

    private String companyName; // burayı auth register yaparken rabbit ile supervisor servisten tokeni deşifre edip findByAuthid ile yöneticinin companyId'sini bulup geri methoduna dahil edecek ???

}
