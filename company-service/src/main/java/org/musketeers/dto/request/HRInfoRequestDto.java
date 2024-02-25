package org.musketeers.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HRInfoRequestDto {

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String gender;

}
