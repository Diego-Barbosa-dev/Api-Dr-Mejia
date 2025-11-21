package com.drmejia.core.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.drmejia.core.persistence.entities.HeadquarterEntity;

@Repository
public interface HeadquaterRepository extends JpaRepository<HeadquarterEntity, Long>{
    
}
