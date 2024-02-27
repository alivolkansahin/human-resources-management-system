package org.musketeers.repository;

import org.musketeers.entity.Spending;
import org.musketeers.entity.enums.ERequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpendingRepository extends JpaRepository<Spending, String> {

    List<Spending> findAllByPersonnelIdAndRequestStatus(String personnelId, ERequestStatus eRequestStatus);

    List<Spending> findAllByCompanyId(String companyId);

    List<Spending> findAllByPersonnelId(String personnelId);

}
