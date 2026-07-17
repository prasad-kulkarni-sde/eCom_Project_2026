package org.wolfsRealm.ecom_project_2026.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wolfsRealm.ecom_project_2026.config.AppConstants;
import org.wolfsRealm.ecom_project_2026.exceptions.ResourceNotFoundException;
import org.wolfsRealm.ecom_project_2026.model.Cart;
import org.wolfsRealm.ecom_project_2026.payload.CartDTO;
import org.wolfsRealm.ecom_project_2026.payload.CartResponse;
import org.wolfsRealm.ecom_project_2026.repositories.CartRepository;
import org.wolfsRealm.ecom_project_2026.service.CartService;
import org.wolfsRealm.ecom_project_2026.util.AuthUtil;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    CartRepository cartRepository;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO>addProductsToCart(@PathVariable Long productId, @PathVariable Integer quantity){
        CartDTO cartDTO= cartService.addProductsToCart(productId,quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.ACCEPTED);
    }

    @GetMapping("/carts")
    public ResponseEntity<CartResponse> getAllCarts(
            @RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber,
            @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
            @RequestParam(name="sortBy",defaultValue = AppConstants.CART_SORT_BY,required=false)String sortBy,
            @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false)String sortOrder
    ){
        return new ResponseEntity<>(cartService.getAllCarts(pageNumber, pageSize,  sortBy, sortOrder),HttpStatus.OK);
    }

    @GetMapping("/carts/user/cart")
    public ResponseEntity<CartDTO> GetCartByEmailAndCartId(){
        String email= authUtil.loggedInEmail();
        Cart cart= cartRepository.findCartByEmail(email);
        if (cart==null)throw new ResourceNotFoundException("You Do Not Have Products In Cart. Add Products To See Them Here");
        Long cartId=cart.getCartId();
        return new ResponseEntity<>(cartService.getCartByEmailAndCartId(email, cartId),HttpStatus.FOUND);
    }
    @PutMapping("/carts/products/{productId}/quantity/{operations}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId, @PathVariable String operations){
        CartDTO cartDTO= cartService.updateCartProductQuantity(productId,operations.equalsIgnoreCase("delete")?-1:1);
        return new ResponseEntity<>(cartDTO, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String>deleteProductFromCart(@PathVariable Long cartId, @PathVariable Long productId){
        return new ResponseEntity<>(cartService.deleteProductFromCart(cartId,productId),HttpStatus.ACCEPTED);
    }
}
