package org.musketeers.service;

import org.musketeers.dto.request.AddDepartmentRequestDto;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.rabbitmq.model.CreatePersonnelCompanyModel;
import org.musketeers.repository.DepartmentRepository;
import org.musketeers.repository.entity.Company;
import org.musketeers.repository.entity.Department;
import org.musketeers.repository.entity.Personnel;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService extends ServiceManager<Department, String> {
    private final DepartmentRepository departmentRepository;

    private final CompanyService companyService;

    private final ExpenseService expenseService;

    public DepartmentService(DepartmentRepository departmentRepository, CompanyService companyService, ExpenseService expenseService) {
        super(departmentRepository);
        this.departmentRepository = departmentRepository;
        this.companyService = companyService;
        this.expenseService = expenseService;
    }

    public Boolean saveDepartment(AddDepartmentRequestDto dto) {
        Company company = companyService.findById(dto.getCompanyId()).orElseThrow(() -> new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND));
        Department department = Department.builder()
                .company(company)
                .name(dto.getName())
                .shiftHour(dto.getShiftHour())
                .breakHour(dto.getBreakHour())
                .personnel(dto.getPersonnels())
                .build();
        save(department);
        return true;
    }

    public void addPersonnelToDepartment(CreatePersonnelCompanyModel model) {
        Department department = findById(model.getDepartmentId()).orElseThrow(() -> new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND));// DEPARTMENT NOT FOUND olabilir.
        Personnel personnel = Personnel.builder()
                .department(department)
                .personnelId(model.getPersonnelId())
                .build();
        department.getPersonnel().add(personnel);
        update(department);
        expenseService.addPersonnelSalaryExpense(model);
    }

}
