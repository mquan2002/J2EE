package com.medical.repository;

import com.medical.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    Optional<Doctor> findByUsername(String username);
    Optional<Doctor> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
