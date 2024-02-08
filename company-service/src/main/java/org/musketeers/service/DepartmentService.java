package org.musketeers.service;

import org.musketeers.repository.DepartmentRepository;
import org.musketeers.repository.entity.Department;
import org.musketeers.utility.ServiceManager;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class DepartmentService extends ServiceManager<Department, Long> {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        super(departmentRepository);
        this.departmentRepository = departmentRepository;
    }

    public ResponseEntity<Boolean> saveDepartment(Department department) {
        save(department);
        return ResponseEntity.ok(true);
    }
}
