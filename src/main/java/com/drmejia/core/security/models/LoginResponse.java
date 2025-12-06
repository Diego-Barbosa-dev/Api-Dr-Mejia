package com.drmejia.core.security.models;

import java.util.List;

public class LoginResponse {
    private boolean authenticated;
    private String username;
    private List<String> roles;

    public LoginResponse(boolean authenticated, String username, List<String> roles) {
        this.authenticated = authenticated;
        this.username = username;
        this.roles = roles;
    }
    // getters y setters

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    
}

