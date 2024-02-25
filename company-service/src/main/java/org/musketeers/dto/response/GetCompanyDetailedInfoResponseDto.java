package org.musketeers.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetCompanyDetailedInfoResponseDto {

    private String companyName;

    private String establishmentDate;

    private String companyLogo;

    private String address;

    private List<HRInfoResponseDto> hrInfos;

    private List<String> departmentNames;

    private Integer personnelCount;

    private List<SupervisorInfoResponseDto> supervisors;

//    private List<CommentsDto> comments; // comment servis endpointinde var

}
