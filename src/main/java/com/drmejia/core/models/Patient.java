package com.drmejia.core.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    String document;
    String name;
    String email;
    String address;
    String notes;


    public boolean hasNullAttributes() {
        return this.document == null || this.document.isBlank() ||
               this.name == null || this.name.isBlank() ||
               this.email == null || this.email.isBlank() ||
               this.address == null || this.address.isBlank();
    }

}
