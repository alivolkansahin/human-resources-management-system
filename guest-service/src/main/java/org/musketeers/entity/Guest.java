package org.musketeers.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.musketeers.entity.enums.ActivationStatus;
import org.musketeers.entity.enums.Gender;
import org.musketeers.entity.enums.Image;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Document(collection = "guests")
public class Guest extends BaseEntity {

    @Id
    private String id;

    private String authId;

    private String name;

    private String lastName;

    private Gender gender;

    private String identityNumber;

    private String email;

    private String image;

    private Phone phone;

    private LocalDate dateOfBirth;

    @Builder.Default
    private ActivationStatus activationStatus = ActivationStatus.PENDING;

}
