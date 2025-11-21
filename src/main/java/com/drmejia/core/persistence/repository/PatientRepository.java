package com.drmejia.core.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.drmejia.core.persistence.entities.PatientEntity;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, Long> {
    Optional<PatientEntity> findByDocument(String document);
    Optional<PatientEntity> deleteByDocument(String document);
}
