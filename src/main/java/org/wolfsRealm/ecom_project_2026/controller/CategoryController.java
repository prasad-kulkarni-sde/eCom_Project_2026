package org.wolfsRealm.ecom_project_2026.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wolfsRealm.ecom_project_2026.config.AppConstants;
import org.wolfsRealm.ecom_project_2026.payload.CategoryDTO;
import org.wolfsRealm.ecom_project_2026.payload.CategoryResponse;
import org.wolfsRealm.ecom_project_2026.service.CategoryService;


@RestController

public class CategoryController {


    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("api/echo")
    public ResponseEntity<String> echoMessage(@RequestParam(name="message",required = false)String message){
        return ResponseEntity.ok("Echoed Message : "+message);
    }

    @Tag(name=" Category APIs",description = "APIs for managing categories")
    @Operation(summary = "Get ALL Category", description = "API to get all category")
    @GetMapping("api/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber,
            @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
            @RequestParam(name="sortBy",defaultValue = AppConstants.CATEGORY_SORT_BY,required=false)String sortBy,
            @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false)String sortOrder
    ){
        CategoryResponse categoryResponse= categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categoryResponse,HttpStatus.OK);
    }

    @Tag(name=" Category APIs",description = "APIs for managing categories")
    @Operation(summary = "Create Category", description = "API to create a new category")
    @ApiResponses({
            @ApiResponse(responseCode = "201",description = "Category is created successfully"),
            @ApiResponse(responseCode = "500",description = "Internal Server Error",content = @Content),
            @ApiResponse(responseCode = "400",description = "Invalid Input",content = @Content)
    })
    @PostMapping("api/public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO ){
        return new ResponseEntity<>(categoryService.createCategory(categoryDTO),HttpStatus.CREATED);
    }

    @Tag(name=" Category APIs",description = "APIs for managing categories")
    @Operation(summary = "Delete Category", description = "API to Delete a category")
    @DeleteMapping("api/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@Parameter(description = "Enter Id of Category that you wish to delete") @PathVariable Long categoryId){
        return ResponseEntity.ok(categoryService.deleteCategory(categoryId));
    }

    @Tag(name=" Category APIs",description = "APIs for managing categories")
    @Operation(summary = "Update Category", description = "API to update existing category")
    @PutMapping("api/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId){
        return ResponseEntity.ok(categoryService.updateCategory(categoryDTO,categoryId));

    }
}
