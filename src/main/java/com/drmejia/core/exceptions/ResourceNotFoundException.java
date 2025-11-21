package com.drmejia.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseBody
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException  extends RuntimeException{

    public ResourceNotFoundException(String massage){
        super(massage);
    }


    @Override
    public String getMessage(){
        return super.getMessage();
    }
}
