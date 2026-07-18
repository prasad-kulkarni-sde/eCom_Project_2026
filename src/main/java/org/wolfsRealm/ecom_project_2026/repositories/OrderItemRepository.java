package org.wolfsRealm.ecom_project_2026.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wolfsRealm.ecom_project_2026.model.OrderItem;


@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
