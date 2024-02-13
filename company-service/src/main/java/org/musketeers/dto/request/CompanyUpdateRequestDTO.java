package org.musketeers.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CompanyUpdateRequestDTO {

    private String token;

    private String establishmentDate;

    private String companyLogo;

    private String address;

    private List<HRInfoDto> hrInfos;

    private List<DepartmentDto> departments;

    private List<HolidayDto> holidays;

    private List<IncomeDto> incomes;

    private List<ExpenseDto> expenses;

}
