package org.musketeers.repository;

import org.musketeers.entity.Advance;
import org.musketeers.entity.enums.ERequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvanceRepository extends JpaRepository<Advance, String> {

    List<Advance> findAllByPersonnelIdAndRequestStatus(String personnelId, ERequestStatus status);

    List<Advance> findAllByCompanyId(String companyId);

    List<Advance> findAllByPersonnelId(String personnelId);

}
