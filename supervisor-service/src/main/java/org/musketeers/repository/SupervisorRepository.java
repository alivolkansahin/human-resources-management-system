package org.musketeers.repository;

import org.musketeers.entity.Supervisor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupervisorRepository extends MongoRepository<Supervisor, String> {

}
