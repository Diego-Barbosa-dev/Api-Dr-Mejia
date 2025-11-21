package com.drmejia.core.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* Clase hecha con la intención de representar a los usuarios de la app 
 * Class made for represent app's users
*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    private String nit;
    private String name;
    private String mail;
    private String password;
    private Long rol;
}












