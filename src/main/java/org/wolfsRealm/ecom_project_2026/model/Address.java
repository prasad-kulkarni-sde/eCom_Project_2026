package org.wolfsRealm.ecom_project_2026.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 3, message = "Building name must consist of at least 3 characters")
    private String buildingName;

    @NotBlank
    @Size(min = 3, message = "Street name must consist of at least 3 characters")
    private String streetName;

    @NotBlank
    @Size(min = 3, message = "city name must consist of at least 3 characters")
    private String city;

    @NotBlank
    @Size(min = 3, message = "state name must consist of at least 3 characters")
    private String state;

    @NotBlank
    @Size(min = 3, message = "country name must consist of at least 3 characters")
    private String country;

    @NotNull
    @Size(min = 6, message = "pin code must consist of at least 6 characters")
    private Integer pincode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users= new ArrayList<>();
}
