package org.musketeers.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AdminRegisterDto {

    private String name;

    private String lastName;

    private String email;

    private String password;

    private String phoneNumber;

}
