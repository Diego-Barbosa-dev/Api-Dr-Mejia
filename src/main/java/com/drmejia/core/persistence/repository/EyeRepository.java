package com.drmejia.core.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.drmejia.core.persistence.entities.EyeEntity;

@Repository
public interface EyeRepository extends JpaRepository<EyeEntity, Long>{
    
}
