package org.musketeers.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.musketeers.repository.entity.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CompanyUpdateRequestDTO {
    private String token;
    private String companyName;
    private Address address;
    private List<HRInfo> hrInfos;
    private List<Department> departments;
    private List<Holiday> holidays;
    private List<Income> incomes;
    private List<Expense> expenses;
    private List<String> supervisorIds;
}
