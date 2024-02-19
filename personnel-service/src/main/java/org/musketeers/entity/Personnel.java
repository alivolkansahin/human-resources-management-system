package org.musketeers.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
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
@Document(collection = "personnels")
public class Personnel extends BaseEntity {

    @Id
    private String id;

    private String authId;

    private String name;

    private String lastName;

    private Gender gender;

    private String identityNumber;

    private String email;

    private String image;

    private List<String> addresses;

    private List<Phone> phones;

    private String companyId;

    private String departmentId;

    private String position;

    private LocalDate dateOfEmployment;

    private LocalDate dateOfBirth;

    private Double salary;

    @Builder.Default
    private Double dayOff = 14d;

}
