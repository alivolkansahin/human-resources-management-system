package org.musketeers.repository;

import org.musketeers.entity.Supervisor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupervisorRepository extends MongoRepository<Supervisor, String> {

    Optional<Supervisor> findOptionalByAuthId(String authId);

}
