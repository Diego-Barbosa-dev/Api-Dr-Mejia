package com.drmejia.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.drmejia.core.domain.services.interfaces.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final UserService userService;

    public SecurityConfig(UserService userService){
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder codifyPass(){ return new BCryptPasswordEncoder(); }

    @Bean
    public AuthenticationManager authentication(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        http.
            csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index.html").permitAll()
                .requestMatchers("/assets/css/styles.css").permitAll()
                .requestMatchers("/assets/js/auth.js").permitAll()
                .requestMatchers("/assets/img/**").permitAll()
                .requestMatchers("/api/**").hasAnyRole("ADMIN", "ASISTENTE", "PROVEEDOR")
                .anyRequest().authenticated()

            )
            .authenticationManager(authManager)
            .userDetailsService(userService)
            .formLogin(form -> form
                .loginPage("/index.html")
                .loginProcessingUrl("/login")
                .permitAll()
            )
            .httpBasic(basic -> {});
        
            return http.build();
    }

}
