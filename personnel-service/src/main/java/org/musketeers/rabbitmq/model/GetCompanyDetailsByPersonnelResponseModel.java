package org.musketeers.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetCompanyDetailsByPersonnelResponseModel {

    private String companyName;

    private String companyLogo;

    private String departmentName;

    private String shifts;

    private String breaks;

    private List<String> holidays;

    private List<String> hrInfos;

}
