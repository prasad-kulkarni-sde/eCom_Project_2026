package org.wolfsRealm.ecom_project_2026.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    @Schema(description = "Enter the Category ID for your intended purpose",example = "6969")
    private Long categoryId;
    @NotBlank(message = "Category Name should Not be left blank !!!")
    @Size(min = 5, message = "Category Name should consist at least 5 Characters !!!")
    @Column(unique = true)
    @Schema(description = "Add the Category Name for your intended purpose")
    private String categoryName;

}

