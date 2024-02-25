package org.musketeers.repository;

import org.musketeers.entity.DayOff;
import org.musketeers.entity.enums.ERequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DayOffRepository extends JpaRepository<DayOff, String> {

    List<DayOff> findAllByPersonnelIdAndRequestStatus(String personnelId, ERequestStatus status);

    List<DayOff> findAllByCompanyId(String companyId);

    List<DayOff> findAllByPersonnelId(String personnelId);

}
