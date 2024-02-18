package org.musketeers.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PersonnelDetailsResponseDto {

    private String name;

    private String lastName;

    private String gender;

    private String image;

    private LocalDate dateOfEmployment;

}
