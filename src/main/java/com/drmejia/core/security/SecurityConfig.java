package com.drmejia.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.drmejia.core.services.interfaces.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;
    private final JwtAuthFilter jwtFilter; // ⬅ INYECTAR TU FILTRO

    public SecurityConfig(UserService userService, JwtAuthFilter jwtFilter) {
        this.userService = userService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authentication(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityChain(HttpSecurity http) throws Exception {
        /*
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            ) // ⬅ Muy importante en JWT
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/", "/index.html").permitAll()
                    .requestMatchers("/assets/**").permitAll()
                    .requestMatchers("/auth/**").permitAll() // ⬅ Login sin token
                    .requestMatchers("/api/**").authenticated() // ⬅ lo demás necesita JWT
                    .anyRequest().authenticated()
            )
            .userDetailsService(userService)
            .httpBasic(basic -> basic.disable()) // ⬅ Desactivar Basic Auth
            .formLogin(form -> form.disable());  // ⬅ Desactivar Login por formulario

        // REGISTRAR EL JWT FILTER
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        
        */
       http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .anyRequest().permitAll()   // <- permite ALL sin autenticación
        )
        .formLogin(login -> login.disable()) // <- desactiva login
        .httpBasic(basic -> basic.disable()); // <- desactiva basic auth
        return http.build();
    }
}
