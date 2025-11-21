package com.drmejia.core.persistence.entities;

import com.drmejia.core.enums.EyeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "eyes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EyeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEye;

    @ManyToOne
    @JoinColumn(name = "id_orden", nullable = false)
    private OrderEntity orden;

    @Enumerated(EnumType.STRING)
    private EyeType type; // izquierdo o derecho

    @Column(nullable = false)
    private String esf;
    @Column(nullable = false)
    private String cil;
    @Column(nullable = false)
    private String axis;
    @Column(nullable = false)
    private String addition;
    @Column(nullable = false)
    private String dp;
    @Column(nullable = false)
    private String high;
    @Column(nullable = false)
    private String avl;
    @Column(nullable = false)
    private String avp;

}

