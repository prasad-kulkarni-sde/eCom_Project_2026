package org.wolfsRealm.ecom_project_2026.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wolfsRealm.ecom_project_2026.model.category;

public interface CategoryRepository extends JpaRepository<category,Long> {
    category findByCategoryName(String categoryName);
}
