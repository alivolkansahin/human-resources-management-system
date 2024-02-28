package org.musketeers.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CompanyUpdateRequestDTO {

    private String token;

    private LocalDate establishmentDate;

    private MultipartFile companyLogo;

    private String address;

    private List<HRInfoRequestDto> hrInfos;

    private List<DepartmentRequestDto> departments;

    private List<HolidayRequestDto> holidays;

    private List<IncomeRequestDto> incomes;

    private List<ExpenseRequestDto> expenses;

}
