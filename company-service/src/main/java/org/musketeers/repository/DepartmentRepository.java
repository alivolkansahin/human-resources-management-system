package org.musketeers.repository;

import org.musketeers.repository.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    Optional<Department> findOptionalByPersonnelId(String personnelId);

}
