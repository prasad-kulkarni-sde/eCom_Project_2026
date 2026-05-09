package org.wolfsRealm.ecom_project_2026.service;


import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wolfsRealm.ecom_project_2026.exceptions.APIException;
import org.wolfsRealm.ecom_project_2026.exceptions.ResourceNotFoundException;
import org.wolfsRealm.ecom_project_2026.model.Category;
import org.wolfsRealm.ecom_project_2026.model.Product;


import org.wolfsRealm.ecom_project_2026.payload.CategoryResponse;
import org.wolfsRealm.ecom_project_2026.payload.ProductDTO;
import org.wolfsRealm.ecom_project_2026.payload.ProductResponse;
import org.wolfsRealm.ecom_project_2026.repositories.CategoryRepository;
import org.wolfsRealm.ecom_project_2026.repositories.ProductRepository;


import java.io.IOException;


import java.util.List;

@Service
class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductResponse getProduct(Integer pageNumber,Integer pageSize,String sortBy, String sortOrder) {

        Sort sortByAndOrder= sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> productPage= productRepository.findAll(pageDetails);


        ProductResponse productResponse= new ProductResponse();
        List<Product> products= productRepository.findAll();
        if (products.isEmpty())throw new ResourceNotFoundException("Product");


        List<ProductDTO>productDTOS= products.stream().map
                (product -> modelMapper.map(product, ProductDTO.class)).toList();

        productResponse.setContent(productDTOS);

        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setLast(productPage.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getProductByCategory(Long categoryId,Integer pageNumber,Integer pageSize,String sortBy, String sortOrder) {
        Category category= categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("category","categoryId",categoryId));

        Sort sortByAndOrder= sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> productPage= productRepository.findAll(pageDetails);

        List<Product>list= productRepository.findByCategoryCategoryId(categoryId);
        if (list.isEmpty())throw new ResourceNotFoundException("Product","categoryId",categoryId);

        ProductResponse productResponse= new ProductResponse();
        List<ProductDTO>productDTOS= list.stream().map
                (product -> modelMapper.map(product, ProductDTO.class)).toList();

        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setLast(productPage.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getProductByKeyword(String keyword,Integer pageNumber,Integer pageSize,String sortBy, String sortOrder) {
        List<Product>products= productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%');
        if (products.isEmpty())throw new ResourceNotFoundException("Product","keyword",keyword);

        Sort sortByAndOrder= sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> productPage= productRepository.findAll(pageDetails);

        ProductResponse productResponse= new ProductResponse();
        List<ProductDTO>productDTOS= products.stream().map
                (product -> modelMapper.map(product, ProductDTO.class)).toList();

        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setLast(productPage.isLast());

        return productResponse;

    }



    @Override
    public ProductDTO addProduct(@Valid Long categoryId, ProductDTO productDTO) {
        Category category= categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));



        Product product= modelMapper.map(productDTO,Product.class);

        if (productRepository.existsByProductName(product.getProductName()))throw new APIException("Product Already Exists !!!");

        product.setImage("default.png");


        product.setCategory(category);
        Double specialPrice= product.getPrice()- ((product.getDiscount()*0.01)*product.getPrice());
        product.setSpecialPrice(specialPrice);

        Product savedProduct= productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);

    }

    @Override
    public ProductDTO updateProduct(@Valid ProductDTO productDTO, Long productId) {
        Product currentProduct= productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));

        Product product= modelMapper.map(productDTO,Product.class);
        if (productRepository.existsByProductName(product.getProductName()))throw new APIException("Product Already Exists !!!");

        if(product.getProductName()!=null)currentProduct.setProductName(product.getProductName());
        if(product.getDiscount()!=null)currentProduct.setDiscount(product.getDiscount());
        if(product.getPrice()!=null)currentProduct.setPrice(product.getPrice());
        if(product.getCategory()!=null)currentProduct.setCategory(product.getCategory());
        if(product.getDescription()!=null)currentProduct.setDescription(product.getDescription());
        if(product.getQuantity()!=null)currentProduct.setQuantity(product.getQuantity());
        currentProduct.setSpecialPrice(currentProduct.getPrice() - ((currentProduct.getDiscount() * 0.01) * currentProduct.getPrice()));


        productRepository.save(currentProduct);
        return modelMapper.map(currentProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product currentProduct= productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));


        String fileName= fileService.updateImage(path,image);


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
}
