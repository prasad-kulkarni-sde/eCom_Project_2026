package org.wolfsRealm.ecom_project_2026.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.wolfsRealm.ecom_project_2026.exceptions.APIException;
import org.wolfsRealm.ecom_project_2026.exceptions.ResourceNotFoundException;
import org.wolfsRealm.ecom_project_2026.model.Category;
import org.wolfsRealm.ecom_project_2026.payload.CategoryDTO;
import org.wolfsRealm.ecom_project_2026.payload.CategoryResponse;
import org.wolfsRealm.ecom_project_2026.repositories.CategoryRepository;

import java.util.*;

@Service

public class categoryServiceImplementation implements categoryService{

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private  ModelMapper modelMapper;



    @Override
    public CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder= sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categoryPage= categoryRepository.findAll(pageDetails);

        List<Category>categories=categoryPage.getContent();

        if (categories.isEmpty())throw new APIException("No Category Found!!!");
        List<CategoryDTO> categoryDTOS= categories.stream().map
                (category -> modelMapper.map(category,CategoryDTO.class)).toList();

        CategoryResponse categoryResponse= new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);

        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setLast(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category= modelMapper.map(categoryDTO,Category.class);

        Category savedCategory1= categoryRepository.findByCategoryName(category.getCategoryName());

        if(savedCategory1!=null) throw new APIException("Category with name "+category.getCategoryName()+" already exists !!!");

        Category savedCategory= categoryRepository.save(category);

        return modelMapper.map(savedCategory,CategoryDTO.class);


    }

    @Override
    public CategoryDTO deleteCategory(Long  categoryId) {

         Category category= categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","Category Id",categoryId));
         modelMapper.map(category,CategoryDTO.class);
         categoryRepository.delete(category);
        return modelMapper.map(category,CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO,Long categoryId) {



        Category category= modelMapper.map(categoryDTO, org.wolfsRealm.ecom_project_2026.model.Category.class);
        Category savedCategory1= categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","Category Id",categoryId));

        Category savedCategory= categoryRepository.findByCategoryName(category.getCategoryName());
        if (savedCategory!=null)throw new APIException("Category with name "+category.getCategoryName()+" already exists !!!");

        category.setCategoryId(categoryId);
        savedCategory1= categoryRepository.save(category);
        return  modelMapper.map(savedCategory1,CategoryDTO.class);


    }
}
