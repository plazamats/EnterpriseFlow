package com.enterpriseflow.organisation;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganisationRepository extends JpaRepository<Organisation, UUID> {
    Optional<Organisation> findByNameIgnoreCase(String name);
}
