package org.musketeers.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.musketeers.repository.entity.Personnel;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddDepartmentRequestDto {
    private String companyId;
    private String name;
    private String shiftHour;
    private String breakHour;
    private List<Personnel> personnels;
}
