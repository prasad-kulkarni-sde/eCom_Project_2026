package org.wolfsRealm.ecom_project_2026.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.wolfsRealm.ecom_project_2026.model.User;
import org.wolfsRealm.ecom_project_2026.repositories.UserRepository;

@Component
public class AuthUtil {
    @Autowired
    UserRepository userRepository;

    public String loggedInEmail(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserName(authentication.getName()).orElseThrow(()->new UsernameNotFoundException("User Not Found !!"));

        return user.getEmail();
    }

    public Long loggedInUserId(){

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserName(authentication.getName()).orElseThrow(()->new UsernameNotFoundException("User Not Found !!"));

        return user.getUserId();

    }

    public User loggedInUser(){

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByUserName(authentication.getName()).orElseThrow(()->new UsernameNotFoundException("User Not Found !!"));

    }
}
