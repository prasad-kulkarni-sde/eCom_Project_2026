package org.wolfsRealm.ecom_project_2026.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private String description;
    private Long quantity;
    private Double price;
    private Double specialPrice;
    private Double discount;
    private String image;

    @ManyToOne
    @JoinColumn(name="categoryId")
    private Category category;

}
