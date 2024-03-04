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
public class GetPersonnelDetailsResponseDto {

    private String name;

    private String lastName;

    private String image;

    private String email;

    private String companyId;

    private List<PhoneResponseDto> phones;

    private List<String> addresses;

    private String companyLogo;

    private String companyName;

    private DepartmentResponseDto department;

    private List<HolidayResponseDto> companyHolidays;

    private List<HRInfoResponseDto> hrInfos;

    private LocalDate dateOfBirth;

    private LocalDate dateOfEmployment;

    private Double salary;

    private Double dayOff;

    private Double advanceQuota;

}
