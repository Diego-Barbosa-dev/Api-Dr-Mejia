package com.drmejia.core.persistence.entities;

import java.time.LocalDate;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity(name = "payments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPayment;

    
    @Column(nullable = false)
    private LocalDate paymentDate;

    @ManyToOne
    @JoinColumn(name = "id_order", nullable = false)
    private OrderEntity order;

    @OneToOne
    @JoinColumn(name = "id_comprobant", nullable = false)
    private ComprobantEntity comprobant;
}
