package org.wolfsRealm.ecom_project_2026.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wolfsRealm.ecom_project_2026.model.Category;
import org.wolfsRealm.ecom_project_2026.model.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {


    boolean existsByProductName(String productName);

    Page<Product> findByCategoryOrderByProductId(Category category, Pageable pageDetails);

    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageable);

}
