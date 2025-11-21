package com.drmejia.core.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.drmejia.core.persistence.entities.ProviderEntity;

@Repository
public interface ProviderRepository extends JpaRepository<ProviderEntity, Long> {
    void deleteById(Long idProvider);
}
