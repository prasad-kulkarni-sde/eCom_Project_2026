package org.wolfsRealm.ecom_project_2026.security.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    public Long Id;
    public String username;
    public String jwtToken;
    public List<String> roles;

}
