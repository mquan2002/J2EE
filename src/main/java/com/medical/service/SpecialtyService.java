package com.medical.service;

import com.medical.model.Specialty;
import com.medical.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecialtyService {
    @Autowired
    private SpecialtyRepository specialtyRepository;

    public Specialty findById(Long id) {
        return specialtyRepository.findById(id)
                .orElse(null);
    }

    public Specialty save(Specialty specialty) {
        return specialtyRepository.save(specialty);
    }
} 