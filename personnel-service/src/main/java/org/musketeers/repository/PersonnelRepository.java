package org.musketeers.repository;

import org.musketeers.entity.Personnel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonnelRepository extends MongoRepository<Personnel, String> {

}
