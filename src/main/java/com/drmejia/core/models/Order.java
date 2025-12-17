package com.drmejia.core.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.drmejia.core.enums.LensType;
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
    private BigDecimal salePrice;
    private BigDecimal costPrice;
    private String frameType;
    private LensType lensType;
    private LocalDate shippingDate;
    private LocalDate deliveryDate;
    private Integer daysPassed;
    private LocalDateTime creationDate;
    private OrderState state;
    private String receivedBy;
    private String labVoucher;
    private LocalDate stateDate;

    public boolean hasNullAttributes() {
        return this.number == null || this.number.isBlank() ||
               this.documentPatient == null || this.documentPatient.isBlank() ||
               this.idHeadquarter == null ||
               this.idProvider == null ||
               this.frameType == null || this.frameType.isBlank() ||
               this.lensType == null ||
               this.shippingDate == null  ||
               this.state == null;
    }
}
