package com.drmejia.core.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Provider {

    private Long idProvider;
    private String nit;
    private String name;
    private String address;
    private String email;

    public boolean hasNullAttributes() {
        return this.idProvider == null ||
               this.nit == null || this.nit.isBlank() ||
               this.name == null || this.name.isBlank() ||
               this.address == null || this.address.isBlank() ||
               this.email == null || this.email.isBlank();
    }
}
