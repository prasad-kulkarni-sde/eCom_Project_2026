package org.wolfsRealm.ecom_project_2026.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.wolfsRealm.ecom_project_2026.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = ?2 AND ci.product.id = ?1")
    CartItem findCartItemByProductIdAndCartId(Long productId, Long cartId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id=?2 AND ci.product.id=?1")
    void deleteCartItemByProductIdAndCartId(Long productId, Long cartId);
}
