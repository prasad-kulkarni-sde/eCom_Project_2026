package org.wolfsRealm.ecom_project_2026.service;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import org.wolfsRealm.ecom_project_2026.payload.ProductDTO;
import org.wolfsRealm.ecom_project_2026.payload.ProductResponse;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);

    ProductResponse getProduct(Integer pageNumber,Integer pageSize,String sortBy, String sortOrder);

    ProductResponse getProductByCategory(Long categoryId,Integer pageNumber,Integer pageSize,String sortBy, String sortOrder);

    ProductResponse getProductByKeyword(String keyword,Integer pageNumber,Integer pageSize,String sortBy, String sortOrder);

    ProductDTO updateProduct(@Valid ProductDTO productDTO, Long productId);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
