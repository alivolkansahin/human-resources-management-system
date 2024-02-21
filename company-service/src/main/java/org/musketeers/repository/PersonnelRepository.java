package org.musketeers.repository;

import org.musketeers.repository.entity.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonnelRepository extends JpaRepository<Personnel,String> {
    Optional<Personnel> findByPersonnelId(String personnelId);

}
