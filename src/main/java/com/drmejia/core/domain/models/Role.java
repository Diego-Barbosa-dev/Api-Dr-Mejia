package com.drmejia.core.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private Long idRole;
    private String name;

    public boolean hasNullAttributes() {
        return this.idRole == null || this.name == null || this.name.isBlank();
    }
}
