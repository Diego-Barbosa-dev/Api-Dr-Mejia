package com.drmejia.core.controllers;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/users")
public class UserRestController {

    @Autowired
    private UserService userService;

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
    @GetMapping("/{name}")
    public ResponseEntity<?> getByName(@PathVariable String name) throws BadRequestException{
        
        if(name.length() < 3){
            throw new BadRequestException("El parametro debe tener al menos 3 letras"); 
        }


        return userService.getAllUsers().stream().
                    filter(user -> user.getName().toLowerCase().contains(name)).
                    findFirst()
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario " + name + " no encontrado"));

    }

    /* Obtiene un usuario desde afuera obtenido de un JSON 
     * Gets an user from outside getting a JSON file
    */
    @PostMapping
    public ResponseEntity<?> altaUser(@RequestBody User user) throws BadRequestException{ 
        if(user.getName().length() < 3){
            throw new BadRequestException("El usuario no puede tener menos de 3 carácteres");
        }

        if(!user.getMail().contains("@")){
            throw new BadRequestException("El correo electrónico no es válido");
        }

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
                .path("/{name}")
                .buildAndExpand(user.getName())
                .toUri();
        return ResponseEntity.created(location).body(user);
    }


    @PutMapping
    public ResponseEntity<?> modifyUsers(@RequestBody User user){

        return null;
    }

    @DeleteMapping("/{userNit}")
    public ResponseEntity<?> deletUsers(@PathVariable int userNit){
  
        /* No Content para los DELETE 
         * No content for DELETE method
        */
        return ResponseEntity.noContent().build();
    }
}
