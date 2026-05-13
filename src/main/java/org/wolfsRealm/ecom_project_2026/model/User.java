package org.wolfsRealm.ecom_project_2026.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @NotBlank
    @Size(max=15)
    @Column(name="userName",unique = true)
    private String userName;

    @NotBlank
    @Size(max=50)
    @Email
    @Column(name= "Email", unique = true)
    private String Email;

    @NotBlank
    @Size(max=120)
    @Column(name = "password")
    private String password;

    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST},fetch = FetchType.EAGER)
    @JoinTable(
        name="user_role",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role>roles= new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user",
               cascade = {CascadeType.MERGE,CascadeType.PERSIST},
               orphanRemoval = true)
    private Set<Product>products;

    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST},fetch = FetchType.LAZY)
    @JoinTable(name = "user_address",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "address_id"))
    private List<Address>addresses= new ArrayList<>();



}
