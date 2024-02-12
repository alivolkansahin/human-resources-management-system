package org.musketeers.repository;



import org.musketeers.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IAuthRepository extends JpaRepository<Auth, String> {

    Boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<Auth> findOptionalByEmailOrPhone(String identity1,String identity2);


}
