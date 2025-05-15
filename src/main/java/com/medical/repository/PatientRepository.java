package com.medical.repository;

import com.medical.model.Doctor;
import com.medical.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUsername(String username);
    Optional<Patient> findById(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
} 