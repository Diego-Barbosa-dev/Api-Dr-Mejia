package com.drmejia.core.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drmejia.core.security.JwtTokenUtil;
import com.drmejia.core.security.models.LoginRequest;
import com.drmejia.core.persistence.entities.UserEntity;
import com.drmejia.core.services.interfaces.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authManager,
                              UserDetailsService userDetailsService,
                              JwtTokenUtil jwtTokenUtil,
                              UserService userService) {
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // 1. AUTENTICAR
            Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 2. SACAR ROLES
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // 3. GENERAR EL TOKEN JWT
            String token = jwtTokenUtil.generateToken(userDetails.getUsername(), roles);

            // 4. RESPONDER CON EL TOKEN
            Map<String, Object> body = new HashMap<>();
            body.put("token", token);
            body.put("username", userDetails.getUsername());
            body.put("roles", roles);

            return ResponseEntity.ok(body);

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Credenciales inválidas"));
        }
    }
    
    @PostMapping("/user-info")
    public ResponseEntity<?> getUserInfo(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Username required"));
        }
        
        try {
            // Buscar por email o por nombre
            UserEntity user;
            if (username.contains("@")) {
                user = userService.findByEmail(username);
            } else {
                user = userService.findByName(username);
            }
            
            Map<String, Object> info = new HashMap<>();
            info.put("idHeadquarter", user.getHeadquarter() != null ? user.getHeadquarter().getIdHeadquarters() : null);
            return ResponseEntity.ok(info);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "User not found"));
        }
    }
}

