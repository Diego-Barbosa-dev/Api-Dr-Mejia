package com.drmejia.core.models;

import java.time.LocalDate;
import java.math.BigDecimal;

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
    private String voucher;
    private BigDecimal amount;

    public boolean hasNullAttributes() {
        return this.paymentDate == null || this.idOrder == null || this.amount == null;
    }
}
