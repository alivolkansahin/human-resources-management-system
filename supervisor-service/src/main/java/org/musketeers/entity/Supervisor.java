package org.musketeers.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.musketeers.entity.enums.ActivationStatus;
import org.musketeers.entity.enums.EGender;
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

    private EGender EGender;

    private String identityNumber;

    private String email;

    private String image;

    private List<String> addresses;

    private List<Phone> phones;

    private String companyName;

    private String companyId;

    private LocalDate dateOfBirth;

    @Builder.Default
    private ActivationStatus activationStatus = ActivationStatus.PENDING;

    private Boolean isCompanyFirstRegistration;

    private String contractName;

    private Integer contractDuration;

    private Double contractCost;

    private String contractCurrency;

}
