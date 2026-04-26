package org.wolfsRealm.ecom_project_2026.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="Categories")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Category Name should Not be left blank !!!")
    @Size(min = 5, message = "Category Name should consist at least 5 Characters !!!")
    private String categoryName;

}
