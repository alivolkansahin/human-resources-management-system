package org.musketeers.repository;

import org.musketeers.repository.entity.SupervisorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupervisorIdRepository extends JpaRepository<SupervisorId,String> {
}
