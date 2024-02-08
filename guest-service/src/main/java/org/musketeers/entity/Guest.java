package org.musketeers.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

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

    private String identityNumber;

    private String email;

    private String password;

    private String image;

    private List<Address> addresses;

    private Phone phone;

    private LocalDate dateOfBirth;

}
