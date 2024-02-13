package org.musketeers.repository;

import org.musketeers.repository.entity.EmployeeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeIdRepository extends JpaRepository<EmployeeId,String> {
}
