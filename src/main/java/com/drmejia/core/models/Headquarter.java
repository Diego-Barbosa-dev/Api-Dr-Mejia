package com.drmejia.core.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Headquarter {
    
    private Long idHeadquarter;
    private String name;
    
    public boolean hasNullAttributes() {
        return this.idHeadquarter == null || this.name == null || this.name.isBlank();
    }
}
