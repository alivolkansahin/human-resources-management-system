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
public class DayOffGetAllRequestsResponseDto {

    private String id;

    private String personnelId;

    private String name;

    private String lastName;

    private String image;

    private String email;

    private String dayOff;

    private String reason;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private String requestStatus;

    private LocalDate createdAt;

    private LocalDate updatedAt;

}
