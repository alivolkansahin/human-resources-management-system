package org.musketeers.repository;

import org.musketeers.repository.entity.HRInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface HRInfoRepository extends JpaRepository<HRInfo, String> {
}
