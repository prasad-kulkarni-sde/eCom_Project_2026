package org.wolfsRealm.ecom_project_2026.service;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.wolfsRealm.ecom_project_2026.exceptions.APIException;
import org.wolfsRealm.ecom_project_2026.exceptions.ResourceNotFoundException;
import org.wolfsRealm.ecom_project_2026.model.Cart;
import org.wolfsRealm.ecom_project_2026.model.CartItem;
import org.wolfsRealm.ecom_project_2026.model.Product;
import org.wolfsRealm.ecom_project_2026.payload.CartDTO;
import org.wolfsRealm.ecom_project_2026.payload.CartResponse;

import org.wolfsRealm.ecom_project_2026.payload.ProductDTO;
import org.wolfsRealm.ecom_project_2026.repositories.CartItemRepository;
import org.wolfsRealm.ecom_project_2026.repositories.CartRepository;
import org.wolfsRealm.ecom_project_2026.repositories.ProductRepository;
import org.wolfsRealm.ecom_project_2026.util.AuthUtil;

import java.util.List;
import java.util.stream.Stream;


@Service
public class CartServiceImpl implements CartService{

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CartItemRepository cartItemRepository;

    @Override
    public CartDTO addProductsToCart(Long productId, Integer quantity) {
            Cart cart= createCart();
            Product product= productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product With Product ID "+productId+" does Not Exists !!"));

           if (product.getQuantity()<quantity)throw new APIException("Entered Product Quantity Must Be Lower Than Or Equal To Available Quantity !!");

           CartItem cartItem= cartItemRepository.findCartItemByProductIdAndCartId(productId,cart.getCartId());
           List<CartItem>cartItemsList= cart.getCartItems();

           if (cartItem!=null) cartItem.setQuantity(cartItem.getQuantity()+quantity);
           else{
               cartItem= new CartItem();
               cartItem.setCart(cart);
               cartItem.setProduct(product);
               cartItem.setQuantity(quantity);
               cartItem.setDiscount(product.getDiscount());
               cartItem.setProductPrice(product.getSpecialPrice());
               cartItemsList.add(cartItem);
               cart.setCartItems(cartItemsList);
           }

           cartItemRepository.save(cartItem);
           cart.setTotalPrice(cart.getTotalPrice()+(product.getSpecialPrice()*quantity));
           cartRepository.save(cart);

           CartDTO cartDTO= modelMapper.map(cart,CartDTO.class);

        Stream<ProductDTO> productDTOStream= cartItemsList.stream().map(item->{ProductDTO map= modelMapper.map(
                item.getProduct(),ProductDTO.class);
                map.setQuantity(item.getQuantity());
                return map;
        });
        cartDTO.setProducts(productDTOStream.toList());
        cartDTO.setTotalPrice(cart.getTotalPrice());


        return cartDTO;




    }

    @Override
    public CartResponse getAllCarts(Integer pageNumber,Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder= sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);

        Page<Cart> cartPage= cartRepository.findAll(pageDetails);





        List<Cart>carts= cartPage.getContent();


        if (carts.isEmpty())throw new ResourceNotFoundException("Cart Not Found!!");

        List<CartDTO>cartDTOS=carts.stream().map(cart->{
            CartDTO cartDTO= modelMapper.map(cart,CartDTO.class);
            List<ProductDTO> productDTOS = cart.getCartItems().stream()
                    .map(cartItem -> {
                        Product product = cartItem.getProduct();
                        product.setQuantity(cartItem.getQuantity());

                        return modelMapper.map(product, ProductDTO.class);
                    })
                    .toList();
            cartDTO.setProducts(productDTOS);
            return cartDTO;

        }).toList();




        return new CartResponse(cartDTOS, cartPage.getNumber(), cartPage.getSize(), cartPage.getTotalElements(), cartPage.getTotalPages(), cartPage.isLast());

    }

    @Override
    public CartDTO getCartByEmailAndCartId(String email, Long cartId) {
         Cart cart= cartRepository.findCartByEmailAndCartId(email,cartId);
         if (cart==null)throw new ResourceNotFoundException("Cart","cartId",cartId);

         CartDTO cartDTO= modelMapper.map(cart,CartDTO.class);

         cart.getCartItems().forEach(c->c.getProduct().setQuantity(c.getQuantity()));

         List<ProductDTO>productDTOS= cart.getCartItems().stream()
                 .map(p->modelMapper.map(p.getProduct(),ProductDTO.class)).toList();
         cartDTO.setProducts(productDTOS);
         return cartDTO;

    }

    @Override
    @Transactional
    public CartDTO updateCartProductQuantity(Long productId, int delete) {
        Cart userCart= cartRepository.findCartByEmail(authUtil.loggedInEmail());

        if (userCart==null)throw new ResourceNotFoundException("Cart Does Not Exists!!");

        Product product= productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("productId","Product",productId));

        CartItem cartItem= cartItemRepository.findCartItemByProductIdAndCartId(productId, userCart.getCartId());
        if (cartItem==null)throw new ResourceNotFoundException("CartItem Does Not Exists!!");




        if (product.getQuantity()<(cartItem.getQuantity()+delete))throw new APIException("Selected Quantity must be smaller than or equal to available quantity !!");

        cartItem.setQuantity(cartItem.getQuantity()+delete);


        cartItem.setProductPrice(product.getSpecialPrice());
        cartItem.setDiscount(product.getDiscount());
        userCart.setTotalPrice(userCart.getTotalPrice()+(cartItem.getProductPrice()*delete));




        cartRepository.save(userCart);
        cartItemRepository.save(cartItem);

        if (cartItem.getQuantity()==0){
            cartItemRepository.deleteCartItemByProductIdAndCartId(productId, userCart.getCartId());
        }



        CartDTO cartDTO= modelMapper.map(userCart,CartDTO.class);

        List<CartItem>cartItems= userCart.getCartItems();

        Stream<ProductDTO> productDTOStream=cartItems.stream().map(item->{
            ProductDTO productDTO= modelMapper.map(item.getProduct(),ProductDTO.class);
            productDTO.setQuantity(item.getQuantity());
            return productDTO;
        });

        cartDTO.setProducts(productDTOStream.toList());

        return cartDTO;

    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(()->new ResourceNotFoundException("cartId","Cart",cartId));
        Product product= productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("productId","Product",productId));

        CartItem cartItem= cartItemRepository.findCartItemByProductIdAndCartId(productId,cartId);
        if (cartItem==null)throw new ResourceNotFoundException("CartItem Does Not Exist !!");

        cart.setTotalPrice(cart.getTotalPrice()-(cartItem.getProductPrice()*cartItem.getQuantity()));
        cartItemRepository.deleteCartItemByProductIdAndCartId(productId,cartId);

        return "Product "+cartItem.getProduct().getProductName()+" Deleted Successfully From The Cart!!";
    }

    @Override
    public void updateProductsInCarts(Long cartId, Long productId) {
        Product product= productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("productId","Product",productId));

         Cart cart= cartRepository.findById(cartId).orElseThrow(()-> new ResourceNotFoundException("Cart","cartId",cartId));

        CartItem cartItem= cartItemRepository.findCartItemByProductIdAndCartId(productId, cartId);
        if (cartItem==null)throw new APIException("Product "+product.getProductName()+" not available");

        double cartPrice= cart.getTotalPrice()-(cartItem.getProductPrice()*cartItem.getQuantity());

        cartItem.setProductPrice(product.getSpecialPrice());

        cart.setTotalPrice(cartPrice+cartItem.getProductPrice()*cartItem.getQuantity());

        cartItem= cartItemRepository.save(cartItem);
    }


    public Cart createCart(){
        Cart userCart= cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(userCart!=null)return userCart;

        Cart cart= new Cart();

        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        cartRepository.save(cart);
        return cart;

    }
}
