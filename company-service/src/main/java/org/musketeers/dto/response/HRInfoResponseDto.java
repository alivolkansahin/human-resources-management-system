package org.musketeers.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.musketeers.repository.enums.EGender;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HRInfoResponseDto {

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String image;

}
