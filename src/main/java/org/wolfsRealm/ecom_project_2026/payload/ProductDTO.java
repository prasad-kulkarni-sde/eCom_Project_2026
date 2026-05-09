package org.wolfsRealm.ecom_project_2026.payload;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    @NotBlank(message = "Product Name should Not be left blank !!!")
    @Size(min = 3, message = "Product Name should consist at least 3 Characters !!!")
    @Column(unique = true)
    private String productName;

    @NotBlank(message = "Product Description should Not be left blank !!!")
    @Size(min = 6, message = "Product Description should consist at least 6 Characters !!!")
    private String description;
    private Long quantity;
    private Double price;
    private Double specialPrice;
    private Double discount;
    private String image;
}
