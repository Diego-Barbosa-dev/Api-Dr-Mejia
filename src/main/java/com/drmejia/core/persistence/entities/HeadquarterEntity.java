package com.drmejia.core.persistence.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="headquarters")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HeadquarterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHeadquarters;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "headquarters", cascade = CascadeType.ALL)
    private List<OrderEntity> orders;
    
}