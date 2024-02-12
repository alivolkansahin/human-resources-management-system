package org.musketeers.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LoginRequestDto {
    @NotBlank(message = "Email or phone number cannot be left blank !")
    private String identity;
    @NotBlank(message = "Password cannot be left blank !")
    private String password;
}
