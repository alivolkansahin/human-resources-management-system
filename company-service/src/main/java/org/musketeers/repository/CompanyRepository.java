package org.musketeers.repository;

import org.musketeers.repository.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findOptionalByCompanyName(String companyName);

    Optional<Company> findOptionalById(String companyId);
    void deleteByCompanyName(String companyName);
}
