package org.musketeers.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SpendingGetAllMyRequestsResponseDto {

    private String id;

    private String reason;

    private String description;

    private Double amount;

    private String currency;

    private LocalDate spendingDate;

    private List<String> attachments;

    private String requestStatus;

    private LocalDate createdAt;

    private LocalDate updatedAt;

}
