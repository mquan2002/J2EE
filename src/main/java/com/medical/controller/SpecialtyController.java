package com.medical.controller;

import com.medical.model.Specialty;
import com.medical.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SpecialtyController {
    @Autowired
    private SpecialtyRepository specialtyRepository;

    @GetMapping("/specialty")
    public String specialty(Model model) {
        List<Specialty> specialties = specialtyRepository.findAll();
        model.addAttribute("specialties", specialties);

        model.addAttribute("pageTitle", "Chuyên Khoa");
        model.addAttribute("contentPage", "specialty");
        return "index"; // Trả về index.htm
    }
}
