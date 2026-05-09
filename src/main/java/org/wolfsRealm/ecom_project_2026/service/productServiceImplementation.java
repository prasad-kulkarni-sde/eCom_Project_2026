package org.wolfsRealm.ecom_project_2026.service;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wolfsRealm.ecom_project_2026.exceptions.ResourceNotFoundException;
import org.wolfsRealm.ecom_project_2026.model.Category;
import org.wolfsRealm.ecom_project_2026.model.Product;


import org.wolfsRealm.ecom_project_2026.payload.ProductDTO;
import org.wolfsRealm.ecom_project_2026.payload.ProductResponse;
import org.wolfsRealm.ecom_project_2026.repositories.CategoryRepository;
import org.wolfsRealm.ecom_project_2026.repositories.ProductRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;


import java.util.List;

@Service
class productServiceImplementation implements productService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductResponse getProduct() {


        ProductResponse productResponse= new ProductResponse();
        List<Product> products= productRepository.findAll();
        if (products.isEmpty())throw new ResourceNotFoundException("Product");


        List<ProductDTO>productDTOS= products.stream().map
                (product -> modelMapper.map(product, ProductDTO.class)).toList();

        productResponse.setContent(productDTOS);

        return productResponse;
    }

    @Override
    public ProductResponse getProductByCategory(Long categoryId) {
        Category category= categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("category","categoryId",categoryId));

        List<Product>list= productRepository.findByCategoryCategoryId(categoryId);
        if (list.isEmpty())throw new ResourceNotFoundException("Product","categoryId",categoryId);

        ProductResponse productResponse= new ProductResponse();
        List<ProductDTO>productDTOS= list.stream().map
                (product -> modelMapper.map(product, ProductDTO.class)).toList();

        productResponse.setContent(productDTOS);

        return productResponse;
    }

    @Override
    public ProductResponse getProductByKeyword(String keyword) {
        List<Product>products= productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%');
        if (products.isEmpty())throw new ResourceNotFoundException("Product","keyword",keyword);

        ProductResponse productResponse= new ProductResponse();
        List<ProductDTO>productDTOS= products.stream().map
                (product -> modelMapper.map(product, ProductDTO.class)).toList();

        productResponse.setContent(productDTOS);

        return productResponse;

    }



    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category= categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));

        Product product= modelMapper.map(productDTO,Product.class);

        product.setImage("default.png");


        product.setCategory(category);
        Double specialPrice= product.getPrice()- ((product.getDiscount()*0.01)*product.getPrice());
        product.setSpecialPrice(specialPrice);

        Product savedProduct= productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);

    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product currentProduct= productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));

        Product product= modelMapper.map(productDTO,Product.class);

        if(product.getProductName()!=null)currentProduct.setProductName(product.getProductName());
        if(product.getDiscount()!=0)currentProduct.setDiscount(product.getDiscount());
        if(product.getPrice()!=0)currentProduct.setPrice(product.getPrice());
        if(product.getCategory()!=null)currentProduct.setCategory(product.getCategory());
        if(product.getDescription()!=null)currentProduct.setDescription(product.getDescription());
        if(product.getQuantity()!=0)currentProduct.setQuantity(product.getQuantity());
        if(product.getSpecialPrice()!=0)currentProduct.setSpecialPrice(product.getSpecialPrice());

        productRepository.save(currentProduct);
        return modelMapper.map(currentProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product currentProduct= productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));

        String path= "image/";

        String fileName= uploadImage(path,image);


        currentProduct.setImage(fileName);

        productRepository.save(currentProduct);

        return modelMapper.map(currentProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product currentProduct= productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));

        productRepository.delete(currentProduct);
        return modelMapper.map(currentProduct,ProductDTO.class);
    }

    private String uploadImage(String path, MultipartFile file) throws IOException {
        String originalFileName= file.getOriginalFilename();

        String randomId= UUID.randomUUID().toString();

        String fileName= randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath= path+ File.separator+ fileName;

        File folder= new File(path);

        if (!folder.exists())folder.mkdir();

        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName;

    }
}
