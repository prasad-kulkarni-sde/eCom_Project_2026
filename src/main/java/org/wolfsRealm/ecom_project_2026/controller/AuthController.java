package org.wolfsRealm.ecom_project_2026.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wolfsRealm.ecom_project_2026.security.jwt.JwtUtils;
import org.wolfsRealm.ecom_project_2026.security.dto.LoginRequest;
import org.wolfsRealm.ecom_project_2026.security.dto.LoginResponse;
import org.wolfsRealm.ecom_project_2026.security.services.UserDetailsImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    public JwtUtils jwtUtils;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication;
        try{
            authentication= authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));

        }catch (AuthenticationException e){
            Map<String,Object> map= new HashMap<>();
            map.put("message","Bad credentials");
            map.put("status",false);

            return new ResponseEntity<Object>(map, HttpStatus.UNAUTHORIZED);
        }

        //SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetailsImpl= (UserDetailsImpl) authentication.getPrincipal();
        String jwtToken= jwtUtils.generateTokenFromUsername(userDetailsImpl);
        List<String> roles= userDetailsImpl.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        LoginResponse loginResponse= new LoginResponse(userDetailsImpl.getId(), userDetailsImpl.getUsername(),jwtToken,roles);
        return  ResponseEntity.ok(loginResponse);
    }
}
