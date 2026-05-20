package org.wolfsRealm.ecom_project_2026.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wolfsRealm.ecom_project_2026.model.AppRole;
import org.wolfsRealm.ecom_project_2026.model.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByRoleName(AppRole appRole);
}
