package org.wolfsRealm.ecom_project_2026.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wolfsRealm.ecom_project_2026.model.Product;
import org.wolfsRealm.ecom_project_2026.payload.ProductDTO;
import org.wolfsRealm.ecom_project_2026.payload.ProductResponse;
import org.wolfsRealm.ecom_project_2026.service.productService;

import java.io.IOException;

@RestController
@RequestMapping
public class productController {
    @Autowired
    private productService productService;

    public productController(productService productService){
        this.productService=productService;
    }

    @GetMapping("api/public/product")
    public ResponseEntity <ProductResponse>  getProduct(){
        return new ResponseEntity<>(productService.getProduct(),HttpStatus.OK);
    }

    @GetMapping("api/public/{categoryId}/product")
    public ResponseEntity<ProductResponse>getProductByCategory(@PathVariable Long categoryId){
        return new ResponseEntity<>(productService.getProductByCategory(categoryId),HttpStatus.OK);
    }

    @GetMapping("api/public/product/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable String keyword){
        ProductResponse productResponse= productService.getProductByKeyword(keyword);
        return new ResponseEntity<>(productResponse,HttpStatus.FOUND);
    }

    @PostMapping("api/admin/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long categoryId){
         return new ResponseEntity<>(productService.addProduct(categoryId,productDTO), HttpStatus.CREATED);
    }

    @PutMapping("api/admin/product/{productId}")
    public ResponseEntity<ProductDTO>updateProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long productId){
        return new ResponseEntity<>(productService.updateProduct(productDTO,productId),HttpStatus.ACCEPTED);
    }

    @PutMapping("api/admin/product/{productId}/image")
    public ResponseEntity<ProductDTO>updateProductImage(@PathVariable Long productId, @RequestParam("Image")MultipartFile image) throws IOException {
        return new ResponseEntity<>(productService.updateProductImage(productId,image),HttpStatus.OK);
    }

    @DeleteMapping("api/admin/product/{productId}")
    public ResponseEntity<ProductDTO>deleteProduct(@PathVariable Long productId){
        return new ResponseEntity<>(productService.deleteProduct(productId),HttpStatus.ACCEPTED);
    }
}
