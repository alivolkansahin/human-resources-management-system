package org.musketeers.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SupervisorInfoResponseDto {

    private String name;

    private String lastName;

    private String gender;

    private String image;

    private String dateOfBirth;

}
