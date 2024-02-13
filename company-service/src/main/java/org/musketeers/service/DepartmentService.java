package org.musketeers.service;

import org.musketeers.dto.request.AddDepartmentRequestDto;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.repository.DepartmentRepository;
import org.musketeers.repository.entity.Company;
import org.musketeers.repository.entity.Department;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService extends ServiceManager<Department, String> {
    private final DepartmentRepository departmentRepository;

    private final CompanyService companyService;

    public DepartmentService(DepartmentRepository departmentRepository, CompanyService companyService) {
        super(departmentRepository);
        this.departmentRepository = departmentRepository;
        this.companyService = companyService;
    }

    public Boolean saveDepartment(AddDepartmentRequestDto dto) {
        Company company = companyService.findById(dto.getCompanyId()).orElseThrow(() -> new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND));
        Department department = Department.builder()
                .company(company)
                .name(dto.getName())
                .shifts(dto.getShifts())
                .breaks(dto.getBreaks())
                .personnel(dto.getPersonnels())
                .build();
        save(department);
        return true;
    }
}
