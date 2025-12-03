package com.drmejia.core.controllers;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import com.drmejia.core.domain.models.User;

import com.drmejia.core.domain.services.interfaces.UserService;
import com.drmejia.core.exceptions.ResourceNotFoundException;


@RestController
@RequestMapping("api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    /*
     * Cada método de los controladores debe devolver
     * una respuesta, para ello se usará el ResponseEntity
     * y para hacerlo flexible nos aseguraremos de usar un
     * genérico (Léase Genericos de Java) que recibirá cualquier 
     * tipo dentro de los métodos de la clase ResponseEntity,
     * en este caso:
     * 
     *      ResponseEntity<?>
     * Every method from Controllers must return an aswer,
     * for it we'll use ResponseEntity class and for making
     * these more flexible will use a generic (read Java Gerenics)
     * that it'll be recieve every type inside methods from 
     * ResponseEntity class, in this case:
     *      
     *      ResponseEntity<?>
     * 
     */


    /* 
     * Función que regresa una lista de usuarios en JSON
     * Function for getting a list of users in JSON
     */
    @GetMapping
    public ResponseEntity<?> getUsers(){
        /*
         * ResponseEntity (de manera genérica) para una respuesta adecuada a los métodos GET y PUT
         * ResponseEntity (in generic mathod) for a correct answer with GET and PUT methods
         */
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /* Devuelve al usuario */
    @GetMapping("/{nit}")
    public ResponseEntity<?> getByNit(@PathVariable("nit") String nit) throws BadRequestException{
        
        return userService.getAllUsers().stream().
                    filter(user -> user.getNit().toLowerCase().contains(nit)).
                    findFirst()
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario " + nit + " no encontrado"));

    }

    /* Obtiene un usuario desde afuera obtenido de un JSON 
     * Gets an user from outside getting a JSON file
    */
    @PostMapping
    public ResponseEntity<?> postUser(@RequestBody User user) throws BadRequestException{ 

        if(user.hasNullAttributes()){
            throw new BadRequestException("Null Attributes Are Not Allowed"
                + "\nNit: " + user.getNit()
                + "\nName: " + user.getName()
                + "\nEmail: " + user.getEmail()
                + "\nPassword: " + "NOT ALLOWED"
                + "\nRole:" + user.getRole()
            );
        }

        if(user.getName().length() < 3){
            throw new BadRequestException("El usuario no puede tener menos de 3 carácteres");
        }

        if(!user.getEmail().contains("@")){
            throw new BadRequestException("El correo electrónico no es válido");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        
        /*
         * According to REST tesis, POST service must
         * return new created resource and an URL to 
         * access to it
         * 
         * De acuerdo a la tesis REST, el servicio POST
         * debe regresar el nuevo recurso creado y una
         * URL para acceder a ella 
         */

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{nit}")
                .buildAndExpand(user.getNit())
                .toUri();
        return ResponseEntity.created(location).body(user);
    }


    @PutMapping("/{nit}")
    public ResponseEntity<?> putUsers(@PathVariable("nit") String nit, @RequestBody User user){
        user.setNit(nit);
        try {
            userService.updateUser(user);
        } catch (BadRequestException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{nit}")
    public ResponseEntity<?> patchUser(@PathVariable("nit") String nit, @RequestBody User user) throws BadRequestException{
        user.setNit(nit);
        userService.modifyUser(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{nit}")
    public ResponseEntity<?> deleteUsers(@PathVariable("nit") String nit){
  
        /* No Content para los DELETE 
         * No content for DELETE method
        */
        userService.deleteUser(nit);
        return ResponseEntity.noContent().build();
    }
}
