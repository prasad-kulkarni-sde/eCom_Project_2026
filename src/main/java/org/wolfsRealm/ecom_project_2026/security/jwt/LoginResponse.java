package org.wolfsRealm.ecom_project_2026.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    public String username;
    public String jwtToken;
    public List<String> roles;

}
