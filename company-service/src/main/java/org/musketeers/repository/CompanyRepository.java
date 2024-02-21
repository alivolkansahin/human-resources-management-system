package org.musketeers.repository;

import org.musketeers.repository.entity.Company;
import org.musketeers.repository.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CompanyRepository extends JpaRepository<Company, String > {
    Optional<Company> findOptionalByCompanyName(String companyName);

    Optional<Company> findOptionalById(String companyId);
    void deleteByCompanyName(String companyName);
//    @Query(value = "SELECT c.* FROM tbl_companies AS c INNER JOIN tbl_departments AS d ON c.department_id = d.id WHERE d.id = ?1", nativeQuery = true)
//    Optional<Company> findCompanyByDepartment(String departmentId);

    List<Company> findAllByCompanyNameLikeIgnoreCase(String companyNameLike);
}
