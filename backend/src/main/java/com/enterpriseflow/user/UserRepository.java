package com.enterpriseflow.user;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByOrganisationIdAndEmail(UUID organisationId, String email);
}
