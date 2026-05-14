package org.wolfsRealm.ecom_project_2026.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.wolfsRealm.ecom_project_2026.model.AppRole;
import org.wolfsRealm.ecom_project_2026.model.Role;
import org.wolfsRealm.ecom_project_2026.model.User;
import org.wolfsRealm.ecom_project_2026.repositories.RoleRepository;
import org.wolfsRealm.ecom_project_2026.repositories.UserRepository;
import org.wolfsRealm.ecom_project_2026.security.jwt.AuthEntryPointJwt;
import org.wolfsRealm.ecom_project_2026.security.jwt.AuthTokenFilter;
import org.wolfsRealm.ecom_project_2026.security.services.UserDetailsServicesImpl;

import java.util.Set;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    UserDetailsServicesImpl userDetailsServices;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    private AuthEntryPointJwt unAuthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider= new DaoAuthenticationProvider(userDetailsServices);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){

           return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers( "/h2-console/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
//                .requestMatchers("/api/public/**").permitAll()
//                .requestMatchers("/api/admin/**").permitAll()
                .requestMatchers("/api/test/**").permitAll()
                .requestMatchers("/images/**").permitAll()
                .anyRequest().authenticated());

        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.headers(headers->headers.frameOptions(frameoptions-> frameoptions.sameOrigin()));

//      http.formLogin(withDefaults());   // that default login page
//      http.httpBasic(withDefaults());

        http.authenticationProvider(authenticationProvider());




        http.exceptionHandling(exception -> exception.authenticationEntryPoint(unAuthorizedHandler));

        http.headers(headers -> headers.frameOptions
                (HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web -> web.ignoring().requestMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"
        ));
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Retrieve or create roles
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> {
                        Role newUserRole = new Role(AppRole.ROLE_USER);
                        return roleRepository.save(newUserRole);
                    });

            Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(() -> {
                        Role newSellerRole = new Role(AppRole.ROLE_SELLER);
                        return roleRepository.save(newSellerRole);
                    });

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
                        return roleRepository.save(newAdminRole);
                    });

            Set<Role> userRoles = Set.of(userRole);
            Set<Role> sellerRoles = Set.of(sellerRole);
            Set<Role> adminRoles = Set.of(userRole, sellerRole, adminRole);


            if (!userRepository.existsByUserName("user1")) {
                User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
                user1.setRoles(userRoles);
                userRepository.save(user1);
            }

            if (!userRepository.existsByUserName("seller1")) {
                User seller1 = new User("seller1", "seller1@example.com", passwordEncoder.encode("password2"));
                seller1.setRoles(sellerRoles);
                userRepository.save(seller1);
            }

            if (!userRepository.existsByUserName("admin")) {
                User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
                admin.setRoles(adminRoles);
                userRepository.save(admin);
            }


        };
    }
}
