package org.musketeers.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.musketeers.repository.entity.EmployeeId;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddDepartmentRequestDto {
    private String companyId;
    private String name;
    private String shifts;
    private String breaks;
    private List<EmployeeId> employeeIds;
}
