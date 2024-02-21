package org.musketeers.repository;

import org.musketeers.repository.entity.Department;
import org.musketeers.repository.entity.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {

//    @Query(value = "SELECT d.* FROM tbl_departments AS d INNER JOIN tbl_personnel AS p ON d.personnel_id = p.id WHERE p.id = ?1", nativeQuery = true)
//    Optional<Department> findDepartmentByPersonnelId(String personnelId);

}
