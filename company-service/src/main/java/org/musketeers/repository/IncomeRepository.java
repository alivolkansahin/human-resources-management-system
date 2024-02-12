package org.musketeers.repository;

import org.musketeers.repository.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface IncomeRepository extends JpaRepository<Income, String> {
}
