package com.drmejia.core.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* 
 * Clase hecha con la intención de representar a los usuarios de la app 
 * Class made for represent app's users
 * 
 * 
*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    private String nit;
    private String name;
    private String email;
    private String password;
    private Long role;

    public boolean hasNullAttributes() {
        return this.nit == null || this.nit.isBlank() ||
               this.name == null || this.name.isBlank() ||
               this.email == null || this.email.isBlank() ||
               this.password == null || this.password.isBlank() ||
               this.role == null;
    }
}












