package com.drmejia.core.persistence.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.drmejia.core.enums.LensType;
import com.drmejia.core.enums.OrderState;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrder;

    @Column(nullable = false)
    private String number;

    @ManyToOne
    @JoinColumn(name="id_patient", nullable = false)
    private PatientEntity patient;

    @ManyToOne
    @JoinColumn(name= "id_headquarters")
    private HeadquarterEntity headquarters;

    @ManyToOne
    @JoinColumn(name = "id_provider")
    private ProviderEntity provider;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @Column(name = "cost_price")
    private BigDecimal costPrice;

    @Column(nullable = false)
    private String frameType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LensType lensType;

    @Column(nullable = false)
    private LocalDate shippingDate;

    
    @Column(nullable = true)
    private LocalDate deliveryDate;

    @Column(nullable = true, columnDefinition = "INT GENERATED ALWAYS AS (DATEDIFF(delivery_date, shipping_date)) STORED", insertable = false, updatable = false)
    @SuppressWarnings("deprecation")
    @Generated(GenerationTime.ALWAYS)
    private Integer daysPassed;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private OrderState state;

    @Column(nullable = true, name = "received_by")
    private String receivedBy;
    
    @Column(nullable = true, name = "lab_voucher")
    private String labVoucher;
    
    @Column(nullable = true, name = "state_date")
    private LocalDate stateDate;

}
