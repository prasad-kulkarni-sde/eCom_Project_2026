package org.wolfsRealm.ecom_project_2026.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wolfsRealm.ecom_project_2026.model.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByCategoryCategoryId(Long categoryId);

    List<Product> findByProductNameLikeIgnoreCase(String s);

    boolean existsByProductName(String productName);
}
