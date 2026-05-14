package org.wolfsRealm.ecom_project_2026.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wolfsRealm.ecom_project_2026.model.Role;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SignupRequest {
    @NotBlank
    @Size(min=3,max=20, message = "Username must at least contain 3 and at most 20 characters ")
    private String username;

    @NotBlank
    @Size(max=50,message = "Email ID must at most contain 50 characters")
    @Email
    private String email;

    @NotBlank
    @Size(min=8,message = "Password must at least contain 8 characters")
    private String password;
    private Set<String>roles;

}
