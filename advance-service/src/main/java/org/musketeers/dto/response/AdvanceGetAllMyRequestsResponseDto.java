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
public class AdvanceGetAllMyRequestsResponseDto {

    private String id;

    private String description;

    private Double amount;

    private String requestStatus;

    private LocalDate createdAt;

    private LocalDate updatedAt;

}
