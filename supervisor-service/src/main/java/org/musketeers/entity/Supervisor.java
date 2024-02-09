package org.musketeers.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.musketeers.entity.enums.ActivationStatus;
import org.musketeers.entity.enums.Gender;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Document(collection = "supervisors")
public class Supervisor extends BaseEntity {

    @Id
    private String id;

    private String authId;

    private String name;

    private String lastName;

    private Gender gender;

    private String identityNumber;

    private String email;

//    private String password;

    private String image;

    private List<Address> addresses;

    private List<Phone> phones;

    private String companyId;

    private LocalDate dateOfBirth;

    @Builder.Default
    private ActivationStatus activationStatus = ActivationStatus.PENDING;

}
