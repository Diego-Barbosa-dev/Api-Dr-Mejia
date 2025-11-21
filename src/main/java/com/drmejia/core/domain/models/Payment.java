package com.drmejia.core.domain.models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    
    private Long idPayment;
    private LocalDate paymentDate;
    private Long idOrder;
    private Long idComprobant;
}
