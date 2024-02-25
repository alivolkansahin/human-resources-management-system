package org.musketeers.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DayOffCreateRequestDto {

    private String token;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

}
