package com.medical.controller;

import com.medical.model.Doctor;
import com.medical.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DoctorController {
    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping("/doctor")
    public String doctor(Model model) {
        List<Doctor> doctors = doctorRepository.findAll();
        model.addAttribute("doctors", doctors);

        model.addAttribute("pageTitle", "Bác sĩ");
        model.addAttribute("contentPage", "doctor");
        return "index"; // Trả về index.htm
    }
}
