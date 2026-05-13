package org.wolfsRealm.ecom_project_2026.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wolfsRealm.ecom_project_2026.model.AppRole;
import org.wolfsRealm.ecom_project_2026.model.Role;
import org.wolfsRealm.ecom_project_2026.model.User;
import org.wolfsRealm.ecom_project_2026.repositories.RoleRepository;
import org.wolfsRealm.ecom_project_2026.repositories.UserRepository;
import org.wolfsRealm.ecom_project_2026.security.jwt.JwtUtils;
import org.wolfsRealm.ecom_project_2026.security.request.LoginRequest;
import org.wolfsRealm.ecom_project_2026.security.request.SignupRequest;
import org.wolfsRealm.ecom_project_2026.security.response.LoginResponse;
import org.wolfsRealm.ecom_project_2026.security.response.MessageResponse;
import org.wolfsRealm.ecom_project_2026.security.services.UserDetailsImpl;

import java.text.BreakIterator;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    public JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;


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

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
        if (userRepository.existsByUserName(signupRequest.getUsername())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken !!!"));
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email ID already exists !!!"));
        }
        User user= new User(signupRequest.getUsername(), signupRequest.getEmail(), passwordEncoder.encode(signupRequest.getPassword()));

        Set<String>setRoles= signupRequest.getRoles();
        Set<Role> roles= new HashSet<>();

        if(setRoles.isEmpty()){
            Role userRole=roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(()->(new RuntimeException("Error: Role Not FOUND !!")));
            roles.add(userRole);
        }
        else{
            setRoles.forEach(role->{
                switch (role.toLowerCase()){
                    case "admin":
                        Role adminRole= roleRepository.findByRoleName(AppRole.ROLE_ADMIN).orElseThrow(()->(new RuntimeException("Error: Role Not FOUND !!")));
                        roles.add(adminRole);
                        break;
                    case "seller":
                        Role sellerRole= roleRepository.findByRoleName(AppRole.ROLE_SELLER).orElseThrow(()->(new RuntimeException("Error: Role Not FOUND !!")));
                                   roles.add(sellerRole);
                                   break;
                    default :
                        Role userRole= roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(()->(new  RuntimeException("Error: Role Not FOUND !!")));
                                   roles.add(userRole);
                                   break;

                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return new ResponseEntity<>(new MessageResponse("User Registered Successfully !!"),HttpStatus.ACCEPTED);

    }
}
