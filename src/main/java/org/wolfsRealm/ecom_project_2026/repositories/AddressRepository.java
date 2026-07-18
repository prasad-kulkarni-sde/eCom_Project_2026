package org.wolfsRealm.ecom_project_2026.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.wolfsRealm.ecom_project_2026.model.Address;


public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("SELECT a FROM Address a WHERE a.user.userId = ?1")
    Page<Address> findByUserId(Long userId, Pageable pageDetails);
}
