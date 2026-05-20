package org.wolfsRealm.ecom_project_2026.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long cartItemID;
    private CartDTO cart;
    private ProductDTO productDTO;

    private Integer quantity;
    private Double discount;
    private Double productPrice;
}
