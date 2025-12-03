package com.drmejia.core.domain.models;

import java.time.LocalDate;

import com.drmejia.core.enums.OrderState;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    
    private Long idOrder;
    private String number;
    private String documentPatient;
    private Long idHeadquarter;
    private Long idProvider;
    private LocalDate shippingDate;
    private LocalDate deliveryDate;
    private Integer daysPassed;
    private OrderState state;

    public boolean hasNullAttributes() {
        return this.idOrder == null ||
               this.number == null || this.number.isBlank() ||
               this.documentPatient == null || this.documentPatient.isBlank() ||
               this.idHeadquarter == null ||
               this.idProvider == null ||
               this.shippingDate == null ||
               this.deliveryDate == null ||
               this.daysPassed == null ||
               this.state == null;
    }
}
