package org.musketeers.dto.request;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AdminSupervisorRegistrationDecisionRequestDto {

    //Annotation kontrolleri sonradan eklenecek...
    private String token;

    private String supervisorAuthId;

    private Boolean decision;

}
