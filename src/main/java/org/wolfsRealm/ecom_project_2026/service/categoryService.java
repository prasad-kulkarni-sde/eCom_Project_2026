package org.wolfsRealm.ecom_project_2026.service;


import org.wolfsRealm.ecom_project_2026.payload.CategoryDTO;
import org.wolfsRealm.ecom_project_2026.payload.CategoryResponse;

import java.util.List;

public interface categoryService {
    CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy, String sortOrder);
    CategoryDTO createCategory(CategoryDTO category);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}
