package org.wolfsRealm.ecom_project_2026.payload;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    
     private Long cartId;

     private Double totalPrice=0.0;
     private List<ProductDTO> products= new ArrayList<>();
}
