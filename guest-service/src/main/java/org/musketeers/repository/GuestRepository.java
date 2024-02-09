package org.musketeers.repository;

import org.musketeers.entity.Guest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestRepository extends MongoRepository<Guest, String> {

    Optional<Guest> findOptionalByAuthId(String authId);

}
