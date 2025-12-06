package com.drmejia.core.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private Long id;
    private String name;

    public boolean hasNullAttributes() {
        return this.id == null ||
               this.name == null || this.name.isBlank();
    }
}
