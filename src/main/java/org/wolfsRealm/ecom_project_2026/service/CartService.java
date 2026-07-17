package org.wolfsRealm.ecom_project_2026.service;

import jakarta.transaction.Transactional;
import org.wolfsRealm.ecom_project_2026.payload.CartDTO;
import org.wolfsRealm.ecom_project_2026.payload.CartResponse;

public interface CartService {
    CartDTO addProductsToCart(Long productId, Integer quantity);

    CartResponse getAllCarts(Integer pageNumber,Integer pageSize, String sortBy, String sortOrder);

    CartDTO getCartByEmailAndCartId(String email, Long cartId);

    @Transactional
    CartDTO updateCartProductQuantity(Long productId, int delete);

    String deleteProductFromCart(Long cartId, Long productId);

    void updateProductsInCarts(Long cartId, Long productId);
}
