package org.musketeers.service;

import org.musketeers.repository.DepartmentRepository;
import org.musketeers.repository.entity.Department;
import org.musketeers.utility.ServiceManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DepartmentService extends ServiceManager<Department, String> {
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
