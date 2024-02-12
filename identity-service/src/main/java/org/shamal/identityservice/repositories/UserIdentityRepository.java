package org.shamal.identityservice.repositories;

import org.shamal.identityservice.model.entities.UserIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserIdentityRepository extends JpaRepository<UserIdentity, Long> {
    UserIdentity findByUsername(String username);

    boolean existsByUsername(String username);
}
