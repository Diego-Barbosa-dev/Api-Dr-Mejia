package com.drmejia.core.persistence.entities;

import java.time.LocalDate;

import com.drmejia.core.enums.OrderState;
import com.drmejia.core.models.Order;

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

    @OneToOne
    @JoinColumn(name="id_patient", nullable = false)
    private PatientEntity patient;

    @ManyToOne
    @JoinColumn(name= "id_headquarters")
    private HeadquarterEntity headquarters;

    @OneToOne
    @JoinColumn(name = "id_provider")
    private ProviderEntity provider;

    @Column(nullable = false)
    private LocalDate shippingDate;

    
    @Column(nullable = true)
    private LocalDate deliveryDate;

    @Column(nullable = true)
    private Integer daysPassed;

    @Column(nullable = false)
    private OrderState state;

}
