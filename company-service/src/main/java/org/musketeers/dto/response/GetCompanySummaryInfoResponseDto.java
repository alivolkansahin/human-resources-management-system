package org.musketeers.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetCompanySummaryInfoResponseDto {

    private String id;

    private String name;

    private String logo;

    private String establishmentDate;

}
