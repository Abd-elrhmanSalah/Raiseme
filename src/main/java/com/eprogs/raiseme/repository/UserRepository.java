package com.eprogs.raiseme.repository;


import com.eprogs.raiseme.entity.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<SystemUser, Long> {

    Boolean existsByEmail(String email);

    Optional<SystemUser> findByEmail(String email);

    Optional<SystemUser> findByToken(String token);

}

