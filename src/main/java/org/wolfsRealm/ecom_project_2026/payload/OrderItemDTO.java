package org.wolfsRealm.ecom_project_2026.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderItemDTO {

    private Long orderItemId;
    private ProductDTO productDTO;
    private Integer quantity;
    private Double discount;
    private Double orderedProductPrice;

}
