package org.wolfsRealm.ecom_project_2026.service;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import org.wolfsRealm.ecom_project_2026.model.Product;
import org.wolfsRealm.ecom_project_2026.payload.ProductDTO;
import org.wolfsRealm.ecom_project_2026.payload.ProductResponse;

import java.io.IOException;

public interface productService {
    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);

    ProductResponse getProduct();

    ProductResponse getProductByCategory(Long categoryId);

    ProductResponse getProductByKeyword(String keyword);

    ProductDTO updateProduct(@Valid ProductDTO productDTO, Long productId);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
