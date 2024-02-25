package org.musketeers.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddHolidayRequestDto {

    private String companyId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
}
