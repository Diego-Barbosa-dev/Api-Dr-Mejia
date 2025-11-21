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
}
